package tech.lastbox.exception;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Exception thrown when attempting to update a user that is not registered in the system.
 * This exception is typically used when the user does not exist in the database.
 */
@Schema(description = "Exception thrown when attempting to update a user that is not registered in the system.")
public class UnregisteredUserException extends Exception {
    /**
     * Constructor for UnregisteredUserException with a custom message.
     *
     * @param message the detail message explaining the exception.
     */
    public UnregisteredUserException(String message) {
        super(message);
    }
}
