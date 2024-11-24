package tech.lastbox.security.core;

import java.util.ArrayList;
import java.util.List;

/**
 * @class
 * The {@code AdvancedFilterChecker} class provides utility methods to check
 * if advanced filtering is enabled and to manage a list of paths that should
 * not be filtered.
 * <p>
 * This utility class is used by {@link  CoreSecurityConfig} and {@link SecurityFilter} to manage route
 * authorizations. It allows checking whether a given path should be excluded from filtering
 * and provides a mechanism to add paths to a "do not filter" list.
 * </p>
 * @see CoreSecurityConfig
 * @see SecurityFilter
 */
class AdvancedFilterChecker {

    /**
     * A boolean attribute indicating if advanced filtering is enabled or not.
     */
    private static boolean isAdvancedFiltered;

    /**
     * A list of paths that should not be filtered.
     * Paths are stored in a normalized format without the wildcard "/**".
     */
    private final static List<String> shouldNotFilterPathList = new ArrayList<>();

    /**
     * Returns the current state of the advanced filter.
     *
     * @return {@code true} if advanced filtering is enabled, {@code false} otherwise.
     */
    public static boolean isAdvancedFiltered() {
        return isAdvancedFiltered;
    }

    /**
     * Checks if the given path should not be filtered by comparing it against
     * the list of paths that should not be filtered.
     * <p>
     * This method checks if the given path starts with any of the paths in the
     * {@code shouldNotFilterPathList}.
     * </p>
     *
     * @param pathToCompare the path to check.
     * @return {@code true} if the path matches any path in the shouldNotFilter list,
     *         {@code false} otherwise.
     */
    public static boolean isInShoudNotFilterList(String pathToCompare) {
        return shouldNotFilterPathList.stream()
                .anyMatch(pathToCompare::startsWith);
    }

    /**
     * Adds a path to the list of paths that should not be filtered.
     * <p>
     * The path is normalized by removing any wildcard suffix "/**" before adding it to the list.
     * </p>
     *
     * @param path the path to add to the list.
     */
    public static void addShoudNotFilterPath(String path) {
        shouldNotFilterPathList.add(path.replace("/**", ""));
    }

    /**
     * Sets the state of the advanced filter.
     *
     * @param isAdvancedFiltered {@code true} to enable advanced filtering,
     *                           {@code false} to disable it.
     */
    public static void setAdvancedFiltered(boolean isAdvancedFiltered) {
        AdvancedFilterChecker.isAdvancedFiltered = isAdvancedFiltered;
    }
}
