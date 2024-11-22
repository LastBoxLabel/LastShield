package tech.lastbox;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
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
    private JwtService jwtService;
    private final SecurityUtil securityUtil;
    private final Object userService;
    private final Logger logger = LoggerFactory.getLogger(SecurityFilter.class);

    public SecurityFilter(SecurityUtil securityUtil) throws Exception {
        this.securityUtil = securityUtil;
        this.userService = securityUtil.getUserServiceInstance();
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) {
        if (AdvancedFilterChecker.isAdvancedFiltered()) {
            var token = this.recoverToken(request);


            if (token == null || token.isEmpty()) throw new RuntimeException("token is empty");
            if (jwtService == null) throw new RuntimeException("JwtService not configured");

            var login = jwtService.getToken(token);
            if (login.isPresent()) {
                try {
                    Method method = userService.getClass().getDeclaredMethod("getUserByUsername", String.class);
                    Object userEntity = method.invoke(userService, login.get().issuer());
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

    public void configureJwtService(JwtConfig jwtConfig) {
        this.jwtService = new JwtService(jwtConfig);
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
