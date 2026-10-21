package by.mluvse.authService.repository;

import by.mluvse.authService.entity.Credentials;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CredentialsRepository  extends JpaRepository<Credentials,Long> {
    boolean existsByLogin(String login);
    Optional<Credentials> findByLogin(String login);
}
