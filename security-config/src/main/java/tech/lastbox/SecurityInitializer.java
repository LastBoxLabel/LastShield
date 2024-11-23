package tech.lastbox;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tech.lastbox.http.HttpMethod;

import java.util.List;

@Configuration
public class SecurityInitializer {

    @Bean
    public SecurityConfig initializeSecurity(SecurityConfig securityConfig) {
        securityConfig.corsAllowCredentials(true)
                .corsAllowedMethods(List.of("GET"))
                .corsAllowedOrigins(List.of("*"))
                .addRouteAuthority("/user", "USER")
                .addRouteAuthority("/admin", List.of("USER", "ADMIN"))
                .addRouteAuthority("/pica/**", "USER", HttpMethod.GET)
                .addRouteAuthority("/pica2/**", "ADMIN", List.of(HttpMethod.GET, HttpMethod.POST))
                .configureJwtService(new JwtConfig(JwtAlgorithm.HMAC256,
                        "pindamonhangaba",
                        "user",
                        2,
                        ExpirationTimeUnit.DAYS))
                .setCsrfProtection(true)
                .build();

        return securityConfig;
    }
}

