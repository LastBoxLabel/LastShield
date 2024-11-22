package tech.lastbox;

class AdvancedFilterChecker {
    private static boolean isAdvancedFiltered;
    public static boolean isAdvancedFiltered() {
        return isAdvancedFiltered;
    }

    public static void setAdvancedFiltered(boolean isAdvancedFiltered) {
        AdvancedFilterChecker.isAdvancedFiltered = isAdvancedFiltered;
    }
}
