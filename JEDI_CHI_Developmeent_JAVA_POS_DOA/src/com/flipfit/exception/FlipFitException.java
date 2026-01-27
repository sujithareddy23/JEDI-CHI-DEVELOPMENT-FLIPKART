package com.flipfit.exception;

/**
 * Base exception for FlipFit application errors.
 */
public class FlipFitException extends RuntimeException {

    public FlipFitException(String message) {
        super(message);
    }

    public FlipFitException(String message, Throwable cause) {
        super(message, cause);
    }
}
