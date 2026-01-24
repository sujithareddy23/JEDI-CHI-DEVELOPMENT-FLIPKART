package com.flipfit.data;

import com.flipfit.bean.*;
import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * In-memory data store using Java Collection API (Map, List, Set).
 * Hardcoded data for FlipFit Beta - Bangalore gyms.
 */
public final class DataStore {

    private static final AtomicInteger BOOKING_ID_GEN = new AtomicInteger(1000);
    private static final AtomicInteger NOTIFICATION_ID_GEN = new AtomicInteger(1);

    // Map: adminId -> GymAdmin
    private static final Map<String, GymAdmin> ADMINS = new HashMap<>();
    // Map: emailId -> GymOwner
    private static final Map<String, GymOwner> OWNERS = new HashMap<>();
    // Map: email -> GymCustomer
    private static final Map<String, GymCustomer> CUSTOMERS = new HashMap<>();
    // Map: gymId -> GymCenter
    private static final Map<String, GymCenter> CENTERS = new HashMap<>();
    // Map: gymId -> List of Slot (templates)
    private static final Map<String, List<Slot>> GYM_SLOTS = new HashMap<>();
    // List: all bookings
    private static final List<Booking> BOOKINGS = new ArrayList<>();
    // List: notifications
    private static final List<Notification> NOTIFICATIONS = new ArrayList<>();
    // Set: slotIds for quick lookup
    private static final Set<String> SLOT_IDS = new HashSet<>();

    static {
        initAdmins();
        initOwners();
        initCustomers();
        initCentersAndSlots();
    }

    private static void initAdmins() {
        GymAdmin a = new GymAdmin();
        a.setAdminId("ADMIN1");
        a.setName("FlipFit Admin");
        a.setPassword("admin123");
        ADMINS.put(a.getAdminId(), a);
    }

    private static void initOwners() {
        GymOwner o1 = new GymOwner();
        o1.setId("OWN1");
        o1.setOwnerName("Bellandur Gym Owner");
        o1.setEmailId("owner.bellandur@flipfit.com");
        o1.setPassword("owner1");
        o1.setPanNo("ABCDE1234F");
        o1.setGstNo("29ABCDE1234F1Z5");
        o1.setValidated(true);
        OWNERS.put(o1.getEmailId(), o1);

        GymOwner o2 = new GymOwner();
        o2.setId("OWN2");
        o2.setOwnerName("Koramangala Gym Owner");
        o2.setEmailId("owner.koramangala@flipfit.com");
        o2.setPassword("owner2");
        o2.setPanNo("FGHIJ5678K");
        o2.setGstNo("29FGHIJ5678K1Z5");
        o2.setValidated(true);
        OWNERS.put(o2.getEmailId(), o2);

        GymOwner o3 = new GymOwner();
        o3.setId("OWN3");
        o3.setOwnerName("Indiranagar Gym Owner");
        o3.setEmailId("owner.indiranagar@flipfit.com");
        o3.setPassword("owner3");
        o3.setValidated(false);  // pending
        OWNERS.put(o3.getEmailId(), o3);
    }

    private static void initCustomers() {
        GymCustomer c1 = new GymCustomer();
        c1.setId("C1");
        c1.setName("Alice");
        c1.setEmail("alice@example.com");
        c1.setPassword("alice123");
        c1.setMobileNo("9876543210");
        c1.setAddress("Bangalore - Bellandur");
        CUSTOMERS.put(c1.getEmail(), c1);

        GymCustomer c2 = new GymCustomer();
        c2.setId("C2");
        c2.setName("Bob");
        c2.setEmail("bob@example.com");
        c2.setPassword("bob123");
        c2.setMobileNo("9876543211");
        c2.setAddress("Bangalore - Koramangala");
        CUSTOMERS.put(c2.getEmail(), c2);

        GymCustomer c3 = new GymCustomer();
        c3.setId("C3");
        c3.setName("Carol");
        c3.setEmail("carol@example.com");
        c3.setPassword("carol123");
        c3.setMobileNo("9876543212");
        c3.setAddress("Bangalore - Indiranagar");
        CUSTOMERS.put(c3.getEmail(), c3);
    }

