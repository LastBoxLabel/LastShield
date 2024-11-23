package tech.lastbox;

import tech.lastbox.http.HttpMethod;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RouteAuthority {
    private final String path;
    private final String[] roles;
    private final List<HttpMethod> httpMethods = new ArrayList<>();

    public RouteAuthority(String path) {
        this.path = path;
        this.roles = null;
    }

    public RouteAuthority(String path, String role) {
        this.path = path;
        this.roles = new String[] {role};
    }

    public RouteAuthority(String path, String role, HttpMethod... httpMethods) {
        this.path = path;
        this.roles = new String[] {role};
        this.httpMethods.addAll(Arrays.asList(httpMethods));
    }

    public RouteAuthority(String path, List<String> roles) {
        this.path = path;
        this.roles = roles.toArray(roles.toArray(new String[0]));
    }

    public RouteAuthority(String path, HttpMethod... httpMethods) {
        this.path = path;
        this.roles = null;
        this.httpMethods.addAll(Arrays.asList(httpMethods));
    }

    public String getPath() {
        return path;
    }

    public String[] getRoles() {
        return roles;
    }

    public List<HttpMethod> getHttpMethods() {return httpMethods;}

    @Override
    public String toString() {
        return "RouteAuthority{" +
                "path='" + path + '\'' +
                ", roles=" + Arrays.toString(roles) +
                ", httpMethods=" + httpMethods +
                '}';
    }
}
