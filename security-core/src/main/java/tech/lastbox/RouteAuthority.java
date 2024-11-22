package tech.lastbox;

import java.util.Arrays;
import java.util.List;

public class RouteAuthority {
    private final String path;
    private final String[] roles;

    public RouteAuthority(String path, String role) {
        this.path = path;
        this.roles = new String[] {role};
    }

    public RouteAuthority(String path, List<String> roles) {
        this.path = path;
        this.roles = roles.toArray(roles.toArray(new String[0]));
    }

    public String getPath() {
        return path;
    }

    public String[] getRoles() {
        return roles;
    }

    @Override
    public String toString() {
        return "RouteAuthority{" +
                "path='" + path + '\'' +
                ", roles=" + Arrays.toString(roles) +
                '}';
    }
}
