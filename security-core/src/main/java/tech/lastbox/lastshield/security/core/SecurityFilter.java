package tech.lastbox.lastshield.security.core;

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

/**
 * A custom security filter that intercepts HTTP requests and performs token validation
 * to authenticate users based on a JWT (JSON Web Token). This filter checks whether a token
 * is provided in the "Authorization" header of the request, validates the token, and sets the
 * authentication context for the request if the token is valid.
 * <p>
 * The filter checks for token validity only if the application is configured to perform
 * advanced filtering (as indicated by the {@link AdvancedFilterChecker}). If advanced filtering
 * is enabled, it processes the token, validates it using the {@link JwtService}, retrieves the
 * associated user from the repository, and sets the authenticated user in the {@link SecurityContextHolder}.
 * </p>
 */
@Component
public class SecurityFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private Object userRepository;
    private final ApplicationContext applicationContext;
    private final Logger logger = LoggerFactory.getLogger(SecurityFilter.class);
    private final SecurityUtil securityUtil;

    /**
     * Constructs a new {@code SecurityFilter}.
     * <p>
     * This constructor initializes the filter with the required dependencies:
     * <ul>
     *     <li>{@link JwtService} for validating JWT tokens.</li>
     *     <li>{@link ApplicationContext} for accessing Spring beans, such as the user repository.</li>
     *     <li>{@link SecurityUtil} for handling user lookups and authority retrieval.</li>
     * </ul>
     * </p>
     *
     * @param jwtService the JWT service used to validate and decode tokens.
     * @param applicationContext the Spring application context to fetch the user repository bean.
     * @param securityUtil utility class for performing security-related operations like user lookup and authority retrieval.
     */
    public SecurityFilter(JwtService jwtService, ApplicationContext applicationContext, SecurityUtil securityUtil) {
        this.jwtService = jwtService;
        this.applicationContext = applicationContext;
        this.securityUtil = securityUtil;
    }

    /**
     * Sets the user repository class to be used for user lookups during authentication.
     * The repository bean is retrieved from the Spring application context.
     *
     * @param userRepositoryClass the class type of the user repository.
     */
    public void setUserRepository(Class<?> userRepositoryClass) {
        this.userRepository = applicationContext.getBean(userRepositoryClass);
    }

    /**
     * Filters HTTP requests by validating JWT tokens and setting the authentication context.
     * <p>
     * If the token is valid, it retrieves the associated user from the repository and sets
     * the user as the authenticated principal in the security context.
     * </p>
     * <p>
     * If the token is missing or invalid, the filter sends an appropriate error response with the status:
     * <ul>
     *     <li>401 Unauthorized if the token is missing or invalid.</li>
     *     <li>403 Forbidden if the user cannot be found or the token is invalid.</li>
     * </ul>
     * </p>
     *
     * @param request the HTTP request to be filtered.
     * @param response the HTTP response to be sent back to the client.
     * @param filterChain the filter chain to pass the request and response to the next filter.
     * @throws ServletException if an error occurs during filter processing.
     * @throws IOException if an I/O error occurs during the filter process.
     */
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


    /**
     * Determines if this filter should be applied to the given HTTP request.
     * <p>
     * This method checks if the request path is in the list of paths that should not be filtered.
     * </p>
     *
     * @param request the HTTP request.
     * @return {@code true} if the request should not be filtered; {@code false} otherwise.
     */
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();
        if (AdvancedFilterChecker.isAdvancedFiltered()) {
            return AdvancedFilterChecker.isInShoudNotFilterList(path);
        } else {
            return true;
        }
    }

    /**
     * Recovers the JWT token from the "Authorization" header of the HTTP request.
     * <p>
     * The token is expected to be prefixed with "Bearer ". This method strips off the "Bearer " prefix
     * and returns the token value.
     * </p>
     *
     * @param request the HTTP request.
     * @return the JWT token if present, or {@code null} if the token is missing.
     */
    private String recoverToken(HttpServletRequest request) {
        var authHeader = request.getHeader("Authorization");
        if (authHeader == null) return null;
        return authHeader.replace("Bearer ", "");
    }
}
