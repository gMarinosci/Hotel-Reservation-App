package se.scandium.hotelproject.exception;

public class UserNotFoundException extends Exception {
    private final String message;

    public UserNotFoundException(String message) {
        super(message);
        this.message = message;
    }

    public UserNotFoundException(String message, Throwable cause) {
        super(message, cause);
        this.message = message;
    }
}
