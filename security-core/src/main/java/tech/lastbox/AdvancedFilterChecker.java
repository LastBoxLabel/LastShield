package tech.lastbox;

/**
 * Utility class to check and manage the state of advanced filtering.
 * <p>
 * This class is used to determinate if an authenticated route has been passed
 * and to manage the state of whether advanced filtering is active.
 * </p>
 */
class AdvancedFilterChecker {

    private static boolean isAdvancedFiltered;

    public static boolean isAdvancedFiltered() {
        return isAdvancedFiltered;
    }

    public static void setAdvancedFiltered(boolean isAdvancedFiltered) {
        AdvancedFilterChecker.isAdvancedFiltered = isAdvancedFiltered;
    }
}
