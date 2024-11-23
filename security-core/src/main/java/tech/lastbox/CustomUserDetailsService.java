package tech.lastbox;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import tech.lastbox.annotations.Password;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final SecurityUtil securityUtil;

    public CustomUserDetailsService(SecurityUtil securityUtil) {
        this.securityUtil = securityUtil;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            Object userService = securityUtil.getUserServiceInstance();
            Method getUserByUsername = userService.getClass()
                    .getDeclaredMethod("getUserByUsername", String.class);

            getUserByUsername.setAccessible(true);
            Object userEntity = getUserByUsername.invoke(userService, username);
            String password = Arrays.stream(userEntity.getClass().getDeclaredFields())
                    .filter(field -> field.isAnnotationPresent(Password.class))
                    .map(field -> {
                        try {
                            field.setAccessible(true);
                            return (String) field.get(userEntity);
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }).findFirst().orElseThrow(() -> new IllegalStateException("No field with @Password annotation found"));

            return new User(username, password, new ArrayList<>());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
