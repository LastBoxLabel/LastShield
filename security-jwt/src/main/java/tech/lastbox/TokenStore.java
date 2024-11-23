package tech.lastbox;

import java.util.Optional;

/**
 * Interface for storing and retrieving token entities.
 * This interface defines http for saving and finding token entities in the token store.
 * It allows interaction with a storage system (e.g., a database) for token management.
 * <p>
 * Implementing classes should provide the logic for persisting and querying token entities
 * from a specific data source such as a relational database, in-memory store, etc.
 */
public interface TokenStore {

    /**
     * Saves a token entity in the store.
     *
     * @param tokenEntity the token entity to be saved.
     */
    TokenEntity save(TokenEntity tokenEntity);

    /**
     * Finds a token entity by its token string.
     *
     * @param token the token string of the entity to be retrieved.
     * @return an {@link Optional} containing the found token entity, or {@link Optional#empty()} if not found.
     */
    Optional<TokenEntity> findById(String token);
}
