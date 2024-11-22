package tech.lastbox;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class SecurityConfig {
    private final CorsConfig corsConfig;
    private final CoreSecurityConfig coreSecurityConfig;

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

    public SecurityConfig addRouteAuthority(String path, String role){
        coreSecurityConfig.addAuthority(new RouteAuthority(path, role));
        return this;
    }

    public SecurityConfig addRouteAuthority(String path, List<String> roles){
        coreSecurityConfig.addAuthority(new RouteAuthority(path, roles));
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

