package tech.lastbox.exception;

public class UsernameNotFoundException extends Exception {
    public UsernameNotFoundException() {
        super("Username not found in database.");
    }
}
