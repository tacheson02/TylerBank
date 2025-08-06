package tylerbank.web.app.TylerBank.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tylerbank.web.app.TylerBank.entity.Account;

/**
 * Repository for managing accounts.
 * @since v1.0
 */
public interface AccountRepository extends JpaRepository<Account, String>{
}
