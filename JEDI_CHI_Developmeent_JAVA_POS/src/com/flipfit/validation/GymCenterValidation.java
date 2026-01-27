package com.flipfit.validation;

import com.flipfit.bean.GymCenter;
import com.flipfit.constants.ValidationConstants;

import java.util.ArrayList;
import java.util.List;

/** Validates GymCenter for registration. */
public final class GymCenterValidation {
    private GymCenterValidation() {}

    public static ValidationResult validateForRegistration(GymCenter g) {
        List<String> errors = new ArrayList<>();
        if (g == null) {
            return ValidationResult.fail("Gym center cannot be null.");
        }
        add(errors, validateGymId(g.getGymId()));
        add(errors, InputValidation.validateName(g.getName(), "Gym name"));
        add(errors, InputValidation.isNotBlank(g.getLocation(), "Location"));
        add(errors, InputValidation.isNotBlank(g.getContactNo(), "Contact number"));
        return errors.isEmpty() ? ValidationResult.ok() : ValidationResult.fail(errors);
    }

    public static ValidationResult validateGymId(String gymId) {
        if (gymId == null || gymId.trim().isEmpty()) {
            return ValidationResult.fail("Gym ID cannot be empty.");
        }
        if (gymId.trim().length() < ValidationConstants.GYM_ID_MIN_LENGTH) {
            return ValidationResult.fail("Gym ID must be at least " + ValidationConstants.GYM_ID_MIN_LENGTH + " characters.");
        }
        return ValidationResult.ok();
    }

    private static void add(List<String> target, ValidationResult r) {
        if (!r.isValid()) target.addAll(r.getErrors());
    }
}
