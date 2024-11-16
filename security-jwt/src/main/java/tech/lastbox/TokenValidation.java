package tech.lastbox;

import java.util.Optional;

/**
 * A record representing the validation result of a JWT (JSON Web Token).
 * It encapsulates the token (if valid) and a flag indicating whether the token is valid.
 *
 * @param tokenOptional An optional containing the token if it is valid, or empty if invalid.
 * @param isValid A flag indicating whether the token is valid.
 */
public record TokenValidation(Optional<Token> tokenOptional, boolean isValid) {
}
