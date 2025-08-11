package tylerbank.web.app.TylerBank.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tylerbank.web.app.TylerBank.dto.AccountDto;
import tylerbank.web.app.TylerBank.dto.TransferDto;
import tylerbank.web.app.TylerBank.entity.*;
import tylerbank.web.app.TylerBank.repository.AccountRepository;
import tylerbank.web.app.TylerBank.repository.TransactionRepository;
import tylerbank.web.app.TylerBank.service.helper.AccountHelper;
import tylerbank.web.app.TylerBank.util.RandomUtil;

import javax.naming.OperationNotSupportedException;
import java.util.List;

/**
 * Service layer for managing accounts.
 * @since v2.1
 */
@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final AccountHelper accountHelper;
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
        accountRepository.save(account);

        // Return the created account.
        return account;
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
    @Transactional
    public Transaction transferFunds(TransferDto transferDto, User user) throws Exception {
        //Create a sender and receiver accounts and fee variables.
        var senderAccount = accountRepository.findByCodeAndOwnerUid(transferDto.getCode(), user.getUid())
                .orElseThrow(() -> new IllegalArgumentException("Sender account not found"));
        var receiverAccount = accountRepository.findByAccountNumber(transferDto.getRecipientAccountNumber())
                .orElseThrow(() -> new IllegalArgumentException("Receiver account not found"));

        //Validate if sender has sufficient funds and receiver account is correct type.
        accountHelper.validateSufficientFunds(senderAccount, transferDto.getAmount()*TRANSACTION_FEE);
        accountHelper.validateAccountType(receiverAccount, transferDto.getCode());

        //Update the balances of the sender and receiver accounts.
        senderAccount.setBalance(senderAccount.getBalance() - transferDto.getAmount()*TRANSACTION_FEE);
        receiverAccount.setBalance(receiverAccount.getBalance() + transferDto.getAmount());

        // Save the updated accounts.
        accountRepository.saveAll(List.of(senderAccount, receiverAccount));

        //Create a sender and receiver transaction for records.
        var senderTransaction = Transaction.builder()
                .account(senderAccount)
                .status(Status.COMPLETED)
                .type(Type.WITHDRAWAL)
                .txFee(transferDto.getAmount()*TRANSACTION_FEE-transferDto.getAmount())
                .amount(transferDto.getAmount())
                .owner(user)
                .build();
        var receiverTransaction = Transaction.builder()
                .account(receiverAccount)
                .status(Status.COMPLETED)
                .type(Type.DEPOSIT)
                .txFee(0.0)
                .amount(transferDto.getAmount())
                .owner(receiverAccount.getOwner())
                .build();

        //Save the transactions and return the senders record.
        return transactionRepository.saveAll(List.of(senderTransaction, receiverTransaction)).getFirst();
    }
}