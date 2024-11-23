package tech.lastbox.mapper;

import org.springframework.stereotype.Component;
import tech.lastbox.dto.UserDTO;
import tech.lastbox.entity.User;
import tech.lastbox.repository.UserRepository;

@Component
public class UserMapper {
    private final UserRepository userRepository;

    public UserMapper(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User toEntity(UserDTO userDTO) {
        return userRepository.findUserById(userDTO.id()).get();
    }

    public UserDTO toDto(User user) {
        return new UserDTO(user.getId(), user.getName(), user.getUsername(), user.getRole());
    }
}
