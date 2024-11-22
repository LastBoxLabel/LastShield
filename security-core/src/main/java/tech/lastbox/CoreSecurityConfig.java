package tech.lastbox;


import jakarta.annotation.PostConstruct;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import org.springframework.context.annotation.Lazy;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Configuration
@ComponentScan
@EnableWebSecurity
public class CoreSecurityConfig {
    private boolean isCalled = false;
    private final CorsConfig corsConfig;
    private String jwtSecretKey;
    private long jwtExpiration;
    private final SecurityUtil securityUtil;
    private boolean csrfProtection = true;
    private final List<RouteAuthority> authorities = new ArrayList<>();
    private final SecurityFilter securityFilter;

    public CoreSecurityConfig(CorsConfig corsConfig, SecurityUtil securityUtil, SecurityFilter securityFilter) {
        this.corsConfig = corsConfig;
        this.securityUtil = securityUtil;
        this.securityFilter = securityFilter;
    }

    void setAdvancedFilter() {
        AdvancedFilterChecker.setAdvancedFiltered(!authorities.isEmpty());
    }


    @Bean
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        if (!isCalled) {
            throw new RuntimeException("Build() method is not called");
        }
        return http
                .cors(cors -> corsConfig.corsConfigurationSource())
                .csrf(csrf -> {
                    if (!csrfProtection) csrf.disable();
                })
                .authorizeHttpRequests(authorize -> {
                    if (authorities.isEmpty()) {
                        authorize.requestMatchers("/**").permitAll();
                    } else {
                        authorities.forEach((authority) -> {
                            System.out.println(authority);
                            authorize.requestMatchers(authority.getPath()).hasAnyRole(authority.getRoles());
                        });
                        authorize.anyRequest().authenticated();
                    }
                })
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class).build();
    }

    public void configureJwt(JwtConfig jwtConfig) {
        JwtServiceConfig.configureJwtService(jwtConfig);
    }

    public void addAuthority(RouteAuthority routeAuthority) {
        this.authorities.add(routeAuthority);
        setAdvancedFilter();
        securityFilter.setUserService(securityUtil.getUserServiceInstance());
    }

    public void setCsrfProtection(boolean csrfProtection) {
        this.csrfProtection = csrfProtection;
    }

    public synchronized void isCalled() {
        this.isCalled = true;
    }
}
