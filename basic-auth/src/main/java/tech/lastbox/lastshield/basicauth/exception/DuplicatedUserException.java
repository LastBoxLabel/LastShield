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
package tech.lastbox.lastshield.basicauth.exception;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Exception thrown when attempting to create a user with a username that already exists.
 * This exception is used to indicate that the username is not unique and cannot be used for new user registration.
 * <p>
 * This exception extends {@link Exception} and provides a default message indicating
 * that the username already exists in the system.
 */
@Schema(description = "Exception thrown when attempting to create a user with a username that already exists.")
public class DuplicatedUserException extends Exception {
    /**
     * Constructor for DuplicatedUserException.
     * The constructor uses a default message indicating that the username already exists.
     */
    public DuplicatedUserException() {
        super("This username already exists.");
    }
}
