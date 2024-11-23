package tech.lastbox.enviroment;

class InvalidSecretKeyException extends RuntimeException {
    public InvalidSecretKeyException(String message) {
        super(message);
    }
}
