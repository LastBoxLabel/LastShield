package tech.lastbox;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SecurityConfigInitializer {
    private final BasicAuthProperties basicAuthProperties;

    public SecurityConfigInitializer(BasicAuthProperties basicAuthProperties) {
        this.basicAuthProperties = basicAuthProperties;
    }

    @Bean
    public SecurityConfig initializeSecurity(SecurityConfig securityConfig) {
        securityConfig.corsAllowCredentials(true)
                .corsAllowedOrigins(List.of("*"))
                .addRouteAuthority("/admin", "ADMIN")
                .addRouteAuthority("/**", List.of("USER", "ADMIN"))
                .configureJwtService(new JwtConfig(JwtAlgorithm.HMAC256,
                        basicAuthProperties.getSecretKey(),
                        basicAuthProperties.getIssuer(),
                        7,
                        ExpirationTimeUnit.DAYS))
                .setCsrfProtection(true)
                .build();
        return securityConfig;
    }
}
