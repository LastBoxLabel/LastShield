package tech.lastbox.security;

import org.springframework.stereotype.Component;
import tech.lastbox.CoreSecurityConfig;
import tech.lastbox.CorsConfig;
import tech.lastbox.RouteAuthority;
import tech.lastbox.http.HttpMethod;

import java.util.HashSet;
import java.util.List;

@Component
public class SecurityConfig {
    private final CorsConfig corsConfig;
    private final CoreSecurityConfig coreSecurityConfig;

    public SecurityConfig(CoreSecurityConfig coreSecurityConfig, CorsConfig corsConfig) {
        this.coreSecurityConfig = coreSecurityConfig;
        this.corsConfig = corsConfig;
    }

    public SecurityConfig corsAllowedOrigins(String allowedOrigin) {
        corsConfig.setAllowedOrigins(allowedOrigin);
        return this;
    }

    public SecurityConfig corsAllowedOrigins(List<String> allowedOrigins) {
        corsConfig.setAllowedOrigins(allowedOrigins);
        return this;
    }

    public SecurityConfig corsAllowMethods(String allowedMethod) {
        corsConfig.setAllowedMethods(allowedMethod);
        return this;
    }

    public SecurityConfig corsAllowedMethods(List<String> allowedMethods) {
        corsConfig.setAllowedMethods(allowedMethods);
        return this;
    }

    public SecurityConfig corsAllowedHeaders(String allowedHeader) {
        corsConfig.setAllowedHeaders(allowedHeader);
        return this;
    }

    public SecurityConfig corsAllowedHeaders(List<String> allowedHeaders) {
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

    public SecurityConfig addRouteAuthority(String path){
        coreSecurityConfig.addAuthority(new RouteAuthority(path));
        return this;
    }

    public SecurityConfig addRouteAuthority(String path, HttpMethod httpMethod){
        coreSecurityConfig.addAuthority(new RouteAuthority(path, httpMethod));
        return this;
    }

    public SecurityConfig addRouteAuthority(String path, HashSet<HttpMethod> httpMethods){
        httpMethods.forEach(httpMethod -> coreSecurityConfig.addAuthority(new RouteAuthority(path, httpMethod)));
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

    public SecurityConfig addRouteAuthority(String path, String role, HttpMethod httpMethod){
        coreSecurityConfig.addAuthority(new RouteAuthority(path, role, httpMethod));
        return this;
    }

    public SecurityConfig addRouteAuthority(String path, String role, List<HttpMethod> httpMethods){
        httpMethods.forEach(httpMethod -> coreSecurityConfig.addAuthority(new RouteAuthority(path, role, httpMethod)));
        return this;
    }

    public void build(){
        coreSecurityConfig.isCalled();
    }
}

