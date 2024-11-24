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

/**
 * Utility class for various security-related operations, such as retrieving user roles and authorities,
 * locating user repositories, and interacting with user entities via reflection.
 * <p>
 * This class provides methods to:
 * <ul>
 *     <li>Obtain the user repository class annotated with {@link UserHandler}.</li>
 *     <li>Find users by username using reflection to invoke repository methods.</li>
 *     <li>Retrieve user roles and convert them to granted authorities for Spring Security.</li>
 *     <li>Handle class loading and reflection for user repository discovery and user entity interaction.</li>
 * </ul>
 * </p>
 */
@Component
public class SecurityUtil {
    private final Logger logger = LoggerFactory.getLogger(SecurityUtil.class);

    /**
     * Retrieves the class of the user repository annotated with {@link UserHandler}.
     * This method performs classpath scanning to find classes annotated with {@link UserHandler} and returns
     * the corresponding class for the user repository. The class is expected to have a method to retrieve
     * users by their username.
     * <p>
     * If advanced filtering is enabled (via {@link AdvancedFilterChecker}), it will scan the classpath for
     * the appropriate class and return it. If no such class is found, it throws an exception.
     * </p>
     *
     * @return the user repository class annotated with {@link UserHandler}.
     * @throws RuntimeException if no user handler class is found or if an error occurs during class loading.
     */
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

    /**
     * Converts the user's roles to a list of {@link GrantedAuthority} objects.
     * This method is used to convert the user's roles (usually from a field like "roles" or "role") into a list
     * of {@link GrantedAuthority}, which Spring Security uses to manage access control.
     *
     * @param user the user object from which roles will be extracted.
     * @return a list of {@link GrantedAuthority} representing the user's roles.
     */
    public List<GrantedAuthority> getUserAuthorities(Object user) {
        return convertRolesToAuthorities(getUserRoles(user));
    }

    /**
     * Finds a user by their username by invoking the "findUserByUsername" method on the provided user repository.
     * The method uses reflection to dynamically invoke the appropriate method on the user repository to retrieve
     * the user associated with the given username.
     *
     * @param userService the user repository or service to search for the user.
     * @param username the username of the user to search for.
     * @return the user entity associated with the given username.
     * @throws RuntimeException if the "findUserByUsername" method is not found or if an error occurs during invocation.
     */
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

    /**
     * Retrieves all classes loaded by a specific class loader. This method scans the resources available
     * in the given class loader and attempts to find all the class files.
     *
     * @param classLoader the class loader to scan for classes.
     * @return a list of {@link Class} objects found in the class loader.
     */
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

    /**
     * Recursively searches a directory for class files and adds them to the class list.
     * This method is used to find all classes in the classpath.
     *
     * @param directory the directory to search for classes.
     * @param packageName the base package name used to form class names.
     * @return a list of {@link Class} objects found in the directory.
     */
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

    /**
     * Retrieves the value of a field using reflection. This method is used to access private fields in user objects,
     * such as "roles" or "role", and return their value.
     *
     * @param object the object from which the field value will be extracted.
     * @param fieldName the name of the field to retrieve.
     * @param <T> the expected type of the field value.
     * @return the value of the specified field.
     * @throws RuntimeException if the field is not found or cannot be accessed.
     */
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

    /**
     * Retrieves the roles of a user by checking the "roles" or "role" field. This method handles both
     * cases where the user has a single role (stored in a field named "role") or multiple roles (stored in a
     * field named "roles").
     *
     * @param user the user entity from which roles will be extracted.
     * @return a list of role names associated with the user.
     * @throws RuntimeException if neither "roles" nor "role" fields can be found.
     */
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

    /**
     * Converts a list of role names into a list of {@link GrantedAuthority} objects.
     * This is used to convert roles (e.g., "ADMIN", "USER") into {@link SimpleGrantedAuthority} objects
     * that Spring Security understands.
     *
     * @param roles a list of role names.
     * @return a list of {@link GrantedAuthority} objects corresponding to the roles.
     */
    private List<GrantedAuthority> convertRolesToAuthorities(List<String> roles) {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(treatRole(role)))
                .collect(Collectors.toList());
    }

    /**
     * Ensures that the role name starts with "ROLE_". If the role name does not start with this prefix,
     * it is added automatically to conform to Spring Security's requirements.
     *
     * @param role the role name to treat.
     * @return the role name prefixed with "ROLE_" if necessary.
     */
    private String treatRole(String role) {
        if (role.startsWith("ROLE_")) return role;
        else return String.format("ROLE_%s", role);
    }
}

