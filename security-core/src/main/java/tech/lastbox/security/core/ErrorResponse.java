package tech.lastbox.security.core;

public final class ErrorResponse {
    private final String message;
    private final int statusCode;
    private final String timestamp;

    public ErrorResponse(String message, int statusCode) {
        this.message = message;
        this.statusCode = statusCode;
        this.timestamp = java.time.LocalDateTime.now().toString();
    }

    public String getMessage() {
        return message;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getTimestamp() {
        return timestamp;
    }
}
