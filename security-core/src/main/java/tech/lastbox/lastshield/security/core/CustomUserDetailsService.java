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
package tech.lastbox.lastshield.security.core;

import org.springframework.context.ApplicationContext;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import tech.lastbox.lastshield.security.core.annotations.Password;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

/**
 * {@code CustomUserDetailsService} is an implementation of {@link UserDetailsService} that loads user-specific data
 * during authentication. It fetches user details from a dynamically determined user repository, using the username to
 * identify the user and retrieving the user's password from a field annotated with {@link Password}.
 * <p>
 * This service uses Spring's {@link ApplicationContext} to retrieve the appropriate user repository based on the configuration
 * provided by {@link SecurityUtil}. The password is extracted from a field in the user entity class that is annotated with
 * {@link Password}.
 * </p>
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final SecurityUtil securityUtil;
    private final ApplicationContext applicationContext;
    private Object userRepository;

    /**
     * Constructs a {@code CustomUserDetailsService} with the required dependencies for security utilities and the
     * Spring application context. This constructor uses Spring's Dependency Injection
     * mechanism to inject the necessary beans into the class.
     *
     * @param securityUtil the utility class for security-related operations, such as finding users by username.
     * @param applicationContext the Spring {@link ApplicationContext} used to retrieve the user repository bean.
     */
    public CustomUserDetailsService(SecurityUtil securityUtil, ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
        this.securityUtil = securityUtil;
    }

    /**
     * Sets the user repository by retrieving the corresponding repository bean from the Spring application context,
     * based on the repository class provided by {@link SecurityUtil}.
     */
    private void setUserRepository() {
        this.userRepository = applicationContext.getBean(securityUtil.getUserRepositoryClass());
    }

    /**
     * Loads the user details by username. This method fetches the user entity from the user repository and retrieves
     * the password field that is annotated with {@link Password}.
     * <p>
     * If the user is not found, a {@link RuntimeException} is thrown. If no field with the {@link Password} annotation
     * is found in the user entity, an {@link IllegalStateException} is thrown.
     * </p>
     *
     * @param username the username of the user whose details are to be loaded.
     * @return a {@link UserDetails} object containing the user's username and password.
     * @throws UsernameNotFoundException if the user with the given username cannot be found.
     */
    @Override
    @SuppressWarnings("unchecked")
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            if (this.userRepository == null) setUserRepository();

            Optional<Object> userEntityOptional = (Optional<Object>) securityUtil.findUserByUsername(userRepository, username);
            if (userEntityOptional.isEmpty()) throw new RuntimeException("User not found.");
            Object userEntity = userEntityOptional.get();
            String password = Arrays.stream(userEntity.getClass().getDeclaredFields())
                    .filter(field -> field.isAnnotationPresent(Password.class))
                    .map(field -> {
                        try {
                            field.setAccessible(true);
                            return (String) field.get(userEntity);
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }).findFirst().orElseThrow(() -> new IllegalStateException("No field with @Password annotation found"));

            return new User(username, password, new ArrayList<>());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
