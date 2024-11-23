package tech.lastbox.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Data Transfer Object (DTO) representing a user.
 * This DTO is used to transfer the user's information such as ID, name, username, and role.
 */
@Schema(description = "DTO for transferring user information including ID, name, username, and role.")
public record UserDTO(

        @Schema(description = "The unique identifier of the user.", example = "1")
        Long id,

        @Schema(description = "The name of the user.", example = "John Doe")
        String name,

        @Schema(description = "The username of the user.", example = "johndoe")
        String username,

        @Schema(description = "The role assigned to the user.", example = "USER")
        String role
) {
}