    private static void initCentersAndSlots() {
        // Bellandur: 6-9am, 6-9pm; 5 seats each
        createCenter("BEL", "FlipFit Bellandur", "Bangalore - Bellandur", "080-12345678", "OWN1", true,
            LocalTime.of(6, 0), LocalTime.of(9, 0), LocalTime.of(18, 0), LocalTime.of(21, 0), 5);

        // Koramangala
        createCenter("KOR", "FlipFit Koramangala", "Bangalore - Koramangala", "080-12345679", "OWN2", true,
            LocalTime.of(6, 0), LocalTime.of(9, 0), LocalTime.of(18, 0), LocalTime.of(21, 0), 4);

        // Indiranagar (pending validation)
        createCenter("IND", "FlipFit Indiranagar", "Bangalore - Indiranagar", "080-12345680", "OWN3", false,
            LocalTime.of(6, 0), LocalTime.of(9, 0), LocalTime.of(18, 0), LocalTime.of(21, 0), 6);
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
        // Morning: 6-7, 7-8, 8-9
        for (int h = amStart.getHour(); h < amEnd.getHour(); h++) {
            Slot s = new Slot();
            String sid = gymId + "_" + String.format("%02d", h) + ":00";
            s.setSlotId(sid);
            s.setGymId(gymId);
            s.setStartTime(LocalTime.of(h, 0));
            s.setEndTime(LocalTime.of(h + 1, 0));
            s.setTotalCapacity(capacity);
            slots.add(s);
            SLOT_IDS.add(sid);
        }
        for (int h = pmStart.getHour(); h < pmEnd.getHour(); h++) {
            Slot s = new Slot();
            String sid = gymId + "_" + String.format("%02d", h) + ":00";
            s.setSlotId(sid);
            s.setGymId(gymId);
            s.setStartTime(LocalTime.of(h, 0));
            s.setEndTime(LocalTime.of(h + 1, 0));
            s.setTotalCapacity(capacity);
            slots.add(s);
            SLOT_IDS.add(sid);
        }
        GYM_SLOTS.put(gymId, slots);
        g.setSlotList(slots);
        CENTERS.put(gymId, g);
    }

    // --- Getters (unmodifiable where appropriate) ---

    public static Map<String, GymAdmin> getAdmins() { return Collections.unmodifiableMap(ADMINS); }
    public static Map<String, GymOwner> getOwners() { return Collections.unmodifiableMap(OWNERS); }
    public static Map<String, GymCustomer> getCustomers() { return Collections.unmodifiableMap(CUSTOMERS); }
    public static Map<String, GymCenter> getCenters() { return Collections.unmodifiableMap(CENTERS); }
    public static Map<String, List<Slot>> getGymSlots() { return Collections.unmodifiableMap(GYM_SLOTS); }
    public static Set<String> getSlotIds() { return Collections.unmodifiableSet(SLOT_IDS); }

    public static List<Booking> getBookings() { return BOOKINGS; }
    public static List<Notification> getNotifications() { return NOTIFICATIONS; }

    // Mutable access for services (add/remove)
    public static List<Booking> getBookingsMutable() { return BOOKINGS; }
    public static List<Notification> getNotificationsMutable() { return NOTIFICATIONS; }
    public static Map<String, GymCustomer> getCustomersMutable() { return CUSTOMERS; }
    public static Map<String, GymOwner> getOwnersMutable() { return OWNERS; }
    public static Map<String, GymCenter> getCentersMutable() { return CENTERS; }
    public static Map<String, List<Slot>> getGymSlotsMutable() { return GYM_SLOTS; }

    public static String nextBookingId() { return "B" + BOOKING_ID_GEN.incrementAndGet(); }
    public static String nextNotificationId() { return "N" + NOTIFICATION_ID_GEN.incrementAndGet(); }
}
