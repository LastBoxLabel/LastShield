package tech.lastbox.lastshield.basicauth.configuration;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import tech.lastbox.jwt.ExpirationTimeUnit;
import tech.lastbox.jwt.JwtAlgorithm;
import tech.lastbox.jwt.JwtConfig;
import tech.lastbox.jwt.JwtService;
import tech.lastbox.lastshield.basicauth.enviroment.BasicAuthProperties;
import tech.lastbox.lastshield.basicauth.repository.TokenRepository;

@Configuration
public class JwtInitializer {
    private final BasicAuthProperties basicAuthProperties;
    private final TokenRepository tokenRepository;

    public JwtInitializer(BasicAuthProperties basicAuthProperties, TokenRepository tokenRepository) {
        this.basicAuthProperties = basicAuthProperties;
        this.tokenRepository = tokenRepository;
    }

    /**
     * Creates a JWT configuration bean.
     *
     * @return JwtConfig for configuring JWT behavior.
     */
    private JwtConfig getJwtConfig() {
        return new JwtConfig(JwtAlgorithm.HMAC256,
                basicAuthProperties.getSecretKey(),
                basicAuthProperties.getIssuer(),
                7,
                ExpirationTimeUnit.DAYS,
                tokenRepository);
    }

    /**
     * Creates a PasswordEncoder bean using BCryptPasswordEncoder.
     *
     * @return PasswordEncoder that uses BCrypt for hashing passwords.
     */
    @Bean
    @ConditionalOnProperty(name = "lastshield.basicauth", havingValue = "true")
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Creates a JwtService bean for generating JWT tokens.
     *
     * @return JwtService used for handling JWT generation and validation.
     */
    @Bean
    @ConditionalOnProperty(name = "lastshield.basicauth", havingValue = "true")
    public JwtService jwtService() {
        return new JwtService(getJwtConfig());
    }
}
