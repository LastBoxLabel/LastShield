package tech.lastbox.lastshield.basicauth.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

/**
 * Data Transfer Object (DTO) representing the response after authentication.
 * This DTO contains the details of the authentication response, including
 * the user's ID, the generated token, a message, and the timestamp of the response.
 */
@Schema(description = "Response DTO for authentication, containing the user's ID, the generated token, a message, and the timestamp of the response.")
public record AuthResponseDTO(
        @Schema(description = "Unique identifier of the authenticated user.", example = "12345")
        Long id,

        @Schema(description = "JWT token generated upon successful authentication.", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
        String token,

        @Schema(description = "Message providing additional information about the authentication response.", example = "Authentication successful.")
        String message,

        @Schema(description = "Timestamp when the authentication response was generated.", example = "2024-11-23T12:34:56")
        LocalDateTime timestamp
) {
}
