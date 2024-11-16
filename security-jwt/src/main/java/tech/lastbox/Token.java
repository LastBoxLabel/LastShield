package tech.lastbox;

import java.time.LocalDateTime;

/**
 * A record representing a JWT (JSON Web Token) with its essential details.
 * This record encapsulates the token string, subject, issued and expiration times, and its revocation status.
 * It is used for managing JWTs in the system.
 *
 * @param token The JWT string.
 * @param subject The subject of the token, typically the user associated with the token.
 * @param issuedAt The date and time when the token was issued.
 * @param expiresIn The date and time when the token will expire.
 * @param isRevoked A flag indicating whether the token has been revoked.
 */
public record Token(
        String token,
        String subject,
        LocalDateTime issuedAt,
        LocalDateTime expiresIn,
        boolean isRevoked
) {}
