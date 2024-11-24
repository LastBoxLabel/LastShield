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
 * Exception thrown when a username is not found in the database.
 * This exception is used to indicate that the specified username does not exist.
 */
@Schema(description = "Exception thrown when a username is not found in the database.")
public class UsernameNotFoundException extends Exception {
    /**
     * Constructor for UsernameNotFoundException with a default message.
     * The default message indicates that the username was not found in the database.
     */
    public UsernameNotFoundException() {
        super("Username not found in database.");
    }
}
