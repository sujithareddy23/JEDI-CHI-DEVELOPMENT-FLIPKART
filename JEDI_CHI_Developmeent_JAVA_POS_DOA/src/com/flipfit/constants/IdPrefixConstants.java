package com.flipfit.constants;

/** ID prefixes and format patterns for entities. */
public final class IdPrefixConstants {
    private IdPrefixConstants() {}

    public static final String OWNER_PREFIX = "OWN";
    public static final String CUSTOMER_PREFIX = "C";
    public static final String BOOKING_PREFIX = "B";
    public static final String NOTIFICATION_PREFIX = "N";
    /** Slot ID format: gymId + "_" + hour, e.g. "BEL_06:00". Use String.format("%s_%02d:00", gymId, hour). */
    public static final String SLOT_ID_FORMAT = "%s_%02d:00";
}
