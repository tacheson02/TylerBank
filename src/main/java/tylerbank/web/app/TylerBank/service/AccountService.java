package tylerbank.web.app.TylerBank.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tylerbank.web.app.TylerBank.dto.AccountDto;
import tylerbank.web.app.TylerBank.entity.Account;
import tylerbank.web.app.TylerBank.entity.User;
import tylerbank.web.app.TylerBank.repository.AccountRepository;
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
    private final AccountHelper accountHelper;

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
        validateAccountNonExistence(accountDto.getCode(), user.getUid());

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
}