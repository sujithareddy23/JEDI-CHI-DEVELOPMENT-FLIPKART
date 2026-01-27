package com.flipfit.validation;

import com.flipfit.bean.GymOwner;

import java.util.ArrayList;
import java.util.List;

/** Validates GymOwner for registration and profile updates. */
public final class OwnerValidation {
    private OwnerValidation() {}

    public static ValidationResult validateForSignUp(GymOwner o) {
        List<String> errors = new ArrayList<>();
        if (o == null) {
            return ValidationResult.fail("Owner cannot be null.");
        }
        add(errors, InputValidation.validateName(o.getOwnerName(), "Owner name"));
        add(errors, InputValidation.validateEmail(o.getEmailId()));
        add(errors, InputValidation.validatePassword(o.getPassword()));
        add(errors, InputValidation.validatePan(o.getPanNo()));
        add(errors, InputValidation.validateGst(o.getGstNo()));
        return errors.isEmpty() ? ValidationResult.ok() : ValidationResult.fail(errors);
    }

    public static ValidationResult validateForProfileUpdate(String name, String password) {
        List<String> errors = new ArrayList<>();
        if (name != null && !name.trim().isEmpty()) {
            add(errors, InputValidation.validateName(name, "Name"));
        }
        if (password != null && !password.isEmpty()) {
            add(errors, InputValidation.validatePassword(password));
        }
        return errors.isEmpty() ? ValidationResult.ok() : ValidationResult.fail(errors);
    }

    private static void add(List<String> target, ValidationResult r) {
        if (!r.isValid()) target.addAll(r.getErrors());
    }
}
