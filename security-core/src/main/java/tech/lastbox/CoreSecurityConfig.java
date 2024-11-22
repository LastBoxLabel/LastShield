package tech.lastbox;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

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
    private boolean csrfProtection = true;
    private final HashMap<String, GrantedAuthority> authorities = new HashMap<>();
    private final SecurityFilter securityFilter;

    public CoreSecurityConfig(SecurityFilter securityFilter, CorsConfig corsConfig) {
        this.securityFilter = securityFilter;
        this.corsConfig = corsConfig;
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
                .authorizeRequests(authorize -> {
                    if (authorities.isEmpty()) {
                        authorize.requestMatchers("/**").permitAll();
                    }
                    authorities.forEach((route, authority) -> {
                        ;
                        authorize.requestMatchers("/" + route + "/**").hasRole(authority.getAuthority());
                    });
                    authorize.anyRequest().authenticated();
                })
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class).build();

    }

    public void configureJwt(JwtConfig jwtConfig) {
        securityFilter.configureJwtService(jwtConfig);
    }

    public void addAuthority(HashMap<String, SimpleGrantedAuthority> authorities) {
        this.authorities.putAll(authorities);
    }

    public void setCsrfProtection(boolean csrfProtection) {
        this.csrfProtection = csrfProtection;
    }

    public synchronized void isCalled() {
        this.isCalled = true;
    }
}
