package tylerbank.web.app.TylerBank.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tylerbank.web.app.TylerBank.entity.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, String>{


}
