package tech.lastbox.repository;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import tech.lastbox.annotations.UserHandler;
import tech.lastbox.dto.UserDTO;
import tech.lastbox.entity.User;

import java.util.List;
import java.util.Optional;

@UserHandler
@ConditionalOnProperty(name = "lastshield.basicauth", havingValue = "true")
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findUserById(Long id);
    Optional<User> findUserByUsername(String email);
    boolean existsUserByUsername(String email);
    Optional<User> findUserByRole(String role);
    List<User> findUsersByRole(String role);

    @Query("SELECT new tech.lastbox.dto.UserDTO(u.id, u.name, u.username, u.role) FROM User u WHERE u.role = 'USER'")
    List<UserDTO> findAllUsersAsDTO();
}
