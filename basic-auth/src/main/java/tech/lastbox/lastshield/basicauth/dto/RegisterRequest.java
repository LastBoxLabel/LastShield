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
