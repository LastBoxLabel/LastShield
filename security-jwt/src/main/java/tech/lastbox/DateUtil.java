package tech.lastbox;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

class DateUtil {
    public static LocalDateTime instantToLocalDateTime(Instant instant) {
        return instant.atZone(ZoneId.of("UTC")).toLocalDateTime();
    }

    public static Instant getExpirationDate(Instant timestamp, long amount, ExpirationTimeUnit expirationTimeUnit) {
        return timestamp.plus(expirationTimeUnit.toDuration(amount));
    }
}
