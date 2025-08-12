package tylerbank.web.app.TylerBank.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tylerbank.web.app.TylerBank.dto.AccountDto;
import tylerbank.web.app.TylerBank.dto.ConversionDto;
import tylerbank.web.app.TylerBank.dto.TransferDto;
import tylerbank.web.app.TylerBank.entity.*;
import tylerbank.web.app.TylerBank.repository.AccountRepository;
import tylerbank.web.app.TylerBank.repository.TransactionRepository;
import tylerbank.web.app.TylerBank.service.helper.AccountHelper;
import tylerbank.web.app.TylerBank.util.RandomUtil;

import java.util.List;
import java.util.Map;

/**
 * Service layer for managing accounts.
 * @since v2.1
 */
@Service
@RequiredArgsConstructor
@Transactional
public class AccountService {

    private final AccountRepository accountRepository;
    private final TransactionService transactionService;
    private final AccountHelper accountHelper;
    private final ExchangeRateService exchangeRateService;
    private final double TRANSACTION_FEE = 1.01;

    /**
     * Creates a new account for a user.
     * @param accountDto
     * @param user
     * @return
     * @throws Exception
     * @since v2.1
     */
    public Account createAccount(AccountDto accountDto, User user) throws Exception  {
        long accountNumber;

        // Validate if account of this type and code does not already exist for the given user.
        accountHelper.validateAccountNonExistence(accountDto.getCode(), user.getUid());

        // Generate a unique account number.
        do{
            accountNumber = new RandomUtil().generateRandom(10);
        } while (accountRepository.existsByAccountNumber(accountNumber));

        // Create and save the account.
        var account = Account.builder()
                .accountNumber(accountNumber)
                .balance(1000)
                .owner(user)
                .code(accountDto.getCode())
                .symbol(accountDto.getSymbol())
                .label(accountHelper.getCURRENCIES().get(accountDto.getCode()))
                .build();
        return accountRepository.save(account);
    }

    /**
     * Retrieves all accounts for a given user.
     * @param uid
     * @return
     * @since v2.1
     */
    public List<Account> getUserAccounts(String uid) {
        return accountRepository.findAllByOwnerUid(uid);
    }

    /**
     * Transfer funds between two accounts.
     * @param transferDto
     * @param user
     * @return
     * @throws Exception
     * @since v2.2
     */
    public Transaction transferFunds(TransferDto transferDto, User user) throws Exception {
        // Create a sender and receiver accounts and fee variables.
        var senderAccount = accountRepository.findByCodeAndOwnerUid(transferDto.getCode(), user.getUid())
                .orElseThrow(() -> new IllegalArgumentException("Sender account not found"));
        var receiverAccount = accountRepository.findByAccountNumber(transferDto.getRecipientAccountNumber())
                .orElseThrow(() -> new IllegalArgumentException("Receiver account not found"));

        // Validate if sender has sufficient funds and receiver account is correct type.
        accountHelper.validateSufficientFunds(senderAccount, transferDto.getAmount()*TRANSACTION_FEE);
        accountHelper.validateAccountType(receiverAccount, transferDto.getCode());

        // Update the balances of the sender and receiver accounts and save the updated accounts.
        senderAccount.setBalance(senderAccount.getBalance() - transferDto.getAmount()*TRANSACTION_FEE);
        receiverAccount.setBalance(receiverAccount.getBalance() + transferDto.getAmount());
        accountRepository.saveAll(List.of(senderAccount, receiverAccount));

        // Create a sender and receiver transaction for records.
        var senderTransaction = transactionService.createAccountTransaction(transferDto.getAmount()
                , Type.WITHDRAWAL, transferDto.getAmount()*TRANSACTION_FEE-transferDto.getAmount()
                , senderAccount, user);
        transactionService.createAccountTransaction(transferDto.getAmount()
                , Type.DEPOSIT, 0.0
                , receiverAccount, receiverAccount.getOwner());

        // Save the transactions and return the senders record.
        return senderTransaction;
    }

    /**
     * Converts funds between accounts to a different currency.
     * @param conversionDto
     * @param uid
     * @return
     * @throws Exception
     * @since v2.3
     */
    public Transaction convertCurrency(ConversionDto conversionDto, String uid) throws Exception {
        // Validate the conversion request and sender's account balance.
        accountHelper.validateConversion(conversionDto, uid);

        // Create variables for currency exchange rates and converted amount
        var rates = exchangeRateService.getRates();
        var sendingRates = rates.get(conversionDto.getFromCurrency());
        var receivingRates = rates.get(conversionDto.getToCurrency());
        var convertedAmount = receivingRates/sendingRates * conversionDto.getAmount();

        // Create variable for sender and receiver accounts.
        var senderAccount = accountRepository.findByCodeAndOwnerUid(conversionDto.getFromCurrency(), uid)
                .orElseThrow(() -> new IllegalArgumentException("Exchange sending account not found"));
        var receiverAccount = accountRepository.findByCodeAndOwnerUid(conversionDto.getToCurrency(), uid)
                .orElseThrow(() -> new IllegalArgumentException("Exchange receiving account not found"));

        // Update account balance and save them in the repository
        senderAccount.setBalance((senderAccount.getBalance() - conversionDto.getAmount()) * TRANSACTION_FEE);
        receiverAccount.setBalance(receiverAccount.getBalance() + convertedAmount);
        accountRepository.saveAll(List.of(senderAccount, receiverAccount));

        // Create, save, and return a transaction for records.
        return transactionService.createAccountTransaction
                (conversionDto.getAmount(), Type.WITHDRAWAL,
                conversionDto.getAmount()*TRANSACTION_FEE-conversionDto.getAmount(), senderAccount,
                senderAccount.getOwner());
    }

    /**
     * Retrieves the current exchange rates.
     * @return
     * @since v2.3
     */
    public Map<String, Double> getExchangeRate() {
        return exchangeRateService.getRates();
    }
}