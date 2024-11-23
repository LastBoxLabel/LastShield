package tech.lastbox.basicauth.repository;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.jpa.repository.JpaRepository;
import tech.lastbox.TokenEntity;
import tech.lastbox.TokenStore;

/**
 * Repository interface for handling token-related operations.
 * This interface extends JpaRepository for CRUD operations and TokenStore for token-specific behavior.
 * <p>
 * The repository is conditional on the 'lastshield.basicauth' property being set to true.
 * This means the repository will only be active when the specified property is enabled.
 */
@ConditionalOnProperty(name = "lastshield.basicauth", havingValue = "true")
@Schema(description = "Repository for handling token data and providing token-specific operations.")
public interface TokenRepository extends JpaRepository<TokenEntity, String>, TokenStore {
}
