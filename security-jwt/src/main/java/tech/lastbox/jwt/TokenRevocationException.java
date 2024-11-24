package tech.lastbox.jwt;

class TokenRevocationException extends RuntimeException {
    public TokenRevocationException(String message) {
        super(message);
    }
}
