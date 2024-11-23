package tech.lastbox.basicauth.repository;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import tech.lastbox.security.core.annotations.UserHandler;
import tech.lastbox.basicauth.dto.UserDTO;
import tech.lastbox.basicauth.entity.User;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for handling User-related database operations.
 * This interface extends JpaRepository for CRUD operations on User entities and provides custom queries
 * for fetching user data with specific roles or by specific fields.
 * <p>
 * The repository is conditionally activated based on the 'lastshield.basicauth' property.
 * It is only available when this property is set to true.
 */
@UserHandler
@ConditionalOnProperty(name = "lastshield.basicauth", havingValue = "true")
@Schema(description = "Repository for handling user data and operations, including fetching users based on role, ID, and username.")
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Find a user by their unique ID.
     *
     * @param id the unique identifier of the user.
     * @return an Optional containing the User if found, empty if not.
     */
    @Schema(description = "Finds a user by their unique ID.")
    Optional<User> findUserById(Long id);

    /**
     * Find a user by their unique username.
     *
     * @param username the username of the user.
     * @return an Optional containing the User if found, empty if not.
     */
    @Schema(description = "Finds a user by their unique username.")
    Optional<User> findUserByUsername(String username);

    /**
     * Checks if a user exists with the given username.
     *
     * @param username the username to check.
     * @return true if a user with the given username exists, false otherwise.
     */
    @Schema(description = "Checks if a user exists with the specified username.")
    boolean existsUserByUsername(String username);

    /**
     * Find a user by their role.
     *
     * @param role the role of the user.
     * @return an Optional containing the User if found, empty if not.
     */
    @Schema(description = "Finds the first user by their role.")
    Optional<User> findUserByRole(String role);

    /**
     * Find all users with the specified role.
     *
     * @param role the role of the users.
     * @return a List of Users with the specified role.
     */
    @Schema(description = "Finds all users with the specified role.")
    List<User> findUsersByRole(String role);

    /**
     * Retrieves a list of all users with the role 'USER', returning only the necessary fields in a UserDTO.
     *
     * @return a list of UserDTO objects representing users with the 'USER' role.
     */
    @Query("SELECT new tech.lastbox.basicauth.dto.UserDTO(u.id, u.name, u.username, u.role) FROM User u WHERE u.role = 'USER'")
    @Schema(description = "Retrieves all users with the 'USER' role as UserDTOs.")
    List<UserDTO> findAllUsersAsDTO();
}
