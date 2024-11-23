package tech.lastbox.dto;

import java.time.LocalDateTime;

public record AuthResponseDTO(Long id, String token, String message, LocalDateTime timestamp) {
}
