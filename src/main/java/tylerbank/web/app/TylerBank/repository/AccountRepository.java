package tylerbank.web.app.TylerBank.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tylerbank.web.app.TylerBank.entity.Account;

import java.util.List;

/**
 * Repository for managing accounts.
 * @since v1.0
 */
public interface AccountRepository extends JpaRepository<Account, String>{
    boolean existsByAccountNumber(long accountNumber);
    boolean existsByCodeAndOwnerUid(String code, String uid);
    List<Account> findAllByOwnerUid(String uid);
}
