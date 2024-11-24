package tech.lastbox.lastshield.security.core;

/**
 * {@code EmptyTokenException} is a custom exception that extends {@link RuntimeException}.
 * This exception is thrown when an expected token is empty or missing in the application context.
 * <p>
 * It provides a default error message, "Empty token", indicating that a required token was not provided
 * or was empty during some operation, such as authentication or authorization.
 * </p>
 */
public class EmptyTokenException extends RuntimeException {
    /**
     * Constructs a new {@code EmptyTokenException} with the default message "Empty token".
     */
    public EmptyTokenException() {
        super("Empty token");
    }
}
