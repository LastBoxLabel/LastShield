/*
 * Copyright 2024 LastBox
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package tech.lastbox.lastshield.basicauth.dto;

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
