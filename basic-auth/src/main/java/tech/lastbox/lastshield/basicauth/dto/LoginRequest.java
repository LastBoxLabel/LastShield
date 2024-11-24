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
