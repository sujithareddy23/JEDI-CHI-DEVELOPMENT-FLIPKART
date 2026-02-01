package com.flipfit.business;

import com.flipfit.bean.Booking;
import com.flipfit.bean.BookingStatus;
import com.flipfit.bean.Slot;
import com.flipfit.constants.MessageConstants;
import com.flipfit.dao.BookingDAO;
import com.flipfit.dao.SlotDAO;
import com.flipfit.dao.impl.BookingDAOImpl;
import com.flipfit.dao.impl.SlotDAOImpl;
import com.flipfit.exception.NotFoundException;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class BookingImpl implements BookingInterface {
    private final SlotInterface slotService;
    private final BookingDAO bookingDAO;
    private final SlotDAO slotDAO;

    public BookingImpl() {
        this.slotService = new SlotImpl();
        this.bookingDAO = new BookingDAOImpl();
        this.slotDAO = new SlotDAOImpl();
    }

    public BookingImpl(SlotInterface slotService, BookingDAO bookingDAO, SlotDAO slotDAO) {
        this.slotService = slotService;
        this.bookingDAO = bookingDAO;
        this.slotDAO = slotDAO;
    }

    @Override
    public Booking bookSlot(String customerId, String slotId, LocalDate date) {
        Optional<Slot> slotOpt = slotDAO.getSlotById(slotId);
        if (!slotOpt.isPresent()) {
            throw new NotFoundException("Slot not found: " + slotId);
        }
        
        Slot slot = slotOpt.get();
        String gymId = slot.getGymId();
        int bookedCount = bookingDAO.getConfirmedBookingCountBySlotAndDate(slotId, date);
        
        if (bookedCount >= slot.getTotalCapacity()) {
            return null; // caller should check and show "slot full" or add to waitlist
        }
        
        cancelSameSlotOtherGym(customerId, date, slot.getStartTime(), slot.getEndTime(), null);
        
        Booking booking = new Booking();
        booking.setId(generateBookingId());
        booking.setCustomerId(customerId);
        booking.setSlotId(slotId);
        booking.setGymId(gymId);
        booking.setBookingDate(date);
        booking.setStatus(BookingStatus.CONFIRMED);
        booking.setSlotStartTime(slot.getStartTime());
        booking.setSlotEndTime(slot.getEndTime());
        booking.setPaymentStatus("COMPLETED"); // Auto-complete since payment not needed
        
        if (bookingDAO.createBooking(booking)) {
            return booking;
        }
        
        return null;
    }

    /**
     * Cancel customer's confirmed booking for same date and time range at any other gym.
     */
    private void cancelSameSlotOtherGym(String customerId, LocalDate date,
            java.time.LocalTime start, java.time.LocalTime end, String excludeBookingId) {
        List<Booking> customerBookings = bookingDAO.getBookingsByCustomerAndDate(customerId, date);
        for (Booking booking : customerBookings) {
            if (!BookingStatus.CONFIRMED.equals(booking.getStatus())) continue;
            if (booking.getSlotStartTime() == null || booking.getSlotEndTime() == null) continue;
            if (!booking.getSlotStartTime().equals(start) || !booking.getSlotEndTime().equals(end)) continue;
            if (excludeBookingId != null && excludeBookingId.equals(booking.getId())) continue;
            bookingDAO.updateBookingStatus(booking.getId(), "CANCELLED");
        }
    }

    @Override
    public void cancelBooking(String bookingId) {
        Optional<Booking> bookingOpt = bookingDAO.getBookingById(bookingId);
        if (!bookingOpt.isPresent()) {
            throw new NotFoundException("Booking not found: " + bookingId);
        }
        
        Booking booking = bookingOpt.get();
        bookingDAO.updateBookingStatus(bookingId, "CANCELLED");
        promoteFirstWaitlisted(booking.getSlotId(), booking.getBookingDate());
    }

    private void promoteFirstWaitlisted(String slotId, LocalDate date) {
        Optional<Booking> waitlistedOpt = bookingDAO.getEarliestWaitlistedBooking(slotId, date);
        if (waitlistedOpt.isPresent()) {
            Booking waitlisted = waitlistedOpt.get();
            bookingDAO.updateBookingStatus(waitlisted.getId(), "CONFIRMED");
            
            // Send notification (would need NotificationDAO implementation)
            // For now, using the existing notification service
            new NotificationImpl().sendNotification(waitlisted.getCustomerId(),
                String.format(MessageConstants.WAITLIST_PROMOTED, waitlisted.getId(), date, slotId));
        }
    }

    @Override
    public void choosePaymentMode(String bookingId, String paymentType) {
        // Payment is not needed - auto-complete all bookings
        Optional<Booking> bookingOpt = bookingDAO.getBookingById(bookingId);
        if (bookingOpt.isPresent()) {
            Booking booking = bookingOpt.get();
            booking.setPaymentType("FREE");
            booking.setPaymentStatus("COMPLETED");
            bookingDAO.updateBooking(booking);
        }
    }

    @Override
    public List<Booking> getBookingsByCustomer(String customerId) {
        return bookingDAO.getBookingsByCustomer(customerId);
    }

    @Override
    public List<Booking> getBookingsByCustomerAndDay(String customerId, LocalDate date) {
        return bookingDAO.getBookingsByCustomerAndDate(customerId, date);
    }

    @Override
    public Booking getBooking(String bookingId) {
        Optional<Booking> bookingOpt = bookingDAO.getBookingById(bookingId);
        if (!bookingOpt.isPresent()) {
            throw new NotFoundException("Booking not found: " + bookingId);
        }
        return bookingOpt.get();
    }

    /**
     * Add to waitlist when slot is full. Returns the waitlisted booking.
     */
    public Booking addToWaitlist(String customerId, String slotId, LocalDate date) {
        Optional<Slot> slotOpt = slotDAO.getSlotById(slotId);
        if (!slotOpt.isPresent()) {
            throw new NotFoundException("Slot not found: " + slotId);
        }
        
        Slot slot = slotOpt.get();
        Booking booking = new Booking();
        booking.setId(generateBookingId());
        booking.setCustomerId(customerId);
        booking.setSlotId(slotId);
        booking.setGymId(slot.getGymId());
        booking.setBookingDate(date);
        booking.setStatus(BookingStatus.WAITLISTED);
        booking.setSlotStartTime(slot.getStartTime());
        booking.setSlotEndTime(slot.getEndTime());
        booking.setPaymentStatus("COMPLETED"); // Auto-complete since payment not needed
        
        if (bookingDAO.createBooking(booking)) {
            return booking;
        }
        
        return null;
    }

    /**
     * Find nearest available time slot for same date, same center, considering customer's other bookings.
     */
    public Slot findNearestAvailableSlot(String customerId, String gymId, LocalDate date,
            java.time.LocalTime after) {
        List<Slot> slots = slotDAO.getActiveSlotsByGym(gymId);
        Slot best = null;
        long bestMinutes = Long.MAX_VALUE;
        
        for (Slot slot : slots) {
            if (slot.getStartTime().isBefore(after)) continue;
            if (!slotService.isSlotAvailable(slot.getSlotId(), date)) continue;
            if (hasOverlapWithCustomerBookings(customerId, date, slot.getStartTime(), slot.getEndTime())) continue;
            long min = slot.getStartTime().toSecondOfDay() / 60L - after.toSecondOfDay() / 60L;
            if (min < bestMinutes) {
                bestMinutes = min;
                best = slot;
            }
        }
        return best;
    }

    private boolean hasOverlapWithCustomerBookings(String customerId, LocalDate date,
            java.time.LocalTime start, java.time.LocalTime end) {
        List<Booking> customerBookings = bookingDAO.getBookingsByCustomerAndDate(customerId, date);
        for (Booking booking : customerBookings) {
            if (!BookingStatus.CONFIRMED.equals(booking.getStatus())) continue;
            if (booking.getSlotStartTime() == null || booking.getSlotEndTime() == null) continue;
            if (start.isBefore(booking.getSlotEndTime()) && end.isAfter(booking.getSlotStartTime())) return true;
        }
        return false;
    }
    
    /**
     * Generate a unique booking ID
     */
    private String generateBookingId() {
        return "BK" + System.currentTimeMillis();
    }
}
