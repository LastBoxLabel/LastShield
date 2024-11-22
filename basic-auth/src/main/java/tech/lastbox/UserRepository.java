package tech.lastbox;

import org.springframework.data.jpa.repository.JpaRepository;
import tech.lastbox.annotations.UserHandler;

@UserHandler
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}
