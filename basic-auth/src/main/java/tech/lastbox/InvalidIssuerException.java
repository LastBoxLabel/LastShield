package tech.lastbox;

class InvalidIssuerException extends RuntimeException {
    public InvalidIssuerException(String message) {
        super(message);
    }
}
