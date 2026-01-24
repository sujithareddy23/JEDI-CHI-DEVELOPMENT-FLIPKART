package com.flipfit.exception;

/**
 * Thrown when an entity (Slot, GymCenter, Booking, Customer, Owner, etc.) is not found.
 */
public class NotFoundException extends FlipFitException {

    public NotFoundException(String message) {
        super(message);
    }

    public NotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
