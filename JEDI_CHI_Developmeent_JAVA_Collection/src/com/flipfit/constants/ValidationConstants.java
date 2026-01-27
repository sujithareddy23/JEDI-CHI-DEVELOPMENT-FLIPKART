package com.flipfit.constants;

/** Constraints and patterns for validation. */
public final class ValidationConstants {
    private ValidationConstants() {}

    /** Simple email: non-empty, contains @ and dot. */
    public static final int EMAIL_MIN_LENGTH = 5;
    public static final int PASSWORD_MIN_LENGTH = 4;
    public static final int MOBILE_LENGTH = 10;
    /** Indian PAN: 5 letters, 4 digits, 1 letter. */
    public static final String PAN_PATTERN = "[A-Za-z]{5}[0-9]{4}[A-Za-z]";
    /** Indian GST: 2 digits state + 10 chars (PAN part) + 1 + 1 digit + 1 letter. Simplification: 15 chars, alphanumeric. */
    public static final int GST_LENGTH = 15;
    public static final int GYM_ID_MIN_LENGTH = 2;
    public static final int NAME_MIN_LENGTH = 2;
    public static final int ROLE_CHOICE_MIN = 1;
    public static final int ROLE_CHOICE_MAX = 3;
}
