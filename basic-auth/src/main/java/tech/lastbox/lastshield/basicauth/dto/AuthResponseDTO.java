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
