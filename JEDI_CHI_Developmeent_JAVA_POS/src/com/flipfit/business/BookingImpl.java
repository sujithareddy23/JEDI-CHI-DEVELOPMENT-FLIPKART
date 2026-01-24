package com.flipfit.business;

import com.flipfit.bean.Booking;
import com.flipfit.bean.BookingStatus;
import com.flipfit.bean.Notification;
import com.flipfit.bean.Slot;
import com.flipfit.constants.MessageConstants;
import com.flipfit.data.DataStore;
import com.flipfit.exception.NotFoundException;

import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class BookingImpl implements BookingInterface {
    private final SlotInterface slotService = new SlotImpl();
    private final NotificationImpl notificationService = new NotificationImpl();

    @Override
    public Booking bookSlot(String customerId, String slotId, LocalDate date) {
        Slot s = slotService.getSlot(slotId);
        String gymId = s.getGymId();
        int booked = slotService.getBookedCount(slotId, date);
        if (booked >= s.getTotalCapacity()) {
            return null; // caller should check and show "slot full" or add to waitlist
        }
        cancelSameSlotOtherGym(customerId, date, s.getStartTime(), s.getEndTime(), null);
        Booking b = new Booking();
        b.setId(DataStore.nextBookingId());
        b.setCustomerId(customerId);
        b.setSlotId(slotId);
        b.setGymId(gymId);
        b.setBookingDate(date);
        b.setStatus(BookingStatus.CONFIRMED);
        b.setSlotStartTime(s.getStartTime());
        b.setSlotEndTime(s.getEndTime());
        DataStore.getBookingsMutable().add(b);
        return b;
    }

    /**
     * Cancel customer's confirmed booking for same date and time range at any other gym.
     */
    private void cancelSameSlotOtherGym(String customerId, LocalDate date,
            java.time.LocalTime start, java.time.LocalTime end, String excludeBookingId) {
        for (Booking x : DataStore.getBookings()) {
            if (!BookingStatus.CONFIRMED.equals(x.getStatus())) continue;
            if (!customerId.equals(x.getCustomerId())) continue;
            if (!date.equals(x.getBookingDate())) continue;
            if (x.getSlotStartTime() == null || x.getSlotEndTime() == null) continue;
            if (!x.getSlotStartTime().equals(start) || !x.getSlotEndTime().equals(end)) continue;
            if (excludeBookingId != null && excludeBookingId.equals(x.getId())) continue;
            x.setStatus(BookingStatus.CANCELLED);
        }
    }

    @Override
    public void cancelBooking(String bookingId) {
        Booking b = getBooking(bookingId);
        b.setStatus(BookingStatus.CANCELLED);
        promoteFirstWaitlisted(b.getSlotId(), b.getBookingDate());
    }

    private void promoteFirstWaitlisted(String slotId, LocalDate date) {
        Booking waitlisted = null;
        for (Booking w : DataStore.getBookings()) {
            if (BookingStatus.WAITLISTED.equals(w.getStatus())
                    && slotId.equals(w.getSlotId())
                    && date.equals(w.getBookingDate())) {
                waitlisted = w;
                break;
            }
        }
        if (waitlisted != null) {
            waitlisted.setStatus(BookingStatus.CONFIRMED);
            notificationService.sendNotification(waitlisted.getCustomerId(),
                String.format(MessageConstants.WAITLIST_PROMOTED, waitlisted.getId(), date, slotId));
        }
    }

    @Override
    public void choosePaymentMode(String bookingId, String paymentType) {
        Booking b = getBooking(bookingId);
        if (b != null) {
            // Could extend Booking with paymentType field if needed
        }
    }

    @Override
    public List<Booking> getBookingsByCustomer(String customerId) {
        List<Booking> out = new ArrayList<>();
        for (Booking b : DataStore.getBookings()) {
            if (customerId.equals(b.getCustomerId()) && !BookingStatus.CANCELLED.equals(b.getStatus())) {
                out.add(b);
            }
        }
        return out;
    }

    @Override
    public List<Booking> getBookingsByCustomerAndDay(String customerId, LocalDate date) {
        List<Booking> out = new ArrayList<>();
        for (Booking b : DataStore.getBookings()) {
            if (customerId.equals(b.getCustomerId()) && date.equals(b.getBookingDate())
                    && !BookingStatus.CANCELLED.equals(b.getStatus())) {
                out.add(b);
            }
        }
        return out;
    }

    @Override
    public Booking getBooking(String bookingId) {
        for (Booking b : DataStore.getBookings()) {
            if (bookingId.equals(b.getId())) return b;
        }
        throw new NotFoundException("Booking not found: " + bookingId);
    }

    /**
     * Add to waitlist when slot is full. Returns the waitlisted booking.
     */
    public Booking addToWaitlist(String customerId, String slotId, LocalDate date) {
        Slot s = slotService.getSlot(slotId);
        Booking b = new Booking();
        b.setId(DataStore.nextBookingId());
        b.setCustomerId(customerId);
        b.setSlotId(slotId);
        b.setGymId(s.getGymId());
        b.setBookingDate(date);
        b.setStatus(BookingStatus.WAITLISTED);
        b.setSlotStartTime(s.getStartTime());
        b.setSlotEndTime(s.getEndTime());
        DataStore.getBookingsMutable().add(b);
        return b;
    }

    /**
     * Find nearest available time slot for same date, same center, considering customer's other bookings.
     */
    public Slot findNearestAvailableSlot(String customerId, String gymId, LocalDate date,
            java.time.LocalTime after) {
        List<Slot> slots = slotService.getSlotsByGym(gymId);
        Slot best = null;
        long bestMinutes = Long.MAX_VALUE;
        for (Slot s : slots) {
            if (s.getStartTime().isBefore(after)) continue;
            if (!slotService.isSlotAvailable(s.getSlotId(), date)) continue;
            if (hasOverlapWithCustomerBookings(customerId, date, s.getStartTime(), s.getEndTime())) continue;
            long min = s.getStartTime().toSecondOfDay() / 60L - after.toSecondOfDay() / 60L;
            if (min < bestMinutes) {
                bestMinutes = min;
                best = s;
            }
        }
        return best;
    }

    private boolean hasOverlapWithCustomerBookings(String customerId, LocalDate date,
            java.time.LocalTime start, java.time.LocalTime end) {
        for (Booking b : DataStore.getBookings()) {
            if (!customerId.equals(b.getCustomerId()) || !BookingStatus.CONFIRMED.equals(b.getStatus())) continue;
            if (!date.equals(b.getBookingDate())) continue;
            if (b.getSlotStartTime() == null || b.getSlotEndTime() == null) continue;
            if (start.isBefore(b.getSlotEndTime()) && end.isAfter(b.getSlotStartTime())) return true;
        }
        return false;
    }
}
