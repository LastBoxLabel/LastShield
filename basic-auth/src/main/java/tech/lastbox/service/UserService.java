package tech.lastbox.service;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import tech.lastbox.dto.UserDTO;
import tech.lastbox.entity.User;
import tech.lastbox.exception.*;

import java.util.List;
import java.util.Optional;

@Service
@ConditionalOnProperty(name = "lastshield.basicauth", havingValue = "true")
public class UserService {
    private final tech.lastbox.repository.UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(tech.lastbox.repository.UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User findUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userOptional = userRepository.findUserByUsername(username);
        if (userOptional.isEmpty()) throw new UsernameNotFoundException();
        return userOptional.get();
    }

    public User createUser(String name, String username, String rawPassword) throws DuplicatedUserException {
        if (userRepository.existsUserByUsername(username)) throw new DuplicatedUserException();
        return userRepository.save(new User(name, username, passwordEncoder.encode(rawPassword)));
    }

    public Optional<User> login(String username, String rawPassword) {
        return userRepository.findUserByUsername(username)
                .filter(user -> passwordEncoder.matches(rawPassword, user.getPassword()));
    }

    public List<UserDTO> getUsersAsDTO() {
        return userRepository.findAllUsersAsDTO();
    }

    public User updateUser(User user) throws UnregisteredUserException {
        if (user.getId() != null || user.getId() == 0) throw new UnregisteredUserException("User not registered to update.");
        return userRepository.save(user);
    }
}
