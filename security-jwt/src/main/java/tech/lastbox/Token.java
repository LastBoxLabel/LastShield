package tech.lastbox;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Represents a JWT (JSON Web Token) in the system.
 * This record encapsulates information related to the token,
 * including its value, subject, issue and expiration times, issuer, scope,
 * and revocation status.
 *
 * <p>
 * This class is typically used for transferring JWT-related data
 * and is ideal for scenarios where immutable data structures are required.
 * It encapsulates the following fields:
 * </p>
 *
 * <ul>
 *     <li>{@code token}: The string representation of the token itself.</li>
 *     <li>{@code subject}: The subject associated with the token (typically the user or entity).</li>
 *     <li>{@code issuedAt}: The timestamp representing when the token was issued.</li>
 *     <li>{@code expiresIn}: The timestamp representing when the token will expire.</li>
 *     <li>{@code issuer}: The entity that issued the token.</li>
 *     <li>{@code scope}: A list of string values representing the scopes/permissions granted by the token.</li>
 *     <li>{@code isRevoked}: A boolean flag indicating whether the token has been revoked.</li>
 * </ul>
 *
 * <p>
 * This record is designed for use in scenarios where tokens are immutable, such as
 * when tokens are validated or stored for reference. The fields provided allow for
 * detailed tracking of token properties and status.
 * </p>
 */
public record Token(
        String token,
        String subject,
        LocalDateTime issuedAt,
        LocalDateTime expiresIn,
        String issuer,
        List<String> scope,
        boolean isRevoked
) {}
