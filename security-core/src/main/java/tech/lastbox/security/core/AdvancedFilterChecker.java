package tech.lastbox.security.core;

import java.util.ArrayList;
import java.util.List;

/**
 * Utility class to check and manage the state of advanced filtering.
 * <p>
 * This class is used to determinate if an authenticated route has been passed
 * and to manage the state of whether advanced filtering is active.
 * </p>
 */
class AdvancedFilterChecker {

    private static boolean isAdvancedFiltered;

    private final static List<String> shoudNotFilterPathList = new ArrayList<>();

    public static boolean isAdvancedFiltered() {
        return isAdvancedFiltered;
    }

    public static boolean isInShoudNotFilterList(String pathToCompare) {
        if (shoudNotFilterPathList.isEmpty()) {
            return false;
        }

        return shoudNotFilterPathList.stream()
                .filter(path -> path != null && !path.trim().isEmpty())
                .anyMatch(path -> pathToCompare.startsWith(path.trim()));
    }

    public static void addShoudNotFilterPath(String path) {
        shoudNotFilterPathList.add(path.replace("/**", ""));
    }

    public static void setAdvancedFiltered(boolean isAdvancedFiltered) {
        AdvancedFilterChecker.isAdvancedFiltered = isAdvancedFiltered;
    }
}
