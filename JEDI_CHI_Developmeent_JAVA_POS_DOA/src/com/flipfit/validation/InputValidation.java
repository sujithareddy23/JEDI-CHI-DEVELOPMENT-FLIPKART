package com.flipfit.validation;

import com.flipfit.constants.FormatConstants;
import com.flipfit.constants.ValidationConstants;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/** Common input validations: email, password, mobile, date, time, PAN, GST, non-empty. */
public final class InputValidation {
    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern(FormatConstants.DATE_PATTERN);
    private static final DateTimeFormatter TIME_FMT = DateTimeFormatter.ofPattern(FormatConstants.TIME_PATTERN);
    private static final Pattern PAN_PATTERN = Pattern.compile(ValidationConstants.PAN_PATTERN);

    private InputValidation() {}

    public static ValidationResult isNotBlank(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            return ValidationResult.fail(fieldName + " cannot be empty.");
        }
        return ValidationResult.ok();
    }

    public static ValidationResult validateEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return ValidationResult.fail("Email cannot be empty.");
        }
        String e = email.trim();
        if (e.length() < ValidationConstants.EMAIL_MIN_LENGTH) {
            return ValidationResult.fail("Email is too short.");
        }
        if (!e.contains("@") || !e.contains(".")) {
            return ValidationResult.fail("Email must contain @ and a domain.");
        }
        return ValidationResult.ok();
    }

    public static ValidationResult validatePassword(String password) {
        if (password == null || password.isEmpty()) {
            return ValidationResult.fail("Password cannot be empty.");
        }
        if (password.length() < ValidationConstants.PASSWORD_MIN_LENGTH) {
            return ValidationResult.fail("Password must be at least " + ValidationConstants.PASSWORD_MIN_LENGTH + " characters.");
        }
        return ValidationResult.ok();
    }

    public static ValidationResult validateMobile(String mobile) {
        if (mobile == null || mobile.trim().isEmpty()) {
            return ValidationResult.fail("Mobile number cannot be empty.");
        }
        String m = mobile.trim().replaceAll("[^0-9]", "");
        if (m.length() != ValidationConstants.MOBILE_LENGTH) {
            return ValidationResult.fail("Mobile must be " + ValidationConstants.MOBILE_LENGTH + " digits.");
        }
        return ValidationResult.ok();
    }

    public static ValidationResult validateDate(String dateStr) {
        if (dateStr == null || dateStr.trim().isEmpty()) {
            return ValidationResult.fail("Date cannot be empty.");
        }
        try {
            LocalDate.parse(dateStr.trim(), DATE_FMT);
            return ValidationResult.ok();
        } catch (Exception e) {
            return ValidationResult.fail("Invalid date. Use " + FormatConstants.DATE_PATTERN + ".");
        }
    }

    public static ValidationResult validateDateNotInPast(String dateStr) {
        ValidationResult r = validateDate(dateStr);
        if (!r.isValid()) return r;
        try {
            LocalDate d = LocalDate.parse(dateStr.trim(), DATE_FMT);
            if (d.isBefore(LocalDate.now())) {
                return ValidationResult.fail("Date cannot be in the past.");
            }
            return ValidationResult.ok();
        } catch (Exception e) {
            return ValidationResult.fail("Invalid date. Use " + FormatConstants.DATE_PATTERN + ".");
        }
    }

    public static ValidationResult validateTime(String timeStr) {
        if (timeStr == null || timeStr.trim().isEmpty()) {
            return ValidationResult.fail("Time cannot be empty.");
        }
        try {
            LocalTime.parse(timeStr.trim(), TIME_FMT);
            return ValidationResult.ok();
        } catch (Exception e) {
            return ValidationResult.fail("Invalid time. Use " + FormatConstants.TIME_PATTERN + ".");
        }
    }

    public static ValidationResult validatePan(String pan) {
        if (pan == null || pan.trim().isEmpty()) {
            return ValidationResult.fail("PAN cannot be empty.");
        }
        String p = pan.trim().toUpperCase();
        if (!PAN_PATTERN.matcher(p).matches()) {
            return ValidationResult.fail("PAN must be 5 letters, 4 digits, 1 letter (e.g. ABCDE1234F).");
        }
        return ValidationResult.ok();
    }

    public static ValidationResult validateGst(String gst) {
        if (gst == null || gst.trim().isEmpty()) {
            return ValidationResult.ok(); // optional
        }
        String g = gst.trim();
        if (g.length() != ValidationConstants.GST_LENGTH) {
            return ValidationResult.fail("GST must be " + ValidationConstants.GST_LENGTH + " characters.");
        }
        return ValidationResult.ok();
    }

    public static ValidationResult validateRoleChoice(int choice) {
        if (choice < ValidationConstants.ROLE_CHOICE_MIN || choice > ValidationConstants.ROLE_CHOICE_MAX) {
            return ValidationResult.fail("Role must be 1, 2, or 3.");
        }
        return ValidationResult.ok();
    }

    public static ValidationResult validateName(String name, String fieldName) {
        if (name == null || name.trim().isEmpty()) {
            return ValidationResult.fail(fieldName + " cannot be empty.");
        }
        if (name.trim().length() < ValidationConstants.NAME_MIN_LENGTH) {
            return ValidationResult.fail(fieldName + " must be at least " + ValidationConstants.NAME_MIN_LENGTH + " characters.");
        }
        return ValidationResult.ok();
    }
}
