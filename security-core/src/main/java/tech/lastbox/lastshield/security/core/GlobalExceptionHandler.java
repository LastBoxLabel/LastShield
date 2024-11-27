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

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Global exception handler that handles {@link RuntimeException} exceptions across the entire application.
 * <p>
 * This class is annotated with {@link ControllerAdvice}, allowing it to handle exceptions globally and return
 * a standardized error response to the client. When a {@link RuntimeException} is thrown, the handler constructs an
 * {@link ErrorResponse} with the exception message, the HTTP status code, and a timestamp, and then returns it
 * as part of a {@link ResponseEntity}.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Default constructor for the GlobalExceptionHandler class.
     * This constructor is used to instantiate the GlobalExceptionHandler class.
     */
    public GlobalExceptionHandler() {
    }

    /**
     * Handles {@link RuntimeException} by creating an {@link ErrorResponse} with the exception message and a
     * {@link HttpStatus#BAD_REQUEST} status code, then returns it as part of a {@link ResponseEntity} with a
     * {@link HttpStatus#NOT_FOUND} response status.
     *
     * @param ex the {@link RuntimeException} that was thrown.
     * @return a {@link ResponseEntity} containing the {@link ErrorResponse} with a {@link HttpStatus#NOT_FOUND} status.
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> handleResourceNotFoundException(RuntimeException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                ex.getMessage(),
                HttpStatus.BAD_REQUEST.value()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }
}
