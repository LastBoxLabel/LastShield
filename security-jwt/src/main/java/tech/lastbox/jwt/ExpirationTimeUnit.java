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

package tech.lastbox.jwt;

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
