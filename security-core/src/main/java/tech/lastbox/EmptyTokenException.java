package tech.lastbox;

public class EmptyTokenException extends RuntimeException {

    public EmptyTokenException() {
        super("Empty token");
    }
}
