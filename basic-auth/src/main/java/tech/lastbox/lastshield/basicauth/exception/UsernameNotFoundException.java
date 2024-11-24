package tech.lastbox.lastshield.basicauth.exception;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Exception thrown when a username is not found in the database.
 * This exception is used to indicate that the specified username does not exist.
 */
@Schema(description = "Exception thrown when a username is not found in the database.")
public class UsernameNotFoundException extends Exception {
    /**
     * Constructor for UsernameNotFoundException with a default message.
     * The default message indicates that the username was not found in the database.
     */
    public UsernameNotFoundException() {
        super("Username not found in database.");
    }
}
