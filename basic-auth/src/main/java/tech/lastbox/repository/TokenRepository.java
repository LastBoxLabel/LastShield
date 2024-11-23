package tech.lastbox.repository;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.jpa.repository.JpaRepository;
import tech.lastbox.TokenEntity;
import tech.lastbox.TokenStore;

@ConditionalOnProperty(name = "lastshield.basicauth", havingValue = "true")
public interface TokenRepository extends JpaRepository<TokenEntity, String>, TokenStore {
}
