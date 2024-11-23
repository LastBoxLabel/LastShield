package tech.lastbox.exception;

public class DuplicatedUserException extends Exception {
    public DuplicatedUserException() {
        super("This username already exists.");
    }
}
