package tylerbank.web.app.TylerBank.service.helper;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import tylerbank.web.app.TylerBank.entity.Account;
import tylerbank.web.app.TylerBank.entity.User;
import tylerbank.web.app.TylerBank.repository.AccountRepository;

import java.util.Map;

/**
 * Helper class for managing accounts and currency conversion.
 * @since v2.1
 */
@Component
@RequiredArgsConstructor
@Getter
public class AccountHelper {

    private final AccountRepository accountRepository;

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
}
