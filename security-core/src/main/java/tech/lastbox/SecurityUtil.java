package tech.lastbox;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import tech.lastbox.annotations.UserServiceImplementation;

import java.io.File;
import java.net.URL;
import java.util.*;


@Component
public class SecurityUtil {
    private final Logger logger = LoggerFactory.getLogger(SecurityUtil.class);

    public Object getUserServiceInstance() {
        if (AdvancedFilterChecker.isAdvancedFiltered()) {
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

            for (ClassLoader cl = classLoader; cl != null; cl = cl.getParent()) {
                for (Class<?> cls : getAllClassesFromClassLoader(cl)) {
                    if (cls.isAnnotationPresent(UserServiceImplementation.class)) {
                        try {
                            return cls.getDeclaredConstructor().newInstance();
                        } catch (Exception e) {
                            throw new RuntimeException("Error instantiating UserService", e);
                        }
                    }
                }
            }

            throw new RuntimeException("No UserServiceImplementation found");
        } else {
            return new Object();
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
}
