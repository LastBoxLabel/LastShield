package tech.lastbox.security.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import tech.lastbox.security.core.annotations.UserHandler;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class SecurityUtil {
    private final Logger logger = LoggerFactory.getLogger(SecurityUtil.class);

    public Class<?> getUserRepositoryClass() {
        if (AdvancedFilterChecker.isAdvancedFiltered()) {
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

            for (ClassLoader cl = classLoader; cl != null; cl = cl.getParent()) {
                for (Class<?> cls : getAllClassesFromClassLoader(cl)) {
                    if (cls.isAnnotationPresent(UserHandler.class)) {
                        try {
                            return cls;
                        } catch (Exception e) {
                            throw new RuntimeException("Error instantiating UserService", e);
                        }
                    }
                }
            }

            throw new RuntimeException("No UserHandler found");
        } else {
            return null;
        }
    }

    public List<GrantedAuthority> getUserAuthorities(Object user) {
        return convertRolesToAuthorities(getUserRoles(user));
    }

    public Object findUserByUsername(Object userService,  String username) {
        try {
            Method method = userService.getClass().getDeclaredMethod("findUserByUsername", String.class);
            method.setAccessible(true);
            return method.invoke(userService, username);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("The method 'getUserByUsername' was not found in the provided class.", e);
        } catch (Exception e) {
            throw new RuntimeException("Error invoking 'getUserByUsername' on the provided class.", e);
        }
    }

    private List<Class<?>> getAllClassesFromClassLoader(ClassLoader classLoader) {
        List<Class<?>> classes = new ArrayList<>();
        try {
            Enumeration<URL> resources = classLoader.getResources("");
            while (resources.hasMoreElements()) {
                URL resource = resources.nextElement();
                if (resource.getProtocol().equals("file")) {
                    classes.addAll(findClassesInDirectory(new File(resource.toURI()), ""));
                }
            }
        } catch (Exception e) {
            logger.error("Error: {}", e, e);
        }
        return classes;
    }

    private List<Class<?>> findClassesInDirectory(File directory, String packageName) {
        List<Class<?>> classes = new ArrayList<>();
        if (!directory.exists()) {
            return classes;
        }

        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    classes.addAll(findClassesInDirectory(file, packageName + file.getName() + "."));
                } else if (file.getName().endsWith(".class")) {
                    String className = packageName + file.getName().substring(0, file.getName().length() - 6);
                    try {
                        classes.add(Class.forName(className));
                    } catch (ClassNotFoundException e) {
                        logger.error("Error: {}", e, e);
                    }
                }
            }
        }
        return classes;
    }

    @SuppressWarnings("unchecked")
    private  <T> T getFieldValue(Object object, String fieldName) {
        try {
            Field field = object.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            Object value = field.get(object);
            return (T) field.get(object);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("Error accessing the field '" + fieldName + "' in "
                    + object.getClass().getName(), e);
        } catch (ClassCastException e) {
            throw new RuntimeException("The field '" + fieldName + "' is not of the expected type.", e);
        }
    }

    private List<String> getUserRoles(Object user) {
        try {
            return getFieldValue(user, "roles");
        } catch (RuntimeException e) {
            if (e.getCause() instanceof NoSuchFieldException) {
                try {
                    String singleRole = getFieldValue(user, "role");
                    return List.of(singleRole);
                } catch (RuntimeException ex) {
                    throw new RuntimeException("Failed to retrieve roles or role field", ex);
                }
            } else {
                throw e;
            }
        }
    }

    private List<GrantedAuthority> convertRolesToAuthorities(List<String> roles) {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(treatRole(role)))
                .collect(Collectors.toList());
    }

    private String treatRole(String role) {
        if (role.startsWith("ROLE_")) return role;
        else return String.format("ROLE_%s", role);
    }
}

