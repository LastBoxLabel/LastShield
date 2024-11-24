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

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import tech.lastbox.lastshield.basicauth.enviroment.BasicAuthProperties;
import tech.lastbox.lastshield.basicauth.repository.TokenRepository;
import tech.lastbox.jwt.ExpirationTimeUnit;
import tech.lastbox.jwt.JwtAlgorithm;
import tech.lastbox.jwt.JwtConfig;
import tech.lastbox.jwt.JwtService;
import tech.lastbox.lastshield.security.SecurityConfig;

import java.util.List;

/**
 * Configuration class that initializes security settings such as JWT, CORS, and route protection.
 * This class is activated when the "lastshield.basicauth" property is set to true.
 */
@Configuration
@ConditionalOnProperty(name = "lastshield.basicauth", havingValue = "true")
public class SecurityConfigInitializer {

    private final BasicAuthProperties basicAuthProperties;
    private final TokenRepository tokenRepository;

    public SecurityConfigInitializer(BasicAuthProperties basicAuthProperties, TokenRepository tokenRepository) {
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
     * Configures security settings for the application.
     *
     * @param securityConfig an instance of SecurityConfig to be configured (auto-injected by Spring IoC).
     * @return the configured SecurityConfig instance.
     */
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
                .addRouteAuthority("/**", List.of("USER", "ADMIN"))
                .setCsrfProtection(false)
                .build();
        return securityConfig;
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
