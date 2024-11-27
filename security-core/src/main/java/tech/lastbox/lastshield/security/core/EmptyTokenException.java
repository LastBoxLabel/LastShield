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
 * {@code EmptyTokenException} is a custom exception that extends {@link RuntimeException}.
 * This exception is thrown when an expected token is empty or missing in the application context.
 * <p>
 * It provides a default error message, "Empty token", indicating that a required token was not provided
 * or was empty during some operation, such as authentication or authorization.
 */
public class EmptyTokenException extends RuntimeException {
    /**
     * Constructs a new {@code EmptyTokenException} with the default message "Empty token".
     */
    public EmptyTokenException() {
        super("Empty token");
    }
}
