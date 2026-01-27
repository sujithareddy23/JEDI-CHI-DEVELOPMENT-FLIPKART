package com.flipfit.constants;

/** Hardcoded demo data for FlipFit Beta (DataStore init). */
public final class DemoDataConstants {
    private DemoDataConstants() {}

    // Admin
    public static final String ADMIN_ID = "ADMIN1";
    public static final String ADMIN_NAME = "FlipFit Admin";
    public static final String ADMIN_PASSWORD = "admin123";

    // Owners
    public static final String OWNER1_ID = "OWN1";
    public static final String OWNER1_NAME = "Bellandur Gym Owner";
    public static final String OWNER1_EMAIL = "owner.bellandur@flipfit.com";
    public static final String OWNER1_PASSWORD = "owner1";
    public static final String OWNER1_PAN = "ABCDE1234F";
    public static final String OWNER1_GST = "29ABCDE1234F1Z5";

    public static final String OWNER2_ID = "OWN2";
    public static final String OWNER2_NAME = "Koramangala Gym Owner";
    public static final String OWNER2_EMAIL = "owner.koramangala@flipfit.com";
    public static final String OWNER2_PASSWORD = "owner2";
    public static final String OWNER2_PAN = "FGHIJ5678K";
    public static final String OWNER2_GST = "29FGHIJ5678K1Z5";

    public static final String OWNER3_ID = "OWN3";
    public static final String OWNER3_NAME = "Indiranagar Gym Owner";
    public static final String OWNER3_EMAIL = "owner.indiranagar@flipfit.com";
    public static final String OWNER3_PASSWORD = "owner3";

    // Customers
    public static final String CUSTOMER1_ID = "C1";
    public static final String CUSTOMER1_NAME = "Alice";
    public static final String CUSTOMER1_EMAIL = "alice@example.com";
    public static final String CUSTOMER1_PASSWORD = "alice123";
    public static final String CUSTOMER1_MOBILE = "9876543210";
    public static final String CUSTOMER1_ADDRESS = "Bangalore - Bellandur";

    public static final String CUSTOMER2_ID = "C2";
    public static final String CUSTOMER2_NAME = "Bob";
    public static final String CUSTOMER2_EMAIL = "bob@example.com";
    public static final String CUSTOMER2_PASSWORD = "bob123";
    public static final String CUSTOMER2_MOBILE = "9876543211";
    public static final String CUSTOMER2_ADDRESS = "Bangalore - Koramangala";

    public static final String CUSTOMER3_ID = "C3";
    public static final String CUSTOMER3_NAME = "Carol";
    public static final String CUSTOMER3_EMAIL = "carol@example.com";
    public static final String CUSTOMER3_PASSWORD = "carol123";
    public static final String CUSTOMER3_MOBILE = "9876543212";
    public static final String CUSTOMER3_ADDRESS = "Bangalore - Indiranagar";

    // Gym centers
    public static final String GYM_BEL = "BEL";
    public static final String GYM_BEL_NAME = "FlipFit Bellandur";
    public static final String GYM_BEL_LOCATION = "Bangalore - Bellandur";
    public static final String GYM_BEL_CONTACT = "080-12345678";
    public static final int GYM_BEL_CAPACITY = 5;

    public static final String GYM_KOR = "KOR";
    public static final String GYM_KOR_NAME = "FlipFit Koramangala";
    public static final String GYM_KOR_LOCATION = "Bangalore - Koramangala";
    public static final String GYM_KOR_CONTACT = "080-12345679";
    public static final int GYM_KOR_CAPACITY = 4;

    public static final String GYM_IND = "IND";
    public static final String GYM_IND_NAME = "FlipFit Indiranagar";
    public static final String GYM_IND_LOCATION = "Bangalore - Indiranagar";
    public static final String GYM_IND_CONTACT = "080-12345680";
    public static final int GYM_IND_CAPACITY = 6;

    // Slot times (hour, 24h)
    public static final int SLOT_AM_START = 6;
    public static final int SLOT_AM_END = 9;
    public static final int SLOT_PM_START = 18;
    public static final int SLOT_PM_END = 21;

    // ID generation
    public static final int BOOKING_ID_INITIAL = 1000;
    public static final int NOTIFICATION_ID_INITIAL = 1;

    // Prompt examples (UI)
    public static final String GYM_ID_EXAMPLES = "BEL, KOR, IND";
    public static final String SLOT_ID_EXAMPLE = "BEL_06:00";
}
