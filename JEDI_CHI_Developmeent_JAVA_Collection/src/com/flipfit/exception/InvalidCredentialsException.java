package com.flipfit.exception;

/**
 * Thrown when login fails (invalid username, password, or no match).
 */
public class InvalidCredentialsException extends FlipFitException {

    public InvalidCredentialsException(String message) {
        super(message);
    }

    public InvalidCredentialsException(String message, Throwable cause) {
        super(message, cause);
    }
}
