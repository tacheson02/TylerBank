package tylerbank.web.app.TylerBank.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tylerbank.web.app.TylerBank.entity.Transaction;

/**
 * Repository for managing transactions.
 * @since v1.0
 */
public interface TransactionRepository extends JpaRepository<Transaction, String>{
}
