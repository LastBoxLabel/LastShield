package tech.lastbox.basicauth.enviroment;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Exception thrown when the issuer provided for Basic Authentication is invalid.
 * This exception is typically thrown when the `issuer` property is null or empty.
 * <p>
 * It extends {@link RuntimeException} and is used to indicate an issue with the configuration
 * of the Basic Authentication issuer.
 * </p>
 */
@Schema(description = "Exception thrown when the issuer provided for Basic Authentication is invalid.")
class InvalidIssuerException extends RuntimeException {
    /**
     * Constructor for InvalidIssuerException.
     *
     * @param message the detail message to be associated with the exception.
     */
    public InvalidIssuerException(String message) {
        super(message);
    }
}
