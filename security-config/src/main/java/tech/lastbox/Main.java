package tech.lastbox;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.HashMap;
import java.util.List;

@EnableWebSecurity
@SpringBootApplication
public class Main{
    public static void main(String[] args) {

        ApplicationContext applicationContext = SpringApplication.run(Main.class, args);
        SecurityConfig securityConfig = applicationContext.getBean(SecurityConfig.class);

        securityConfig.corsAllowCredentials(true)
                      .corsAllowedMethods(List.of("GET"))
                      .corsAllowedOrigins(List.of("*"))
                      .addRouteAuthority(new HashMap<String, SimpleGrantedAuthority>() {{
                            put("/**", new SimpleGrantedAuthority("QUANDO EU QUISER"));
                        }})
                      .configureJwtService(new JwtConfig(JwtAlgorithm.HMAC256,
                                                        "pindamonhangaba",
                                                        "user",
                                                        2,
                                                        ExpirationTimeUnit.DAYS))
                      .setCsrfProtection(true)
                      .build();
    }
}
