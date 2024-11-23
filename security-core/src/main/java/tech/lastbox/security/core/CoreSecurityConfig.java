package tech.lastbox.security.core;

import jakarta.servlet.http.HttpServletResponse;
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

    public CoreSecurityConfig(CorsConfig corsConfig, SecurityUtil securityUtil, SecurityFilter securityFilter) {
        this.corsConfig = corsConfig;
        this.securityUtil = securityUtil;
        this.securityFilter = securityFilter;
    }

    void setAdvancedFilter() {
        AdvancedFilterChecker.setAdvancedFiltered(true);
    }

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

    private Customizer<AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry> configureAuthorities() {
        return authorize -> {
            authorities.forEach(authority -> configureAuthority(authorize, authority));
            authorize.anyRequest().authenticated();
        };
    }

    private void configureAuthority(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry authorize,
                                    RouteAuthority authority) {
        if (authority.getHttpMethods().isEmpty()) {
            configurePathOnly(authorize, authority);
        } else {
            configurePathWithMethods(authorize, authority);
        }
    }

    private void configurePathOnly(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry authorize,
                                   RouteAuthority authority) {
        var matcher = authorize.requestMatchers(authority.getPath());
        applyAuthorization(matcher, authority.getRoles());
    }

    private void configurePathWithMethods(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry authorize,
                                          RouteAuthority authority) {
        authority.getHttpMethods().forEach(method -> {
            var matcher = authorize.requestMatchers(method.name(), authority.getPath());
            applyAuthorization(matcher, authority.getRoles());
        });
    }

    private void applyAuthorization(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizedUrl authorizedUrl,
                                    String[] roles) {
        if (roles == null) {
            authorizedUrl.permitAll();
        } else {
            authorizedUrl.hasAnyRole(roles);
        }
    }

    private void configureCsrfProtection(CsrfConfigurer<HttpSecurity> csrf){
        if(!csrfProtection) csrf.disable();
    }

    public void addAuthority(RouteAuthority routeAuthority) {
        this.authorities.add(routeAuthority);
        if (routeAuthority.getRoles() == null) {
            AdvancedFilterChecker.addShoudNotFilterPath(routeAuthority.getPath());
        }
        if (!AdvancedFilterChecker.isAdvancedFiltered()) {
            setAdvancedFilter();
            securityFilter.setUserRepository(securityUtil.getUserRepositoryClass());
        }
    }

    public void setCsrfProtection(boolean csrfProtection) {
        this.csrfProtection = csrfProtection;
    }

    public void isCalled(){
        this.isCalled = true;
    }

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

    private void handleUnauthorized(HttpServletResponse response) throws IOException {
        writeErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
    }

    private void handleAccessDenied(HttpServletResponse response) throws IOException {
        writeErrorResponse(response, HttpServletResponse.SC_FORBIDDEN, "Access Denied");
    }
}
