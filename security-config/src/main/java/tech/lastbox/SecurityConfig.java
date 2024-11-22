package tech.lastbox;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;

@Component
public class SecurityConfig {
    private CorsConfig corsConfig;
    private CoreSecurityConfig coreSecurityConfig;

    public SecurityConfig(CoreSecurityConfig coreSecurityConfig, CorsConfig corsConfig) {
        this.coreSecurityConfig = coreSecurityConfig;
        this.corsConfig = corsConfig;
    }

    public SecurityConfig corsAllowedOrigins(List<String> allowedOrigins) {
        corsConfig.setAllowedOrigins(allowedOrigins);
        return this;
    }

    public SecurityConfig corsAllowedMethods(List<String> allowedMethods) {
        corsConfig.setAllowedMethods(allowedMethods);
        return this;
    }

    public SecurityConfig corAllowedHeaders(List<String> allowedHeaders) {
        corsConfig.setAllowedHeaders(allowedHeaders);
        return this;
    }

    public SecurityConfig corsAllowCredentials(boolean allowCredentials) {
        corsConfig.setAllowCredentials(allowCredentials);
        return this;
    }

    public SecurityConfig setCsrfProtection(boolean csrfProtection) {
        coreSecurityConfig.setCsrfProtection(csrfProtection);
        return this;
    }

    public SecurityConfig addRouteAuthority(HashMap<String, SimpleGrantedAuthority> authorities){
        coreSecurityConfig.addAuthority(authorities);
        System.out.println("teoricamente adicionou" + authorities);
        return this;
    }

    public SecurityConfig configureJwtService(JwtConfig jwtConfig) {
        coreSecurityConfig.configureJwt(jwtConfig);
        return this;
    }

    public void build(){
        coreSecurityConfig.isCalled();
    }
}

