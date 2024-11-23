package tech.lastbox;

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
}
