package tech.lastbox.jwt;

class TokenCreationException extends RuntimeException {
    public TokenCreationException(String message) {
        super(message);
    }
}
