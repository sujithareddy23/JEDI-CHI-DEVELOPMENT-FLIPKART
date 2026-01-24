package com.flipfit.client;

import com.flipfit.bean.Booking;
import com.flipfit.bean.BookingStatus;
import com.flipfit.bean.GymCenter;
import com.flipfit.bean.GymCustomer;
import com.flipfit.bean.Notification;
import com.flipfit.bean.Slot;
import com.flipfit.business.GymCenterImpl;
import com.flipfit.business.GymCenterInterface;
import com.flipfit.business.GymCustomerImpl;
import com.flipfit.business.GymCustomerInterface;
import com.flipfit.business.NotificationImpl;
import com.flipfit.constants.DemoDataConstants;
import com.flipfit.constants.FormatConstants;
import com.flipfit.validation.CustomerValidation;
import com.flipfit.validation.InputValidation;
import com.flipfit.validation.ValidationResult;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class CustomerClient {
    private final GymCustomerInterface customerService = new GymCustomerImpl();
    private final NotificationImpl notificationService = new NotificationImpl();
    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern(FormatConstants.DATE_PATTERN);
    private static final DateTimeFormatter TIME_FMT = DateTimeFormatter.ofPattern(FormatConstants.TIME_PATTERN);

    public void customerRegistration(Scanner in) {
        System.out.println("\n--- Customer Registration ---");
        System.out.print("Name: ");
        String name = in.nextLine().trim();
        System.out.print("Email: ");
        String email = in.next().trim();
        System.out.print("Password: ");
        String password = in.next().trim();
        System.out.print("Mobile: ");
        String mobile = in.next().trim();
        in.nextLine();
        System.out.print("Address (e.g. Bangalore - Bellandur): ");
        String address = in.nextLine().trim();

        GymCustomer c = new GymCustomer();
        c.setName(name);
        c.setEmail(email);
        c.setPassword(password);
        c.setMobileNo(mobile);
        c.setAddress(address);
        ValidationResult vr = CustomerValidation.validateForSignUp(c);
        if (!vr.isValid()) {
            System.out.println("Validation failed: " + vr.getMessage());
            return;
        }
        if (customerService.signUp(c)) {
            System.out.println("Registration successful. You can now login with email: " + email);
        } else {
            System.out.println("Registration failed. Email may already exist.");
        }
    }

    public void customerMenu(Scanner in, String customerId) {
        int choice;
        do {
            System.out.println("\n--- Customer Menu ---");
            System.out.println("1. View all centers (by city)");
            System.out.println("2. View slot availability for a center and date");
            System.out.println("3. Book a slot");
            System.out.println("4. View my bookings");
            System.out.println("5. View my plan by day");
            System.out.println("6. Cancel a booking");
            System.out.println("7. Find nearest available slot");
            System.out.println("8. View notifications");
            System.out.println("9. Modify profile");
            System.out.println("0. Logout");
            System.out.print("Choice: ");
            choice = readInt(in);
            in.nextLine();

            switch (choice) {
                case 1:
                    viewCentersByCity(in);
                    break;
                case 2:
                    viewSlotAvailability(in);
                    break;
                case 3:
                    bookSlot(in, customerId);
                    break;
                case 4:
                    viewBookings(customerId);
                    break;
                case 5:
                    viewPlanByDay(in, customerId);
                    break;
                case 6:
                    cancelBooking(in, customerId);
                    break;
                case 7:
                    findNearestSlot(in, customerId);
                    break;
                case 8:
                    viewNotifications(customerId);
                    break;
                case 9:
                    modifyProfile(in, customerId);
                    break;
                case 0:
                    System.out.println("Logged out.");
                    return;
                default:
                    System.out.println("Invalid choice.");
            }
        } while (choice != 0);
    }

    private void viewCentersByCity(Scanner in) {
        System.out.print("Enter city (e.g. Bangalore): ");
        String city = in.nextLine().trim();
        List<GymCenter> centers = customerService.searchGymsByCity(city);
        if (centers.isEmpty()) {
            System.out.println("No centers found for: " + city);
            return;
        }
        System.out.println("\nCenters in " + city + ":");
        for (GymCenter g : centers) {
            System.out.println("  " + g.getGymId() + " | " + g.getName() + " | " + g.getLocation()
                + " | Contact: " + g.getContactNo());
        }
    }

    private void viewSlotAvailability(Scanner in) {
        System.out.print("Gym ID (e.g. " + DemoDataConstants.GYM_ID_EXAMPLES + "): ");
        String gymId = in.next().trim();
        System.out.print("Date (" + FormatConstants.DATE_PATTERN + "): ");
        String dateStr = in.next().trim();
        ValidationResult dr = InputValidation.validateDate(dateStr);
        if (!dr.isValid()) {
            System.out.println(dr.getFirstError());
            return;
        }
        LocalDate date = LocalDate.parse(dateStr, DATE_FMT);
        GymCenterInterface gci = new GymCenterImpl();
        Map<Slot, Integer> avail = gci.getSlotAvailabilityForDate(gymId, date);
        if (avail.isEmpty()) {
            System.out.println("No slots for this gym or invalid gym ID.");
            return;
        }
        System.out.println("\nSlot availability for " + gymId + " on " + date + ":");
        for (Map.Entry<Slot, Integer> e : avail.entrySet()) {
            Slot s = e.getKey();
            int free = e.getValue();
            System.out.println("  " + s.getSlotId() + " | " + s.getStartTime() + "-" + s.getEndTime()
                + " | Seats available: " + free + "/" + s.getTotalCapacity());
        }
    }

    private void bookSlot(Scanner in, String customerId) {
        System.out.print("Slot ID (e.g. " + DemoDataConstants.SLOT_ID_EXAMPLE + "): ");
        String slotId = in.next().trim();
        ValidationResult sr = InputValidation.isNotBlank(slotId, "Slot ID");
        if (!sr.isValid()) { System.out.println(sr.getFirstError()); return; }
        System.out.print("Date (" + FormatConstants.DATE_PATTERN + "): ");
        String dateStr = in.next().trim();
        ValidationResult dr = InputValidation.validateDateNotInPast(dateStr);
        if (!dr.isValid()) { System.out.println(dr.getFirstError()); return; }
        LocalDate date = LocalDate.parse(dateStr, DATE_FMT);
        Booking b = customerService.bookSlot(customerId, slotId, date);
        if (b == null) {
            System.out.println("Booking failed. Invalid slot or error.");
            return;
        }
        if (b.getStatus() == BookingStatus.WAITLISTED) {
            System.out.println("Slot is full. You have been added to the waitlist. Booking ID: " + b.getId());
        } else {
            System.out.println("Booked successfully. Booking ID: " + b.getId());
        }
    }

    private void viewBookings(String customerId) {
        List<Booking> list = customerService.viewBookings(customerId);
        if (list.isEmpty()) {
            System.out.println("No bookings.");
            return;
        }
        System.out.println("\nYour bookings:");
        for (Booking b : list) {
            System.out.println("  " + b.getId() + " | " + b.getBookingDate() + " | " + b.getGymId()
                + " | " + b.getSlotId() + " | " + b.getStatus());
        }
    }

    private void viewPlanByDay(Scanner in, String customerId) {
        System.out.print("Date (" + FormatConstants.DATE_PATTERN + "): ");
        String dateStr = in.next().trim();
        ValidationResult dr = InputValidation.validateDate(dateStr);
        if (!dr.isValid()) { System.out.println(dr.getFirstError()); return; }
        LocalDate date = LocalDate.parse(dateStr, DATE_FMT);
        List<Booking> list = customerService.viewBookingsByDay(customerId, date);
        if (list.isEmpty()) {
            System.out.println("No bookings for " + date + ".");
            return;
        }
        System.out.println("\nPlan for " + date + ":");
        for (Booking b : list) {
            System.out.println("  " + b.getGymId() + " | " + b.getSlotId() + " | "
                + b.getSlotStartTime() + "-" + b.getSlotEndTime() + " | " + b.getStatus());
        }
    }

    private void cancelBooking(Scanner in, String customerId) {
        viewBookings(customerId);
        System.out.print("Booking ID to cancel: ");
        String bid = in.next().trim();
        if (customerService.cancelBooking(customerId, bid)) {
            System.out.println("Booking cancelled.");
        } else {
            System.out.println("Booking not found or you cannot cancel it.");
        }
    }

    private void findNearestSlot(Scanner in, String customerId) {
        System.out.print("Gym ID: ");
        String gymId = in.next().trim();
        System.out.print("Date (" + FormatConstants.DATE_PATTERN + "): ");
        String dateStr = in.next().trim();
        System.out.print("After time (" + FormatConstants.TIME_PATTERN + ", e.g. 07:00): ");
        String timeStr = in.next().trim();
        ValidationResult dr = InputValidation.validateDate(dateStr);
        if (!dr.isValid()) { System.out.println(dr.getFirstError()); return; }
        ValidationResult tr = InputValidation.validateTime(timeStr);
        if (!tr.isValid()) { System.out.println(tr.getFirstError()); return; }
        LocalDate date = LocalDate.parse(dateStr, DATE_FMT);
        LocalTime after = LocalTime.parse(timeStr, TIME_FMT);
        Slot s = customerService.findNearestAvailableSlot(customerId, gymId, date, after);
        if (s == null) {
            System.out.println("No available slot found.");
            return;
        }
        System.out.println("Nearest available: " + s.getSlotId() + " | " + s.getStartTime() + "-" + s.getEndTime());
    }

    private void viewNotifications(String customerId) {
        List<Notification> list = notificationService.getNotificationsForCustomer(customerId);
        if (list.isEmpty()) {
            System.out.println("No notifications.");
            return;
        }
        for (Notification n : list) {
            System.out.println("  [" + n.getTimestamp() + "] " + n.getMessage());
        }
    }

    private void modifyProfile(Scanner in, String customerId) {
        in.nextLine();
        System.out.print("Name (leave blank to skip): ");
        String name = in.nextLine().trim();
        System.out.print("Mobile (leave blank to skip): ");
        String mobile = in.nextLine().trim();
        System.out.print("Address (leave blank to skip): ");
        String address = in.nextLine().trim();
        ValidationResult vr = CustomerValidation.validateForProfileUpdate(name, mobile, address);
        if (!vr.isValid()) {
            System.out.println("Validation failed: " + vr.getMessage());
            return;
        }
        customerService.modifyProfile(customerId, name.isEmpty() ? null : name,
            mobile.isEmpty() ? null : mobile, address.isEmpty() ? null : address);
        System.out.println("Profile updated.");
    }

    private static int readInt(Scanner in) {
        try {
            return in.nextInt();
        } catch (Exception e) {
            return -1;
        }
    }
}
