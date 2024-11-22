package tech.lastbox;

class InvalidSecretKeyException extends RuntimeException {
    public InvalidSecretKeyException(String message) {
        super(message);
    }
}
