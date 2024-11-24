package tech.lastbox.lastshield.basicauth.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Data Transfer Object (DTO) representing a register request.
 * This DTO is used to encapsulate the user registration details,
 * including the user's name, username, and password.
 */
@Schema(description = "DTO for user registration request containing the user's name, username, and password for account creation.")
public record RegisterRequest(

        @Schema(description = "The full name of the user being registered.", example = "John Doe")
        String name,

        @Schema(description = "The desired username for the user account.", example = "johndoe")
        String username,

        @Schema(description = "The password for the user account.", example = "securePassword123")
        String password
) {
}
