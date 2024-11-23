package tech.lastbox.basicauth.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

/**
 * Data Transfer Object (DTO) representing an error response.
 * This DTO contains details about an error, including the error message,
 * the status of the error, and the timestamp when the error occurred.
 */
@Schema(description = "Response DTO for errors, containing an error message, status, and the timestamp of the error occurrence.")
public record ErrorResponse(

        @Schema(description = "A description of the error that occurred.", example = "Invalid username or password.")
        String message,

        @Schema(description = "Status of the error, typically a string such as 'error' or 'failed'.", example = "error")
        String status,

        @Schema(description = "Timestamp when the error occurred.", example = "2024-11-23T12:45:00")
        LocalDateTime timestamp
) {
}
