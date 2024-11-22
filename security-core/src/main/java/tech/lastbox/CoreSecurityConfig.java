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

import java.util.HashMap;

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
    private final HashMap<String, GrantedAuthority> authorities = new HashMap<>();
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
                        authorities.forEach((route, authority) -> {
                            authorize.requestMatchers(route).hasRole(authority.getAuthority());
                        });
                        authorize.anyRequest().authenticated();
                    }
                })
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class).build();
    }

    public void configureJwt(JwtConfig jwtConfig) {
        JwtServiceConfig.configureJwtService(jwtConfig);
    }

    public void addAuthority(HashMap<String, SimpleGrantedAuthority> authorities) {
        this.authorities.putAll(authorities);
        setAdvancedFilter();
        securityUtil.getUserServiceInstance();
    }

    public void setCsrfProtection(boolean csrfProtection) {
        this.csrfProtection = csrfProtection;
    }

    public synchronized void isCalled() {
        this.isCalled = true;
    }
}
