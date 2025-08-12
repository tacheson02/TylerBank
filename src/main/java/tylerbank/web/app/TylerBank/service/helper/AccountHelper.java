package tylerbank.web.app.TylerBank.service.helper;

import jakarta.transaction.Transactional;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import tylerbank.web.app.TylerBank.dto.ConversionDto;
import tylerbank.web.app.TylerBank.entity.*;
import tylerbank.web.app.TylerBank.repository.AccountRepository;
import tylerbank.web.app.TylerBank.repository.TransactionRepository;

import java.util.Map;

/**
 * Helper class for managing accounts and currency conversion.
 * @since v2.1
 */
@Component
@RequiredArgsConstructor
@Getter
@Transactional
public class AccountHelper {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    private final Map<String, String> CURRENCIES = Map.of(
            "USD", "United States Dollar",
            "EUR", "Euro",
            "GBP", "British Pound",
            "JPY", "Japanese Yen",
            "INR", "Indian Rupee",
            "CAD", "Canadian Dollar",
            "CNY", "Chinese Yuan"
    );

    /**
     * Validates if an account of the given type and code does not already exist for the given user.
     * @param code
     * @param uid
     * @throws Exception
     * @since v2.1
     */
    public void validateAccountNonExistence(String code, String uid) throws Exception  {
        if(accountRepository.existsByCodeAndOwnerUid(code, uid)) {
            throw new Exception("Account of this type already exists for this user");
        }
    }

    /**
     * Validates if the given account balance is sufficient to cover the given amount.
     * @param account
     * @param amount
     * @throws Exception
     * @since v2.2
     */
    public void validateSufficientFunds(Account account, double amount) throws Exception  {
        if (account.getBalance() < amount) {
            throw new Exception("Insufficient funds in sender account");
        }
    }

    /**
     * Validates if the given account type matches the expected type.
     * @param receiverAccount
     * @param code
     * @throws Exception
     * @since v2.2
     */
    public void validateAccountType(Account receiverAccount, String code) throws Exception {
        if (!receiverAccount.getCode().equals(code)) {
            throw new Exception("Receiver account is not " + code);
        }
    }

    /**
     * Validates if the currencies in the given conversion request are different.
     * @param conversiontDto
     * @throws Exception
     * @since v2.3
     */
    public void validateDifferentCurrency(ConversionDto conversiontDto) throws Exception {
        if (conversiontDto.getFromCurrency().equals(conversiontDto.getToCurrency())) {
            throw new Exception("Conversion from the same currency is not allowed");
        }
    }

    /**
     * Validates if all requirements for conversion is met
     * @param conversionDto
     * @param uid
     * @throws Exception
     * @since v2.3
     */
    public void validateConversion(ConversionDto conversionDto, String uid) throws Exception {
        validateDifferentCurrency(conversionDto);
        validateSufficientFunds(accountRepository.findByCodeAndOwnerUid(conversionDto.getFromCurrency(), uid)
                .orElseThrow(() -> new Exception("Account not found"))
                ,conversionDto.getAmount());
    }

    /**
     * Creates a new account transaction with the given parameters
     * @param amount
     * @param type
     * @param txFee
     * @param account
     * @param owner
     * @return
     * @since v2.4
     */
    public Transaction createAccountTransaction(double amount, Type type, double txFee, Account account, User owner) {
        var transaction = Transaction.builder()
                .txFee(txFee)
                .amount(amount)
                .type(type)
                .status(Status.COMPLETED)
                .owner(owner)
                .build();
        return transactionRepository.save(transaction);
    }

    /**
     * Creates a new card transaction with the given parameters
     * @param amount
     * @param type
     * @param txFee
     * @param card
     * @param owner
     * @return
     * @since v2.4
     */
    public Transaction createCardTransaction(double amount, Type type, double txFee, Card card, User owner) {
        var transaction = Transaction.builder()
                .txFee(txFee)
                .amount(amount)
                .type(type)
                .status(Status.COMPLETED)
                .owner(owner)
                .card(card)
                .build();
        return transactionRepository.save(transaction);
    }
}
