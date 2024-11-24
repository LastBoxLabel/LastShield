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
package tech.lastbox.lastshield.basicauth.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import tech.lastbox.lastshield.basicauth.entity.User;
import tech.lastbox.lastshield.basicauth.enviroment.BasicAuthProperties;
import tech.lastbox.lastshield.basicauth.repository.UserRepository;

import java.util.Optional;

/**
 * Configuration class for setting up an admin user.
 * This configuration is activated when the "lastshield.basicauth" property is true.
 */
@Configuration
@ConditionalOnProperty(name = "lastshield.basicauth", havingValue = "true")
public class AdminConfig {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final BasicAuthProperties basicAuthProperties;
    private final Logger logger = LoggerFactory.getLogger(AdminConfig.class);

    public AdminConfig(UserRepository userRepository, PasswordEncoder passwordEncoder, BasicAuthProperties basicAuthProperties) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.basicAuthProperties = basicAuthProperties;
    }

    /**
     * CommandLineRunner bean that runs when the application starts.
     * It checks if an admin user exists, and if not, creates one with the configured properties.
     *
     * @return CommandLineRunner that sets up or updates the admin user.
     */
    @Bean
    @ConditionalOnProperty(name = "lastshield.basicauth", havingValue = "true")
    public CommandLineRunner setupAdminUser() {
        return args -> {
            String adminName = basicAuthProperties.getAdminName();
            String adminUsername = basicAuthProperties.getAdminUsername();
            String adminPassword = basicAuthProperties.getAdminPassword();

            Optional<User> adminOptional = userRepository.findUserByRole("ADMIN");

            if (adminOptional.isEmpty()) {
                User adminUser = new User();
                adminUser.setName(adminName);
                adminUser.setUsername(adminUsername);
                adminUser.setPassword(passwordEncoder.encode(adminPassword));
                adminUser.setRole("ADMIN");
                userRepository.save(adminUser);
                logger.info("Admin user registered with login: {}", adminUsername);
            } else {
                User adminUser = adminOptional.get();
                if (!adminUser.getName().equals(adminName)) adminUser.setName(adminName);
                if (!adminUser.getUsername().equals(adminUsername)) adminUser.setUsername(adminUsername);
                if (!passwordEncoder.matches(adminPassword, adminUser.getPassword())) adminUser.setPassword(passwordEncoder.encode(adminPassword));
                userRepository.save(adminUser);
                logger.info("Admin user is set with login: {}", adminUsername);
            }
        };
    }
}
