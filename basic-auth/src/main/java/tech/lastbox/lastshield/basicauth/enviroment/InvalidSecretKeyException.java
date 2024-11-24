package tech.lastbox.lastshield.basicauth.enviroment;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Exception thrown when the secret key for Basic Authentication is invalid.
 * This exception is typically thrown when the `secretKey` property is null or empty.
 * <p>
 * It extends {@link RuntimeException} and is used to indicate an issue with the configuration
 * of the Basic Authentication secret key.
 * </p>
 */
@Schema(description = "Exception thrown when the secret key for Basic Authentication is invalid.")
class InvalidSecretKeyException extends RuntimeException {
    /**
     * Constructor for InvalidSecretKeyException.
     *
     * @param message the detail message to be associated with the exception.
     */
    public InvalidSecretKeyException(String message) {
        super(message);
    }
}
