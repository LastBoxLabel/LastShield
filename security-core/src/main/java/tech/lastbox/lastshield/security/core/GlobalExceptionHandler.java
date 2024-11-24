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
 * </p>
 */
@ControllerAdvice
public class GlobalExceptionHandler {

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
