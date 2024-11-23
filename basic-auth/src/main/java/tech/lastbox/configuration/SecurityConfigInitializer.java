package tech.lastbox.configuration;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import tech.lastbox.*;
import tech.lastbox.enviroment.BasicAuthProperties;
import tech.lastbox.repository.TokenRepository;

import java.util.List;

@Configuration
@ConditionalOnProperty(name = "lastshield.basicauth", havingValue = "true")
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
    @ConditionalOnProperty(name = "lastshield.basicauth", havingValue = "true")
    public SecurityConfig initializeSecurity(SecurityConfig securityConfig) {
        securityConfig.corsAllowCredentials(true)
                .corsAllowedOrigins(List.of("*"))
                .corsAllowedMethods(List.of("*"))
                .addRouteAuthority("/login")
                .addRouteAuthority("/register")
                .addRouteAuthority("/api-docs/**")
                .addRouteAuthority("/swagger-ui/**")
                .addRouteAuthority("/admin", "ADMIN")
                .addRouteAuthority("/actuator", "ADMIN")
                .addRouteAuthority("/actuator/**", "ADMIN")
                .addRouteAuthority("/**")
                .setCsrfProtection(false)
                .build();
        return securityConfig;
    }

    @Bean
    @ConditionalOnProperty(name = "lastshield.basicauth", havingValue = "true")
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @ConditionalOnProperty(name = "lastshield.basicauth", havingValue = "true")
    public JwtService jwtService() {
        return new JwtService(getJwtConfig());
    }
}
