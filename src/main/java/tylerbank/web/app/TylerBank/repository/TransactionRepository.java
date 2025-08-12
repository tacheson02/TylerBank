package tylerbank.web.app.TylerBank.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import tylerbank.web.app.TylerBank.entity.Transaction;

/**
 * Repository for managing transactions.
 * @since v1.0
 */
public interface TransactionRepository extends JpaRepository<Transaction, String>{
    Page<Transaction> findAllByOwnerUid(String uid, Pageable pageable);
    Page<Transaction> findAllByCardCardIdAndOwnerUid(String cardId, String uid, Pageable pageable);
    Page<Transaction> findAllByAccountAccountIdAndOwnerUid(String accountId, String uid, Pageable pageable);
}
