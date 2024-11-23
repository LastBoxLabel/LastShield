package tech.lastbox.exception;

public class UnregisteredUserException extends Exception {
    public UnregisteredUserException(String message) {
        super(message);
    }
}
