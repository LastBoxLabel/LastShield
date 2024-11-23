package tech.lastbox;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.CorsConfigurer;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
public class CorsConfig {
    private List<String> allowedOrigins;
    private List<String> allowedMethods;
    private List<String> allowedHeaders;
    private Boolean allowCredentials;
    private final Logger logger = LoggerFactory.getLogger(CorsConfig.class);

    @Bean
    public Customizer<CorsConfigurer<HttpSecurity>> configure(){
        return cors -> corsConfigurationSource();
    }

    private void corsConfigurationSource() {
        logger.info("✅ CORS Configuration Initialized");
        CorsConfiguration configuration = new CorsConfiguration();

        if(allowedOrigins == null) configuration.setAllowedOrigins(List.of("*"));
        if(allowedMethods == null) configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        if(allowedHeaders == null) configuration.setAllowedHeaders(List.of("*"));
        if(allowCredentials == null) configuration.setAllowCredentials(true);

        configuration.setAllowedOrigins(allowedOrigins);
        configuration.setAllowedMethods(allowedMethods);
        configuration.setAllowCredentials(allowCredentials);
        configuration.addAllowedHeader("*");

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
    }

    public void setAllowedOrigins(String origin) {this.allowedOrigins.add(origin);}

    public void setAllowedOrigins(List<String> allowedOrigins) {
        this.allowedOrigins = allowedOrigins;
    }

    public void setAllowedMethods(String method) {this.allowedMethods.add(method);}

    public void setAllowedMethods(List<String> allowedMethods) {
        this.allowedMethods = allowedMethods;
    }

    public void setAllowedHeaders(String header) {this.allowedHeaders.add(header);}

    public void setAllowedHeaders(List<String> allowedHeaders) {
        this.allowedHeaders = allowedHeaders;
    }

    public void setAllowCredentials(Boolean allowCredentials) {
        this.allowCredentials = allowCredentials;
    }
}
