package com.flipfit.exception;

/**
 * Thrown when a booking operation fails (e.g. cannot cancel - not your booking).
 */
public class BookingException extends FlipFitException {

    public BookingException(String message) {
        super(message);
    }

    public BookingException(String message, Throwable cause) {
        super(message, cause);
    }
}
