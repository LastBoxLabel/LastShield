package tech.lastbox;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.HashMap;
import java.util.List;

@Configuration
public class SecurityInitializer {

    @Bean
    public SecurityConfig initializeSecurity(SecurityConfig securityConfig) {
        securityConfig.corsAllowCredentials(true)
                .corsAllowedMethods(List.of("GET"))
                .corsAllowedOrigins(List.of("*"))
                .addRouteAuthority(new HashMap<String, SimpleGrantedAuthority>() {{
                    put("/**", new SimpleGrantedAuthority("QUANDO EU QUISER"));
                }})
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
