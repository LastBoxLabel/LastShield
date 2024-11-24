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
package tech.lastbox.lastshield.security.core.http;

/**
 * Enum {@code HttpMethod} that represents HTTP methods with their respective descriptions.
 *
 * <p>This enum maps common HTTP methods used for interacting with web services.
 * Each method has a description that explains its purpose and usage. HTTP methods are used
 * to define the desired action when communicating with a server.</p>
 *
 * <ul>
 *     <li>{@link HttpMethod#GET} - Retrieve data from the server.</li>
 *     <li>{@link HttpMethod#POST} - Submit data to the server.</li>
 *     <li>{@link HttpMethod#PUT} - Update or replace data on the server.</li>
 *     <li>{@link HttpMethod#DELETE} - Remove data from the server.</li>
 *     <li>{@link HttpMethod#PATCH} - Partially update data on the server.</li>
 *     <li>{@link HttpMethod#HEAD} - Retrieve only the response headers.</li>
 *     <li>{@link HttpMethod#OPTIONS} - Describe communication options with the server.</li>
 *     <li>{@link HttpMethod#TRACE} - Echo the request for debugging.</li>
 *     <li>{@link HttpMethod#CONNECT} - Establish a tunnel for communication.</li>
 * </ul>

 */
public enum HttpMethod {
    GET("Retrieve data"),
    POST("Submit data"),
    PUT("Update or replace data"),
    DELETE("Remove data"),
    PATCH("Partially update data"),
    HEAD("Retrieve headers only"),
    OPTIONS("Describe communication options"),
    TRACE("Echo request for debugging"),
    CONNECT("Establish a tunnel");

    private final String description;

    HttpMethod(String description) {
        this.description = description;
    }

    /**
     * Returns the description associated with this HTTP method.
     *
     * @return The description of the HTTP method.
     */
    public String getDescription() {
        return description;
    }
}
