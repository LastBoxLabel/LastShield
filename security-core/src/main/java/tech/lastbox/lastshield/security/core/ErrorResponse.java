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
package tech.lastbox.lastshield.security.core;

/**
 * Represents a standardized error response format for API error messages.
 * <p>
 * This class contains three key properties: a message describing the error, the HTTP status code associated with the error,
 * and a timestamp indicating when the error occurred.
 */
public final class ErrorResponse {
    private final String message;
    private final int statusCode;
    private final String timestamp;

    /**
     * Constructs a new {@code ErrorResponse} with the specified error message and status code.
     * The timestamp is automatically set to the current date and time.
     *
     * @param message the error message to be included in the response.
     * @param statusCode the HTTP status code representing the error.
     */
    public ErrorResponse(String message, int statusCode) {
        this.message = message;
        this.statusCode = statusCode;
        this.timestamp = java.time.LocalDateTime.now().toString();
    }

    /**
     * Gets the error message.
     *
     * @return the error message.
     */
    public String getMessage() {
        return message;
    }

    /**
     * Gets the HTTP status code associated with the error.
     *
     * @return the HTTP status code.
     */
    public int getStatusCode() {
        return statusCode;
    }

    /**
     * Gets the timestamp indicating when the error occurred.
     *
     * @return the timestamp of the error occurrence.
     */
    public String getTimestamp() {
        return timestamp;
    }
}
