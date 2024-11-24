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
package tech.lastbox.lastshield.security;

import org.springframework.stereotype.Component;
import tech.lastbox.lastshield.security.core.CoreSecurityConfig;
import tech.lastbox.lastshield.security.core.CorsConfig;
import tech.lastbox.lastshield.security.core.RouteAuthority;
import tech.lastbox.lastshield.security.core.http.HttpMethod;

import java.util.HashSet;
import java.util.List;

/**
 * Class responsible for dynamically configuring the application's security rules.
 * It allows defining CORS policies, CSRF protection, and route permissions,
 * delegating configuration to {@link CorsConfig} and {@link CoreSecurityConfig}.
 */
@Component
public class SecurityConfig {
    private final CorsConfig corsConfig;
    private final CoreSecurityConfig coreSecurityConfig;

    /**
     * Constructor that injects the necessary dependencies.
     *
     * @param coreSecurityConfig The core security configuration (auto-injected by Spring IoC).
     * @param corsConfig The CORS configuration (auto-injected by Spring IoC).
     */
    public SecurityConfig(CoreSecurityConfig coreSecurityConfig, CorsConfig corsConfig) {
        this.coreSecurityConfig = coreSecurityConfig;
        this.corsConfig = corsConfig;
    }

    /**
     * Sets a single allowed origin for CORS.
     *
     * @param allowedOrigin The allowed origin.
     * @return The current instance of {@link SecurityConfig} for method chaining.
     */
    public SecurityConfig corsAllowedOrigins(String allowedOrigin) {
        corsConfig.setAllowedOrigins(allowedOrigin);
        return this;
    }

    /**
     * Sets multiple allowed origins for CORS.
     *
     * @param allowedOrigins A list of allowed origins.
     * @return The current instance of {@link SecurityConfig} for method chaining.
     */
    public SecurityConfig corsAllowedOrigins(List<String> allowedOrigins) {
        corsConfig.setAllowedOrigins(allowedOrigins);
        return this;
    }

    /**
     * Sets a single allowed HTTP method for CORS.
     *
     * @param allowedMethod The allowed HTTP method.
     * @return The current instance of {@link SecurityConfig} for method chaining.
     */
    public SecurityConfig corsAllowMethods(String allowedMethod) {
        corsConfig.setAllowedMethods(allowedMethod);
        return this;
    }

    /**
     * Sets multiple allowed HTTP methods for CORS.
     *
     * @param allowedMethods A list of allowed HTTP methods.
     * @return The current instance of {@link SecurityConfig} for method chaining.
     */
    public SecurityConfig corsAllowedMethods(List<String> allowedMethods) {
        corsConfig.setAllowedMethods(allowedMethods);
        return this;
    }

    /**
     * Sets a single allowed header for CORS.
     *
     * @param allowedHeader The allowed header.
     * @return The current instance of {@link SecurityConfig} for method chaining.
     */
    public SecurityConfig corsAllowedHeaders(String allowedHeader) {
        corsConfig.setAllowedHeaders(allowedHeader);
        return this;
    }

    /**
     * Sets multiple allowed headers for CORS.
     *
     * @param allowedHeaders A list of allowed headers.
     * @return The current instance of {@link SecurityConfig} for method chaining.
     */
    public SecurityConfig corsAllowedHeaders(List<String> allowedHeaders) {
        corsConfig.setAllowedHeaders(allowedHeaders);
        return this;
    }

    /**
     * Enables or disables credentials support for CORS.
     *
     * @param allowCredentials {@code true} to allow credentials, {@code false} otherwise.
     * @return The current instance of {@link SecurityConfig} for method chaining.
     */
    public SecurityConfig corsAllowCredentials(boolean allowCredentials) {
        corsConfig.setAllowCredentials(allowCredentials);
        return this;
    }

    /**
     * Enables or disables CSRF protection.
     *
     * @param csrfProtection {@code true} to enable CSRF protection, {@code false} to disable it.
     * @return The current instance of {@link SecurityConfig} for method chaining.
     */
    public SecurityConfig setCsrfProtection(boolean csrfProtection) {
        coreSecurityConfig.setCsrfProtection(csrfProtection);
        return this;
    }

    /**
     * Adds a public route without restrictions on HTTP methods.
     *
     * @param path The route path.
     * @return The current instance of {@link SecurityConfig} for method chaining.
     */
    public SecurityConfig addRouteAuthority(String path){
        coreSecurityConfig.addAuthority(new RouteAuthority(path));
        return this;
    }

    /**
     * Adds a public route restricted to a specific HTTP method.
     *
     * @param path The route path.
     * @param httpMethod The allowed HTTP method.
     * @return The current instance of {@link SecurityConfig} for method chaining.
     */
    public SecurityConfig addRouteAuthority(String path, HttpMethod httpMethod){
        coreSecurityConfig.addAuthority(new RouteAuthority(path, httpMethod));
        return this;
    }

    /**
     * Adds a public route restricted to multiple HTTP methods.
     *
     * @param path The route path.
     * @param httpMethods A set of allowed HTTP methods.
     * @return The current instance of {@link SecurityConfig} for method chaining.
     */
    public SecurityConfig addRouteAuthority(String path, HashSet<HttpMethod> httpMethods){
        httpMethods.forEach(httpMethod -> coreSecurityConfig.addAuthority(new RouteAuthority(path, httpMethod)));
        return this;
    }

    /**
     * Adds a route restricted to a specific user role.
     *
     * @param path The route path.
     * @param role The required user role.
     * @return The current instance of {@link SecurityConfig} for method chaining.
     */
    public SecurityConfig addRouteAuthority(String path, String role){
        coreSecurityConfig.addAuthority(new RouteAuthority(path, role));
        return this;
    }

    /**
     * Adds a route restricted to multiple user roles.
     *
     * @param path The route path.
     * @param roles A list of required user roles.
     * @return The current instance of {@link SecurityConfig} for method chaining.
     */
    public SecurityConfig addRouteAuthority(String path, List<String> roles){
        coreSecurityConfig.addAuthority(new RouteAuthority(path, roles));
        return this;
    }

    /**
     * Adds a route restricted to a specific user role and HTTP method.
     *
     * @param path The route path.
     * @param role The required user role.
     * @param httpMethod The allowed HTTP method.
     * @return The current instance of {@link SecurityConfig} for method chaining.
     */
    public SecurityConfig addRouteAuthority(String path, String role, HttpMethod httpMethod){
        coreSecurityConfig.addAuthority(new RouteAuthority(path, role, httpMethod));
        return this;
    }

    /**
     * Adds a route restricted to a specific user role and multiple HTTP methods.
     *
     * @param path The route path.
     * @param role The required user role.
     * @param httpMethods A list of allowed HTTP methods.
     * @return The current instance of {@link SecurityConfig} for method chaining.
     */
    public SecurityConfig addRouteAuthority(String path, String role, List<HttpMethod> httpMethods){
        httpMethods.forEach(httpMethod -> coreSecurityConfig.addAuthority(new RouteAuthority(path, role, httpMethod)));
        return this;
    }

    /**
     * Finalizes and validates the security configuration.
     */
    public void build(){
        coreSecurityConfig.isCalled();
    }
}

