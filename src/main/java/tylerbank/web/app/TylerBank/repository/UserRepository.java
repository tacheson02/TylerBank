package tylerbank.web.app.TylerBank.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tylerbank.web.app.TylerBank.entity.User;

public interface UserRepository extends JpaRepository<User, String> {


    User findByUsernameIgnoreCase(String username);
}
