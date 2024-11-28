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
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import tech.lastbox.lastshield.security.SecurityConfig;

import java.util.List;

/**
 * Configuration class that initializes security settings such as JWT, CORS, and route protection.
 * This class is activated when the "lastshield.basicauth" property is set to true.
 */
@Configuration
@ConditionalOnProperty(name = "lastshield.basicauth", havingValue = "true")
@ComponentScan(basePackages = "tech.lastbox.lastshield")
public class SecurityConfigInitializer {

    /**
     * Configures security settings for the application.
     *
     * @param securityConfig an instance of SecurityConfig to be configured (auto-injected by Spring IoC).
     * Initialize the configured SecurityConfig instance.
     */
    public SecurityConfigInitializer(SecurityConfig securityConfig) {
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
                .setCsrfProtection(false);
        securityConfig.build();
    }
}
