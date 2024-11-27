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

import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * The {@code CoreSecurityConfig} class configures the security settings for the web application,
 * including authorization, authentication, and Cross-Site Request Forgery (CSRF) protection.
 * It uses Spring Security to define custom security filter chains and manages path-based access control
 * using {@link RouteAuthority} objects.
 * <p>
 * This class is responsible for setting up the security filter chain, including the handling of custom
 * CORS configurations, CSRF protection settings, and adding custom authorities for route access.
 */
@Configuration
@ComponentScan
@EnableWebSecurity
public class CoreSecurityConfig {
    private boolean isCalled = false;
    private boolean csrfProtection = true;
    private final CorsConfig corsConfig;
    private final SecurityUtil securityUtil;
    private final SecurityFilter securityFilter;
    private final List<RouteAuthority> authorities = new ArrayList<>();
    private final Logger logger = LoggerFactory.getLogger(CoreSecurityConfig.class);

    /**
     * Constructs a new {@code CoreSecurityConfig} instance with the required dependencies for CORS configuration,
     * security utilities, and a custom security filter. This constructor uses Spring's Dependency Injection
     * mechanism to inject the necessary beans into the class.
     *
     * @param corsConfig the {@link CorsConfig} object used to configure Cross-Origin Resource Sharing (CORS) settings.
     * @param securityUtil the {@link SecurityUtil} utility used for various security-related operations, such as user repository handling.
     * @param securityFilter the {@link SecurityFilter} that is added to the security filter chain to handle custom authentication and authorization.
     */
    public CoreSecurityConfig(CorsConfig corsConfig, SecurityUtil securityUtil, SecurityFilter securityFilter) {
        this.corsConfig = corsConfig;
        this.securityUtil = securityUtil;
        this.securityFilter = securityFilter;
    }

    /**
     * Enables the advanced filter by updating the state in {@link AdvancedFilterChecker}.
     */
    void setAdvancedFilter() {
        AdvancedFilterChecker.setAdvancedFiltered(true);
    }

    /**
     * Configures the default {@link SecurityFilterChain} with CORS, CSRF protection, and authority-based
     * access control.
     * <p>
     * If the {@code build()} method was not previously called, an exception will be thrown. This method
     * sets up the security filter chain by adding the {@link SecurityFilter} and configuring paths and roles.
     *
     * @param http the {@link HttpSecurity} object used to configure security settings.
     * @return the configured {@link SecurityFilterChain}.
     * @throws Exception if there is any error while configuring the security filter chain.
     */
    @Bean
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        if (!isCalled) throw new RuntimeException("Build() method is not called");

