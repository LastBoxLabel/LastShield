package tech.lastbox.lastshield.basicauth.exception;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Exception thrown when attempting to create a user with a username that already exists.
 * This exception is used to indicate that the username is not unique and cannot be used for new user registration.
 * <p>
 * This exception extends {@link Exception} and provides a default message indicating
 * that the username already exists in the system.
 * </p>
 */
@Schema(description = "Exception thrown when attempting to create a user with a username that already exists.")
public class DuplicatedUserException extends Exception {
    /**
     * Constructor for DuplicatedUserException.
     * The constructor uses a default message indicating that the username already exists.
     */
    public DuplicatedUserException() {
        super("This username already exists.");
    }
}
