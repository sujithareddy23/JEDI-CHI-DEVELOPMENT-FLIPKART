package com.flipfit.exception;

/**
 * Thrown when attempting to register an email or identifier that already exists.
 */
public class AlreadyExistsException extends FlipFitException {

    public AlreadyExistsException(String message) {
        super(message);
    }

    public AlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }
}
