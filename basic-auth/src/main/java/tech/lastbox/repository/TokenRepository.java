package tech.lastbox.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tech.lastbox.TokenEntity;
import tech.lastbox.TokenStore;

public interface TokenRepository extends JpaRepository<TokenEntity, String>, TokenStore {
}
