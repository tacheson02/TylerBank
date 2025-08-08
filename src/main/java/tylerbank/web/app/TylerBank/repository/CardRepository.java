package tylerbank.web.app.TylerBank.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tylerbank.web.app.TylerBank.entity.Card;

/**
 * Repository for managing Card entities.
 * @since v1.0
 */
public interface CardRepository extends JpaRepository<Card, String>{
}
