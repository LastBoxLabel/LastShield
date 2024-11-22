package tech.lastbox;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;


@Configuration
@EnableWebSecurity
public class SecurityFilter extends OncePerRequestFilter {
    private final JwtService jwtService = JwtServiceConfig.getJwtService();
    private Object userService;
    private final Logger logger = LoggerFactory.getLogger(SecurityFilter.class);

    public void setUserService(Object userService) {
        this.userService = userService;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) {
        if (AdvancedFilterChecker.isAdvancedFiltered()) {
            var token = this.recoverToken(request);


            if (token == null || token.isEmpty()) throw new RuntimeException("Token is empty");
            if (jwtService == null) throw new RuntimeException("JwtService not configured");

            var login = jwtService.validateToken(token).tokenOptional();
            if (login.isPresent()) {
                try {
                    Method method = userService.getClass().getDeclaredMethod("getUserByUsername", String.class);
                    Object userEntity = method.invoke(userService, login.get().subject());
                    var authorities = Collections.singletonList(new SimpleGrantedAuthority(userEntity.getClass().getDeclaredField("role").get(userEntity).toString()));
                    var authentication = new UsernamePasswordAuthenticationToken(userEntity, null, authorities);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                } catch (NoSuchMethodException | NoSuchFieldException | IllegalAccessException |
                         InvocationTargetException e) {
                    logger.error("Error while authenticating: {}", e, e);
                }
            }
        }
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();

        if (AdvancedFilterChecker.isAdvancedFiltered()) {
            return path.startsWith("/login") ||
                    path.startsWith("/register");
        } else {
            return true;
        }
    }

    private String recoverToken(HttpServletRequest request) {
        var authHeader = request.getHeader("Authorization");
        if (authHeader == null) return null;
        return authHeader.replace("Bearer ", "");
    }
}
