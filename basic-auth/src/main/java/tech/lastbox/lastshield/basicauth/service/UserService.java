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
package tech.lastbox.lastshield.basicauth.service;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import tech.lastbox.lastshield.basicauth.dto.UserDTO;
import tech.lastbox.lastshield.basicauth.entity.User;
import tech.lastbox.lastshield.basicauth.exception.DuplicatedUserException;
import tech.lastbox.lastshield.basicauth.exception.UnregisteredUserException;
import tech.lastbox.lastshield.basicauth.exception.UsernameNotFoundException;
import tech.lastbox.lastshield.basicauth.repository.UserRepository;

import java.util.List;
import java.util.Optional;

/**
 * Service class for managing user-related operations, such as creating new users, logging in, and updating user details.
 * This service is conditionally enabled based on the 'lastshield.basicauth' property, meaning it is only active
 * when basic authentication is enabled in the application.
 */
@Service
@ConditionalOnProperty(name = "lastshield.basicauth", havingValue = "true")
@Schema(description = "Service for managing user-related operations, including user creation, login, and updates.")
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Find a user by their username.
     *
     * @param username the username of the user to find.
     * @return the User object if found.
     * @throws UsernameNotFoundException if no user with the given username is found.
     */
    @Schema(description = "Finds a user by their username. Throws UsernameNotFoundException if the user does not exist.")
    public User findUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userOptional = userRepository.findUserByUsername(username);
        if (userOptional.isEmpty()) throw new UsernameNotFoundException();
        return userOptional.get();
    }

    /**
     * Creates a new user.
     * If a user with the given username already exists, throws DuplicatedUserException.
     *
     * @param name the name of the new user.
     * @param username the username of the new user.
     * @param rawPassword the raw password for the new user.
     * @return the created User object.
     * @throws DuplicatedUserException if a user with the given username already exists.
     */
    @Schema(description = "Creates a new user. Throws DuplicatedUserException if the username already exists.")
    public User createUser(String name, String username, String rawPassword) throws DuplicatedUserException {
        if (userRepository.existsUserByUsername(username)) throw new DuplicatedUserException();
        return userRepository.save(new User(name, username, passwordEncoder.encode(rawPassword)));
    }

    /**
     * Attempts to log in with the provided username and password.
     * Returns an Optional containing the user if the login is successful (username matches and password is correct).
     *
     * @param username the username to log in with.
     * @param rawPassword the raw password to check.
     * @return an Optional containing the User if login is successful, empty otherwise.
     */
    @Schema(description = "Attempts to log in with the given username and raw password.")
    public Optional<User> login(String username, String rawPassword) {
        return userRepository.findUserByUsername(username)
                .filter(user -> passwordEncoder.matches(rawPassword, user.getPassword()));
    }

    /**
     * Retrieves all users as UserDTO objects.
     *
     * @return a List of UserDTOs representing all users.
     */
    @Schema(description = "Retrieves all users as UserDTO objects.")
    public List<UserDTO> getUsersAsDTO() {
        return userRepository.findAllUsersAsDTO();
    }

    /**
     * Updates the user details.
     * If the user is not registered (i.e., ID is null or 0), throws UnregisteredUserException.
     *
     * @param user the user object containing the updated information.
     * @return the updated User object.
     * @throws UnregisteredUserException if the user is not registered in the database.
     */
    @Schema(description = "Updates the user details. Throws UnregisteredUserException if the user is not registered.")
    public User updateUser(User user) throws UnregisteredUserException {
        if (user.getId() == null || user.getId() == 0) throw new UnregisteredUserException("User not registered to update.");
        return userRepository.save(user);
    }
}
