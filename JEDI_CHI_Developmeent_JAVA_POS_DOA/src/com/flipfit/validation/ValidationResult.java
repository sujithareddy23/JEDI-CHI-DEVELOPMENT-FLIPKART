package com.flipfit.validation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Result of a validation: either valid with no errors, or invalid with one or more error messages.
 */
public final class ValidationResult {
    private final boolean valid;
    private final List<String> errors;

    private ValidationResult(boolean valid, List<String> errors) {
        this.valid = valid;
        this.errors = errors == null ? new ArrayList<>() : new ArrayList<>(errors);
    }

    public static ValidationResult ok() {
        return new ValidationResult(true, Collections.emptyList());
    }

    public static ValidationResult fail(String message) {
        List<String> e = new ArrayList<>();
        e.add(message);
        return new ValidationResult(false, e);
    }

    public static ValidationResult fail(List<String> messages) {
        return new ValidationResult(false, messages);
    }

    public boolean isValid() {
        return valid;
    }

    public List<String> getErrors() {
        return Collections.unmodifiableList(errors);
    }

    /** Single error message, or first if many; empty if valid. */
    public String getFirstError() {
        return errors.isEmpty() ? "" : errors.get(0);
    }

    /** All errors joined by "; ". */
    public String getMessage() {
        return String.join("; ", errors);
    }
}
