package tech.lastbox.security.core;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import tech.lastbox.jwt.JwtService;

import java.io.IOException;
import java.util.Optional;


@Component
public class SecurityFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private Object userRepository;
    private final ApplicationContext applicationContext;
    private final Logger logger = LoggerFactory.getLogger(SecurityFilter.class);
    private final SecurityUtil securityUtil;

    public SecurityFilter(JwtService jwtService, ApplicationContext applicationContext, SecurityUtil securityUtil) {
        this.jwtService = jwtService;
        this.applicationContext = applicationContext;
        this.securityUtil = securityUtil;
    }

    public void setUserRepository(Class<?> userRepositoryClass) {
        this.userRepository = applicationContext.getBean(userRepositoryClass);
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            if (AdvancedFilterChecker.isAdvancedFiltered()) {
                var authorization = this.recoverToken(request);
                if (authorization == null || authorization.isEmpty()) {
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token is missing");
                    return;
                }

                var tokenValidation = jwtService.validateToken(authorization);
                var tokenOptional = tokenValidation.tokenOptional();
                if (tokenOptional.isPresent() && tokenValidation.isValid()) {
                    var token = tokenOptional.get();
                    Optional<Object> userEntityOptional = (Optional<Object>) securityUtil.findUserByUsername(userRepository, token.subject());
                    if (userEntityOptional.isEmpty()) {
                        response.sendError(HttpServletResponse.SC_FORBIDDEN, "User not found");
                        return;
                    }

                    Object userEntity = userEntityOptional.get();
                    var authorities = securityUtil.getUserAuthorities(userEntity);
                    System.out.println(authorities);
                    var authentication = new UsernamePasswordAuthenticationToken(userEntity, null, authorities);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                } else {
                    response.sendError(HttpServletResponse.SC_FORBIDDEN, "Invalid token");
                }
            }
        } catch (RuntimeException e) {
            logger.error("Exception in SecurityFilter: {}", e.getMessage(), e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Unexpected error");
        }
        filterChain.doFilter(request, response);
    }


    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();
        if (AdvancedFilterChecker.isAdvancedFiltered()) {
            return AdvancedFilterChecker.isInShoudNotFilterList(path);
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
