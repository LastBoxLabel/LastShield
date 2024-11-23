package tech.lastbox.security.core;

public class EmptyTokenException extends RuntimeException {

    public EmptyTokenException() {
        super("Empty token");
    }
}
