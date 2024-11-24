package tech.lastbox.security.core;

import tech.lastbox.security.core.http.HttpMethod;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Represents the authorization configuration for a specific route within the application.
 * <p>
 * A {@code RouteAuthority} defines the access control for a specific path, including the roles required to access it
 * and the HTTP methods (GET, POST, etc.) allowed on that path. This class is used to specify the security constraints
 * for each route in the application's security configuration.
 * </p>
 */
public class RouteAuthority {
    private final String path;
    private final String[] roles;
    private final List<HttpMethod> httpMethods = new ArrayList<>();

    /**
     * Constructs a {@code RouteAuthority} with the specified path and no roles.
     * The route can be accessed with any role or no role, depending on additional configuration.
     *
     * @param path the path of the route to which this authority applies.
     */
    public RouteAuthority(String path) {
        this.path = path;
        this.roles = null;
    }

    /**
     * Constructs a {@code RouteAuthority} with the specified path and a single role.
     *
     * @param path the path of the route to which this authority applies.
     * @param role the role that is required to access this route.
     */
    public RouteAuthority(String path, String role) {
        this.path = path;
        this.roles = new String[] {role};
    }

    /**
     * Constructs a {@code RouteAuthority} with the specified path, role, and HTTP methods.
     *
     * @param path the path of the route to which this authority applies.
     * @param role the role that is required to access this route.
     * @param httpMethods the HTTP methods (e.g., GET, POST) allowed on this route.
     */
    public RouteAuthority(String path, String role, HttpMethod... httpMethods) {
        this.path = path;
        this.roles = new String[] {role};
        this.httpMethods.addAll(Arrays.asList(httpMethods));
    }

    /**
     * Constructs a {@code RouteAuthority} with the specified path and list of roles.
     *
     * @param path the path of the route to which this authority applies.
     * @param roles the list of roles that are allowed to access this route.
     */
    public RouteAuthority(String path, List<String> roles) {
        this.path = path;
        this.roles = roles.toArray(roles.toArray(new String[0]));
    }

    /**
     * Constructs a {@code RouteAuthority} with the specified path and HTTP methods.
     *
     * @param path the path of the route to which this authority applies.
     * @param httpMethods the HTTP methods (e.g., GET, POST) allowed on this route.
     */
    public RouteAuthority(String path, HttpMethod... httpMethods) {
        this.path = path;
        this.roles = null;
        this.httpMethods.addAll(Arrays.asList(httpMethods));
    }

    /**
     * Gets the path associated with this {@code RouteAuthority}.
     *
     * @return the path for this route.
     */
    public String getPath() {
        return path;
    }

    /**
     * Gets the roles associated with this {@code RouteAuthority}.
     *
     * @return the roles that are allowed to access this route.
     */
    public String[] getRoles() {
        return roles;
    }

    /**
     * Gets the HTTP methods allowed on this route.
     *
     * @return a list of allowed HTTP methods (GET, POST, etc.).
     */
    public List<HttpMethod> getHttpMethods() {return httpMethods;}

    /**
     * Returns a string representation of the {@code RouteAuthority}.
     * This includes the path, roles, and HTTP methods associated with the route.
     *
     * @return a string representing the {@code RouteAuthority}.
     */
    @Override
    public String toString() {
        return "RouteAuthority{" +
                "path='" + path + '\'' +
                ", roles=" + Arrays.toString(roles) +
                ", httpMethods=" + httpMethods +
                '}';
    }
}
