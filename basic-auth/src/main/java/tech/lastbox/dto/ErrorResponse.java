package tech.lastbox.dto;

import java.time.LocalDateTime;

public record ErrorResponse(String message, String status, LocalDateTime timestamp) {
}
