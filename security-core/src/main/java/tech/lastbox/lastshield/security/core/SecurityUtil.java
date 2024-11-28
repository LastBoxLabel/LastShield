/*
 * Copyright 2024 LastBox
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package tech.lastbox.lastshield.security.core;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.Entity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import tech.lastbox.lastshield.security.core.annotations.UserHandler;
import tech.lastbox.lastshield.security.core.annotations.Username;

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
 */
@Component
public class SecurityUtil {
    private final Logger logger = LoggerFactory.getLogger(SecurityUtil.class);
    private final Set<String> usernameFields = new HashSet<>();

    /**
     * Default constructor for the SecurityUtil class.
     * This constructor is used to instantiate the SecurityUtil utility class.
     */
    public SecurityUtil() {
    }

    /**
     * This method is executed after the bean has been initialized to retrieve and register all fields annotated
     * with {@link Username} from entities within the classpath.
     * <p>
     * It scans all classes loaded by the current class loader, identifies those annotated with {@link Entity},
     * and registers the field names that are annotated with {@link Username} for later use.
     * <p>
     * If an error occurs during the process, an error message will be logged.
     * <p>
     * If the system is set to authenticate by roles and there's no field with @Username annotation the application will not start.
     */
    @PostConstruct
    public void getUsernameFields() {
        try {
            List<Class<?>> allClasses = getAllClassesFromClassLoader(Thread.currentThread().getContextClassLoader());
            for (Class<?> clazz : allClasses) {
                if (clazz.isAnnotationPresent(Entity.class)) registerUsernameFields(clazz);
            }
        } catch (Exception e) {
            logger.error("Error during application startup: {}", e.getMessage(), e);
        }
        if (AdvancedFilterChecker.isAdvancedFiltered() && usernameFields.isEmpty()) {
            logger.error("There's no field with @Username annotation, so the authentication can't run properly. So consider to changing to no auth or creating a field with this annotation.");
            throw new RuntimeException("There's no field with @Username annotation, so the authentication can't run properly.");
        }
    }

    /**
     * Registers the field names of a class annotated with {@link Username} into the {@link #usernameFields} set.
     * This method is called for each class annotated with {@link Entity} during the classpath scan.
     * <p>
     * The method inspects each field in the given class, checking if the field is annotated with {@link Username}.
     * If it is, the field name is added to the {@link #usernameFields} set.
     *
     * @param clazz the class to inspect for fields annotated with {@link Username}.
     */
    private void registerUsernameFields(Class<?> clazz) {
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(Username.class)) {
                System.out.println(field.getName());
                usernameFields.add(field.getName());
            }
        }
    }


    /**
     * Capitalizes the first letter of a given string, leaving the rest of the string unchanged.
     * <p>
     * This method is used to format the field name by ensuring that only the first character is uppercase,
     * which is typically needed when dynamically calling methods like "findUserByUsername" or similar.
     *
     * @param fieldName the name of the field to modify.
     * @return the modified string with the first letter capitalized, or the original string if it is null or empty.
     */
    private String capitalizeFirstLetter(String fieldName) {
        if (fieldName == null || fieldName.isEmpty()) {
            return fieldName;
        }
        return fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
    }


    /**
     * Retrieves the class of the user repository annotated with {@link UserHandler}.
     * This method performs classpath scanning to find classes annotated with {@link UserHandler} and returns
     * the corresponding class for the user repository. The class is expected to have a method to retrieve
     * users by their username.
     * <p>
     * If advanced filtering is enabled (via {@link AdvancedFilterChecker}), it will scan the classpath for
     * the appropriate class and return it. If no such class is found, it throws an exception.
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
            for (String field : usernameFields) {
                System.out.println(field);
                try {
                    String fieldToSearch = String.format("findUserBy%s", capitalizeFirstLetter(field));
                    System.out.println(fieldToSearch);
                    Method method = userService.getClass().getDeclaredMethod(fieldToSearch, String.class);
                    method.setAccessible(true);
                    return method.invoke(userService, username);
                } catch (Exception ignored) {}
            }
            throw new NoSuchMethodException();
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("The method 'findUserBy' was not found in the provided class for any username fields.", e);
        } catch (Exception e) {
            throw new RuntimeException("Error invoking 'findUserBy' on the provided class.", e);
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

