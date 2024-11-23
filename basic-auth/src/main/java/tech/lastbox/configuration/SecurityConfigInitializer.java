package tech.lastbox.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import tech.lastbox.*;
import tech.lastbox.enviroment.BasicAuthProperties;
import tech.lastbox.repository.TokenRepository;

import java.util.List;

@Configuration
public class SecurityConfigInitializer {
    private final BasicAuthProperties basicAuthProperties;
    private final TokenRepository tokenRepository;

    public SecurityConfigInitializer(BasicAuthProperties basicAuthProperties, TokenRepository tokenRepository) {
        this.basicAuthProperties = basicAuthProperties;
        this.tokenRepository = tokenRepository;
    }

    private JwtConfig getJwtConfig() {
        return new JwtConfig(JwtAlgorithm.HMAC256,
                basicAuthProperties.getSecretKey(),
                basicAuthProperties.getIssuer(),
                7,
                ExpirationTimeUnit.DAYS,
                tokenRepository);
    }

    @Bean
    public SecurityConfig initializeSecurity(SecurityConfig securityConfig) {
        securityConfig.corsAllowCredentials(true)
                .corsAllowedOrigins(List.of("*"))
                .addRouteAuthority("/admin", "ADMIN")
                .addRouteAuthority("/actuator", "ADMIN")
                .addRouteAuthority("/actuator/**", "ADMIN")
                .addRouteAuthority("/**", List.of("USER", "ADMIN"))
                .configureJwtService(getJwtConfig())
                .setCsrfProtection(true)
                .build();
        return securityConfig;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JwtService jwtService() {
        return new JwtService(getJwtConfig());
    }
}
