/*
 * Copyright 2024 LastBox
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package tech.lastbox.lastshield.basicauth.mapper;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import tech.lastbox.lastshield.basicauth.dto.UserDTO;
import tech.lastbox.lastshield.basicauth.entity.User;
import tech.lastbox.lastshield.basicauth.repository.UserRepository;

/**
 * Mapper class that provides the transformation between User entity and UserDTO.
 * The class is conditional on the 'lastshield.basicauth' property being set to true.
 * <p>
 * This class maps a UserDTO to a User entity and vice versa.
 * It uses the UserRepository to fetch User entities based on UserDTO information.
 */
@Component
@ConditionalOnProperty(name = "lastshield.basicauth", havingValue = "true")
@Schema(description = "Mapper for converting UserDTO to User entity and vice versa.")
public class UserMapper {

    private final UserRepository userRepository;

    /**
     * Constructor that injects the UserRepository.
     *
     * @param userRepository the UserRepository instance for fetching User entities.
     */
    public UserMapper(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Maps a UserDTO to a User entity.
     *
     * @param userDTO the UserDTO instance to be converted.
     * @return the corresponding User entity.
     */
    @Schema(description = "Converts a UserDTO into a User entity.")
    public User toEntity(@Schema(description = "The UserDTO object containing the user information.") UserDTO userDTO) {
        return userRepository.findUserById(userDTO.id()).get();
    }

    /**
     * Maps a User entity to a UserDTO.
     *
     * @param user the User entity to be converted.
     * @return the corresponding UserDTO.
     */
    @Schema(description = "Converts a User entity into a UserDTO object.")
    public UserDTO toDto(@Schema(description = "The User entity object containing the user information.") User user) {
        return new UserDTO(user.getId(), user.getName(), user.getUsername(), user.getRole());
    }
}
