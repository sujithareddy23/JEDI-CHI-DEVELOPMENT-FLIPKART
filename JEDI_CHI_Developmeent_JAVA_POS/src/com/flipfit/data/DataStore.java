package com.flipfit.data;

import com.flipfit.bean.*;
import com.flipfit.constants.DemoDataConstants;
import com.flipfit.constants.IdPrefixConstants;

import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * In-memory data store using Java Collection API (Map, List, Set).
 * Hardcoded data for FlipFit Beta - Bangalore gyms.
 */
public final class DataStore {

    private static final AtomicInteger BOOKING_ID_GEN = new AtomicInteger(DemoDataConstants.BOOKING_ID_INITIAL);
    private static final AtomicInteger NOTIFICATION_ID_GEN = new AtomicInteger(DemoDataConstants.NOTIFICATION_ID_INITIAL);

    private static final Map<String, GymAdmin> ADMINS = new HashMap<>();
    private static final Map<String, GymOwner> OWNERS = new HashMap<>();
    private static final Map<String, GymCustomer> CUSTOMERS = new HashMap<>();
    private static final Map<String, GymCenter> CENTERS = new HashMap<>();
    private static final Map<String, List<Slot>> GYM_SLOTS = new HashMap<>();
    private static final List<Booking> BOOKINGS = new ArrayList<>();
    private static final List<Notification> NOTIFICATIONS = new ArrayList<>();
    private static final Set<String> SLOT_IDS = new HashSet<>();

    static {
        initAdmins();
        initOwners();
        initCustomers();
        initCentersAndSlots();
    }

    private static void initAdmins() {
        GymAdmin a = new GymAdmin();
        a.setAdminId(DemoDataConstants.ADMIN_ID);
        a.setName(DemoDataConstants.ADMIN_NAME);
        a.setPassword(DemoDataConstants.ADMIN_PASSWORD);
        ADMINS.put(a.getAdminId(), a);
    }

    private static void initOwners() {
        GymOwner o1 = new GymOwner();
        o1.setId(DemoDataConstants.OWNER1_ID);
        o1.setOwnerName(DemoDataConstants.OWNER1_NAME);
        o1.setEmailId(DemoDataConstants.OWNER1_EMAIL);
        o1.setPassword(DemoDataConstants.OWNER1_PASSWORD);
        o1.setPanNo(DemoDataConstants.OWNER1_PAN);
        o1.setGstNo(DemoDataConstants.OWNER1_GST);
        o1.setValidated(true);
        OWNERS.put(o1.getEmailId(), o1);

        GymOwner o2 = new GymOwner();
        o2.setId(DemoDataConstants.OWNER2_ID);
        o2.setOwnerName(DemoDataConstants.OWNER2_NAME);
        o2.setEmailId(DemoDataConstants.OWNER2_EMAIL);
        o2.setPassword(DemoDataConstants.OWNER2_PASSWORD);
        o2.setPanNo(DemoDataConstants.OWNER2_PAN);
        o2.setGstNo(DemoDataConstants.OWNER2_GST);
        o2.setValidated(true);
        OWNERS.put(o2.getEmailId(), o2);

        GymOwner o3 = new GymOwner();
        o3.setId(DemoDataConstants.OWNER3_ID);
        o3.setOwnerName(DemoDataConstants.OWNER3_NAME);
        o3.setEmailId(DemoDataConstants.OWNER3_EMAIL);
        o3.setPassword(DemoDataConstants.OWNER3_PASSWORD);
        o3.setValidated(false);
        OWNERS.put(o3.getEmailId(), o3);
    }

    private static void initCustomers() {
        GymCustomer c1 = new GymCustomer();
        c1.setId(DemoDataConstants.CUSTOMER1_ID);
        c1.setName(DemoDataConstants.CUSTOMER1_NAME);
        c1.setEmail(DemoDataConstants.CUSTOMER1_EMAIL);
        c1.setPassword(DemoDataConstants.CUSTOMER1_PASSWORD);
        c1.setMobileNo(DemoDataConstants.CUSTOMER1_MOBILE);
        c1.setAddress(DemoDataConstants.CUSTOMER1_ADDRESS);
        CUSTOMERS.put(c1.getEmail(), c1);

        GymCustomer c2 = new GymCustomer();
        c2.setId(DemoDataConstants.CUSTOMER2_ID);
        c2.setName(DemoDataConstants.CUSTOMER2_NAME);
        c2.setEmail(DemoDataConstants.CUSTOMER2_EMAIL);
        c2.setPassword(DemoDataConstants.CUSTOMER2_PASSWORD);
        c2.setMobileNo(DemoDataConstants.CUSTOMER2_MOBILE);
        c2.setAddress(DemoDataConstants.CUSTOMER2_ADDRESS);
        CUSTOMERS.put(c2.getEmail(), c2);

        GymCustomer c3 = new GymCustomer();
        c3.setId(DemoDataConstants.CUSTOMER3_ID);
        c3.setName(DemoDataConstants.CUSTOMER3_NAME);
        c3.setEmail(DemoDataConstants.CUSTOMER3_EMAIL);
        c3.setPassword(DemoDataConstants.CUSTOMER3_PASSWORD);
        c3.setMobileNo(DemoDataConstants.CUSTOMER3_MOBILE);
        c3.setAddress(DemoDataConstants.CUSTOMER3_ADDRESS);
        CUSTOMERS.put(c3.getEmail(), c3);
    }

