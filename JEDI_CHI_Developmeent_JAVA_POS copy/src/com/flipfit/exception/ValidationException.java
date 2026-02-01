package com.flipfit.exception;

/**
 * Thrown when validation fails (e.g. invalid email, PAN, or entity).
 */
public class ValidationException extends FlipFitException {

    public ValidationException(String message) {
        super(message);
    }

    public ValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}
