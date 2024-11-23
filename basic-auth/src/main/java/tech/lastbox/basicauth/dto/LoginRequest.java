package tech.lastbox.basicauth.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Data Transfer Object (DTO) representing a login request.
 * This DTO is used to encapsulate the login credentials (username and password)
 * provided by the user when attempting to authenticate.
 */
@Schema(description = "DTO for user login request containing the username and password for authentication.")
public record LoginRequest(

        @Schema(description = "The username of the user attempting to log in.", example = "johndoe")
        String username,

        @Schema(description = "The password of the user attempting to log in.", example = "securePassword123")
        String password
) {
}
