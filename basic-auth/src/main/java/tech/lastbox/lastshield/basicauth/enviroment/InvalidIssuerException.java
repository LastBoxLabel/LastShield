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
package tech.lastbox.lastshield.basicauth.enviroment;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Exception thrown when the issuer provided for Basic Authentication is invalid.
 * This exception is typically thrown when the `issuer` property is null or empty.
 * <p>
 * It extends {@link RuntimeException} and is used to indicate an issue with the configuration
 * of the Basic Authentication issuer.
 * </p>
 */
@Schema(description = "Exception thrown when the issuer provided for Basic Authentication is invalid.")
class InvalidIssuerException extends RuntimeException {
    /**
     * Constructor for InvalidIssuerException.
     *
     * @param message the detail message to be associated with the exception.
     */
    public InvalidIssuerException(String message) {
        super(message);
    }
}
