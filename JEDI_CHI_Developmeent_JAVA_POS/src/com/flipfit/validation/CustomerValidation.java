package com.flipfit.validation;

import com.flipfit.bean.GymCustomer;

import java.util.ArrayList;
import java.util.List;

/** Validates GymCustomer for registration and profile updates. */
public final class CustomerValidation {
    private CustomerValidation() {}

    public static ValidationResult validateForSignUp(GymCustomer c) {
        List<String> errors = new ArrayList<>();
        if (c == null) {
            return ValidationResult.fail("Customer cannot be null.");
        }
        add(errors, InputValidation.validateName(c.getName(), "Name"));
        add(errors, InputValidation.validateEmail(c.getEmail()));
        add(errors, InputValidation.validatePassword(c.getPassword()));
        add(errors, InputValidation.validateMobile(c.getMobileNo()));
        add(errors, InputValidation.isNotBlank(c.getAddress(), "Address"));
        return errors.isEmpty() ? ValidationResult.ok() : ValidationResult.fail(errors);
    }

    public static ValidationResult validateForProfileUpdate(String name, String mobile, String address) {
        List<String> errors = new ArrayList<>();
        if (name != null && !name.trim().isEmpty()) {
            add(errors, InputValidation.validateName(name, "Name"));
        }
        if (mobile != null && !mobile.trim().isEmpty()) {
            add(errors, InputValidation.validateMobile(mobile));
        }
        return errors.isEmpty() ? ValidationResult.ok() : ValidationResult.fail(errors);
    }

    private static void add(List<String> target, ValidationResult r) {
        if (!r.isValid()) target.addAll(r.getErrors());
    }
}
