package tech.lastbox.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import tech.lastbox.entity.User;
import tech.lastbox.enviroment.BasicAuthProperties;
import tech.lastbox.repository.UserRepository;

import java.util.Optional;

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
                logger.info("Admin user is set with login: {}", adminUsername);
            }
        };
    }
}
