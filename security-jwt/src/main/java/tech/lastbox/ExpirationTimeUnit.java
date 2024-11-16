package tech.lastbox;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

/**
 * Enum representing various time units for expiration (Days, Hours, Minutes, Seconds).
 * This enum is used to define the unit of time that will be used for token expiration.
 * Each constant corresponds to a specific {@link ChronoUnit}.
 *
 * @see ChronoUnit
 */
public enum ExpirationTimeUnit {
    /** Represents the time unit in days. */
    DAYS(ChronoUnit.DAYS),

    /** Represents the time unit in hours. */
    HOURS(ChronoUnit.HOURS),

    /** Represents the time unit in minutes. */
    MINUTES(ChronoUnit.MINUTES),

    /** Represents the time unit in seconds. */
    SECONDS(ChronoUnit.SECONDS);

    private final ChronoUnit chronoUnit;

    /**
     * Constructor to initialize the {@link ExpirationTimeUnit} with its corresponding {@link ChronoUnit}.
     *
     * @param chronoUnit the {@link ChronoUnit} associated with the expiration time unit.
     */
    ExpirationTimeUnit(ChronoUnit chronoUnit) {
        this.chronoUnit = chronoUnit;
    }

    /**
     * Returns the {@link ChronoUnit} associated with this expiration time unit.
     *
     * @return the corresponding {@link ChronoUnit}.
     */
    public ChronoUnit getChronoUnit() {
        return chronoUnit;
    }

    /**
     * Converts the given amount of time to a {@link Duration} using this expiration time unit.
     *
     * @param amount the amount of time.
     * @return a {@link Duration} representing the specified amount of time.
     */
    public Duration toDuration(long amount) {
        return Duration.of(amount, chronoUnit);
    }
}
