package se.scandium.hotelproject.exception;

public class ArgumentInvalidException extends RuntimeException {
    private final String message;

    public ArgumentInvalidException(String message) {
        super(message);
        this.message = message;
    }

    public ArgumentInvalidException(String message, Throwable cause) {
        super(message, cause);
        this.message = message;
    }
}
