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
