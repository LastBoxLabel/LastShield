package tech.lastbox.security.core;

import org.springframework.context.ApplicationContext;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import tech.lastbox.security.core.annotations.Password;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final SecurityUtil securityUtil;
    private final ApplicationContext applicationContext;
    private Object userRepository;

    public CustomUserDetailsService(SecurityUtil securityUtil, ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
        this.securityUtil = securityUtil;
    }

    private void setUserRepository() {
        this.userRepository = applicationContext.getBean(securityUtil.getUserRepositoryClass());
    }

    @Override
    @SuppressWarnings("unchecked")
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            if (this.userRepository == null) setUserRepository();

            Optional<Object> userEntityOptional = (Optional<Object>) securityUtil.findUserByUsername(userRepository, username);
            if (userEntityOptional.isEmpty()) throw new RuntimeException("User not found.");
            Object userEntity = userEntityOptional.get();
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
