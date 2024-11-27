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
package tech.lastbox.lastshield.security.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.CorsConfigurer;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

/**
 * The {@code CorsConfig} class is responsible for configuring Cross-Origin Resource Sharing (CORS) settings
 * for the application. It allows defining allowed origins, methods, headers, and whether credentials are allowed.
 * <p>
 * This configuration is used to control which domains can access resources in your application and how cross-origin
 * requests are handled.
 */
@Configuration
public class CorsConfig {
    private List<String> allowedOrigins;
    private List<String> allowedMethods;
    private List<String> allowedHeaders;
    private Boolean allowCredentials;
    private final Logger logger = LoggerFactory.getLogger(CorsConfig.class);

    /**
     * Default constructor for the CorsConfig class.
     * This constructor is used to instantiate the CorsConfig class.
     */
    public CorsConfig() {
    }

    /**
     * Configures CORS settings for HTTP security by returning a {@link Customizer} that customizes the {@link CorsConfigurer}
     * for HTTP security. This method is invoked to apply CORS configurations, such as allowed origins, methods, headers,
     * and credentials, to the HTTP security configuration.
     * <p>
     * If the attributes {@link CorsConfig#allowedOrigins}, {@link CorsConfig#allowedMethods},
     * {@link CorsConfig#allowedHeaders}, or {@link CorsConfig#allowCredentials} are not set,
     * default values will be applied, effectively allowing unrestricted CORS access.
     * <p>
     * The returned {@link Customizer} is used to configure the {@link CorsConfigurer} as part of the security filter chain,
     * enabling fine-grained control over cross-origin request handling in the application.
     *
     * @return a {@link Customizer} that applies CORS settings to HTTP security.
     */
    @Bean
    public Customizer<CorsConfigurer<HttpSecurity>> configure(){
        return cors -> corsConfigurationSource();
    }

    private void corsConfigurationSource() {
        logger.info("✅ CORS Configuration Initialized");
        CorsConfiguration configuration = new CorsConfiguration();

        if(allowedOrigins == null) configuration.setAllowedOrigins(List.of("*"));
        if(allowedMethods == null) configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        if(allowedHeaders == null) configuration.setAllowedHeaders(List.of("*"));
        if(allowCredentials == null) configuration.setAllowCredentials(true);

        configuration.setAllowedOrigins(allowedOrigins);
        configuration.setAllowedMethods(allowedMethods);
        configuration.setAllowCredentials(allowCredentials);
        configuration.addAllowedHeader("*");

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
    }

    /**
     * Sets the allowed origins for cross-origin requests.
     *
     * @param origin a single allowed origin to be added to the list.
     */
    public void setAllowedOrigins(String origin) {this.allowedOrigins.add(origin);}

    /**
     * Sets the allowed origins for cross-origin requests.
     *
     * @param allowedOrigins a list of allowed origins for cross-origin requests.
     */
    public void setAllowedOrigins(List<String> allowedOrigins) {
        this.allowedOrigins = allowedOrigins;
    }

    /**
     * Sets the allowed HTTP methods for cross-origin requests.
     *
     * @param method a single allowed HTTP method (e.g., GET, POST) to be added to the list.
     */
    public void setAllowedMethods(String method) {this.allowedMethods.add(method);}

    /**
     * Sets the allowed HTTP methods for cross-origin requests.
     *
     * @param allowedMethods a list of allowed HTTP methods (e.g., GET, POST, PUT, DELETE).
     */
    public void setAllowedMethods(List<String> allowedMethods) {
        this.allowedMethods = allowedMethods;
    }

    /**
     * Sets the allowed headers for cross-origin requests.
     *
     * @param header a single allowed header to be added to the list.
     */
    public void setAllowedHeaders(String header) {this.allowedHeaders.add(header);}

    /**
     * Sets the allowed headers for cross-origin requests.
     *
     * @param allowedHeaders a list of allowed headers for cross-origin requests.
     */
    public void setAllowedHeaders(List<String> allowedHeaders) {
        this.allowedHeaders = allowedHeaders;
    }

    /**
     * Sets whether credentials (cookies, HTTP authentication, etc.) are allowed in cross-origin requests.
     *
     * @param allowCredentials {@code true} to allow credentials, {@code false} to disallow credentials.
     */
    public void setAllowCredentials(Boolean allowCredentials) {
        this.allowCredentials = allowCredentials;
    }
}