        try {
            return http
                    .cors(corsConfig.configure())
                    .csrf(this::configureCsrfProtection)
                    .authorizeHttpRequests(configureAuthorities())
                    .exceptionHandling(exceptionHandling ->
                            exceptionHandling
                                    .authenticationEntryPoint((request, response, authException) -> handleUnauthorized(response))
                                    .accessDeniedHandler((request, response, accessDeniedException) -> handleAccessDenied(response))
                    )
                    .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
                    .build();
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    /**
     * Configures the authorities by iterating over the list of {@link RouteAuthority} objects.
     * It applies the corresponding role-based access control to each route.
     *
     * @return a customizer for authorizing HTTP requests based on route authorities.
     */
    private Customizer<AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry> configureAuthorities() {
        return authorize -> {
            authorities.forEach(authority -> configureAuthority(authorize, authority));
            authorize.anyRequest().authenticated();
        };
    }

    /**
     * Configures the authorization for a specific {@link RouteAuthority}. If the authority has no associated HTTP methods,
     * it configures access control for the path only. Otherwise, it configures the path along with the HTTP methods.
     *
     * @param authorize the authorization manager registry.
     * @param authority the {@link RouteAuthority} to configure.
     */
    private void configureAuthority(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry authorize,
                                    RouteAuthority authority) {
        if (authority.getHttpMethods().isEmpty()) {
            configurePathOnly(authorize, authority);
        } else {
            configurePathWithMethods(authorize, authority);
        }
    }

    /**
     * Configures access control for a route path that does not have associated HTTP methods.
     *
     * @param authorize the authorization manager registry.
     * @param authority the {@link RouteAuthority} to configure.
     */
    private void configurePathOnly(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry authorize,
                                   RouteAuthority authority) {
        var matcher = authorize.requestMatchers(authority.getPath());
        applyAuthorization(matcher, authority.getRoles());
    }

    /**
     * Configures access control for a route path with associated HTTP methods.
     *
     * @param authorize the authorization manager registry.
     * @param authority the {@link RouteAuthority} to configure.
     */
    private void configurePathWithMethods(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry authorize,
                                          RouteAuthority authority) {
        authority.getHttpMethods().forEach(method -> {
            var matcher = authorize.requestMatchers(method.name(), authority.getPath());
            applyAuthorization(matcher, authority.getRoles());
        });
    }

    /**
     * Applies authorization rules (permitAll or hasAnyRole) to the matched URL based on the roles of the {@link RouteAuthority}.
     *
     * @param authorizedUrl the URL being authorized.
     * @param roles the roles associated with the authority.
     */
    private void applyAuthorization(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizedUrl authorizedUrl,
                                    String[] roles) {
        if (roles == null) {
            authorizedUrl.permitAll();
        } else {
            authorizedUrl.hasAnyRole(roles);
        }
    }

    /**
     * Configures CSRF protection based on the {@code csrfProtection} flag.
     * If CSRF protection is disabled, it will be turned off for the application.
     *
     * @param csrf the CSRF configuration object.
     */
    private void configureCsrfProtection(CsrfConfigurer<HttpSecurity> csrf){
        if(!csrfProtection) csrf.disable();
    }

    /**
     * Adds a {@link RouteAuthority} to the list of authorities. If the provided {@link RouteAuthority} does not have any associated roles,
     * the path of the authority is added to the list of paths that should not be filtered by the advanced filter using the
     * {@link AdvancedFilterChecker#addShoudNotFilterPath(String)} method.
     * <p>
     * If the advanced filter is not currently enabled ({@link AdvancedFilterChecker#isAdvancedFiltered()} returns {@code false}),
     * this method will enable it by calling {@link #setAdvancedFilter()} and configure the {@link SecurityFilter} with the appropriate
     * user repository class using {@link SecurityUtil#getUserRepositoryClass()}.
     *
     * @param routeAuthority the {@link RouteAuthority} object representing the authority to be added.
     */
    public void addAuthority(RouteAuthority routeAuthority) {
        this.authorities.add(routeAuthority);
        logger.debug("Added authority: {}", routeAuthority);

        if (routeAuthority.getRoles() == null) {
            AdvancedFilterChecker.addShoudNotFilterPath(routeAuthority.getPath());
            logger.debug("Added path to not filter: {}", routeAuthority.getPath());
        }

        if (!AdvancedFilterChecker.isAdvancedFiltered()) {
            setAdvancedFilter();
            securityFilter.setUserRepository(securityUtil.getUserRepositoryClass());
            logger.debug("Advanced filter set up with repository: {}",
                    securityUtil.getUserRepositoryClass().getName());
        }
    }

    /**
     * Enables or disables CSRF protection for the application.
     *
     * @param csrfProtection {@code true} to enable CSRF protection, {@code false} to disable it.
     */
    public void setCsrfProtection(boolean csrfProtection) {
        this.csrfProtection = csrfProtection;
    }

    /**
     * Marks the security configuration as initialized. This should be called after the configuration is properly set up.
     */
    public void isCalled(){
        this.isCalled = true;
    }

    /**
     * Writes an error response with the given status code and message in JSON format.
     *
     * @param response the HTTP response object.
     * @param status the HTTP status code.
     * @param message the error message.
     * @throws IOException if there is an error writing to the response.
     */
    private void writeErrorResponse(HttpServletResponse response, int status, String message) throws IOException {
        response.setStatus(status);
        response.setContentType("application/json");
        String jsonResponse = String.format(
                "{\"message\": \"%s\", \"status\": %d, \"timestamp\": \"%s\"}",
                message,
                status,
                LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
        );
        response.getWriter().write(jsonResponse);
    }

    /**
     * Handles unauthorized access attempts by writing an "Unauthorized" error response.
     *
     * @param response the HTTP response object.
     * @throws IOException if there is an error writing to the response.
     */
    private void handleUnauthorized(HttpServletResponse response) throws IOException {
        writeErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
    }

    /**
     * Handles access-denied attempts by writing an "Access Denied" error response.
     *
     * @param response the HTTP response object.
     * @throws IOException if there is an error writing to the response.
     */
    private void handleAccessDenied(HttpServletResponse response) throws IOException {
        writeErrorResponse(response, HttpServletResponse.SC_FORBIDDEN, "Access Denied");
    }
}