    private static void initCentersAndSlots() {
        LocalTime amStart = LocalTime.of(DemoDataConstants.SLOT_AM_START, 0);
        LocalTime amEnd = LocalTime.of(DemoDataConstants.SLOT_AM_END, 0);
        LocalTime pmStart = LocalTime.of(DemoDataConstants.SLOT_PM_START, 0);
        LocalTime pmEnd = LocalTime.of(DemoDataConstants.SLOT_PM_END, 0);

        createCenter(DemoDataConstants.GYM_BEL, DemoDataConstants.GYM_BEL_NAME, DemoDataConstants.GYM_BEL_LOCATION,
                DemoDataConstants.GYM_BEL_CONTACT, DemoDataConstants.OWNER1_ID, true, amStart, amEnd, pmStart, pmEnd, DemoDataConstants.GYM_BEL_CAPACITY);
        createCenter(DemoDataConstants.GYM_KOR, DemoDataConstants.GYM_KOR_NAME, DemoDataConstants.GYM_KOR_LOCATION,
                DemoDataConstants.GYM_KOR_CONTACT, DemoDataConstants.OWNER2_ID, true, amStart, amEnd, pmStart, pmEnd, DemoDataConstants.GYM_KOR_CAPACITY);
        createCenter(DemoDataConstants.GYM_IND, DemoDataConstants.GYM_IND_NAME, DemoDataConstants.GYM_IND_LOCATION,
                DemoDataConstants.GYM_IND_CONTACT, DemoDataConstants.OWNER3_ID, false, amStart, amEnd, pmStart, pmEnd, DemoDataConstants.GYM_IND_CAPACITY);
    }

    private static void createCenter(String gymId, String name, String location, String contact, String ownerId,
            boolean validated, LocalTime amStart, LocalTime amEnd, LocalTime pmStart, LocalTime pmEnd, int capacity) {
        GymCenter g = new GymCenter();
        g.setGymId(gymId);
        g.setName(name);
        g.setLocation(location);
        g.setContactNo(contact);
        g.setOwnerId(ownerId);
        g.setValidated(validated);
        List<Slot> slots = new ArrayList<>();
        for (int h = amStart.getHour(); h < amEnd.getHour(); h++) {
            Slot s = createSlot(gymId, h, capacity);
            slots.add(s);
            SLOT_IDS.add(s.getSlotId());
        }
        for (int h = pmStart.getHour(); h < pmEnd.getHour(); h++) {
            Slot s = createSlot(gymId, h, capacity);
            slots.add(s);
            SLOT_IDS.add(s.getSlotId());
        }
        GYM_SLOTS.put(gymId, slots);
        g.setSlotList(slots);
        CENTERS.put(gymId, g);
    }

    private static Slot createSlot(String gymId, int hour, int capacity) {
        Slot s = new Slot();
        s.setSlotId(String.format(IdPrefixConstants.SLOT_ID_FORMAT, gymId, hour));
        s.setGymId(gymId);
        s.setStartTime(LocalTime.of(hour, 0));
        s.setEndTime(LocalTime.of(hour + 1, 0));
        s.setTotalCapacity(capacity);
        return s;
    }

    public static Map<String, GymAdmin> getAdmins() { return Collections.unmodifiableMap(ADMINS); }
    public static Map<String, GymOwner> getOwners() { return Collections.unmodifiableMap(OWNERS); }
    public static Map<String, GymCustomer> getCustomers() { return Collections.unmodifiableMap(CUSTOMERS); }
    public static Map<String, GymCenter> getCenters() { return Collections.unmodifiableMap(CENTERS); }
    public static Map<String, List<Slot>> getGymSlots() { return Collections.unmodifiableMap(GYM_SLOTS); }
    public static Set<String> getSlotIds() { return Collections.unmodifiableSet(SLOT_IDS); }

    public static List<Booking> getBookings() { return BOOKINGS; }
    public static List<Notification> getNotifications() { return NOTIFICATIONS; }

    public static List<Booking> getBookingsMutable() { return BOOKINGS; }
    public static List<Notification> getNotificationsMutable() { return NOTIFICATIONS; }
    public static Map<String, GymCustomer> getCustomersMutable() { return CUSTOMERS; }
    public static Map<String, GymOwner> getOwnersMutable() { return OWNERS; }
    public static Map<String, GymCenter> getCentersMutable() { return CENTERS; }
    public static Map<String, List<Slot>> getGymSlotsMutable() { return GYM_SLOTS; }

    public static String nextBookingId() { return IdPrefixConstants.BOOKING_PREFIX + BOOKING_ID_GEN.incrementAndGet(); }
    public static String nextNotificationId() { return IdPrefixConstants.NOTIFICATION_PREFIX + NOTIFICATION_ID_GEN.incrementAndGet(); }
}
