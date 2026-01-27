package com.flipfit.business;

import com.flipfit.bean.Booking;
import com.flipfit.bean.BookingStatus;
import com.flipfit.bean.GymCenter;
import com.flipfit.bean.GymCustomer;
import com.flipfit.bean.Slot;
import com.flipfit.constants.IdPrefixConstants;
import com.flipfit.data.DataStore;
import com.flipfit.exception.AlreadyExistsException;
import com.flipfit.exception.BookingException;
import com.flipfit.exception.ValidationException;
import com.flipfit.validation.CustomerValidation;
import com.flipfit.validation.ValidationResult;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class GymCustomerImpl implements GymCustomerInterface {
    private final GymCenterInterface centerService = new GymCenterImpl();
    private final BookingImpl bookingService = new BookingImpl();
    private final SlotImpl slotService = new SlotImpl();

    @Override
    public void signUp(GymCustomer customer) {
        ValidationResult vr = CustomerValidation.validateForSignUp(customer);
        if (!vr.isValid()) throw new ValidationException(vr.getMessage());
        if (DataStore.getCustomers().containsKey(customer.getEmail())) {
            throw new AlreadyExistsException("Email already registered.");
        }
        String id = IdPrefixConstants.CUSTOMER_PREFIX + (DataStore.getCustomers().size() + 1);
        customer.setId(id);
        DataStore.getCustomersMutable().put(customer.getEmail(), customer);
    }

    @Override
    public boolean signIn(String email, String password) {
        GymCustomer c = DataStore.getCustomers().get(email);
        return c != null && password != null && password.equals(c.getPassword());
    }

    @Override
    public List<GymCenter> searchGymsByCity(String city) {
        return centerService.getCentersByCity(city);
    }

    @Override
    public Booking bookSlot(String customerId, String slotId, LocalDate date) {
        Slot s = slotService.getSlot(slotId);
        boolean available = slotService.isSlotAvailable(slotId, date);
        if (available) {
            return bookingService.bookSlot(customerId, slotId, date);
        }
        return bookingService.addToWaitlist(customerId, slotId, date);
    }

    @Override
    public List<Booking> viewBookings(String customerId) {
        return bookingService.getBookingsByCustomer(customerId);
    }

    @Override
    public List<Booking> viewBookingsByDay(String customerId, LocalDate date) {
        return bookingService.getBookingsByCustomerAndDay(customerId, date);
    }

    @Override
    public void cancelBooking(String customerId, String bookingId) {
        Booking b = bookingService.getBooking(bookingId);
        if (!customerId.equals(b.getCustomerId())) {
            throw new BookingException("Cannot cancel: not your booking.");
        }
        bookingService.cancelBooking(bookingId);
    }

    @Override
    public Slot findNearestAvailableSlot(String customerId, String gymId, LocalDate date,
            java.time.LocalTime after) {
        return bookingService.findNearestAvailableSlot(customerId, gymId, date, after);
    }

    @Override
    public void modifyProfile(String customerId, String name, String mobile, String address) {
        for (GymCustomer c : DataStore.getCustomers().values()) {
            if (customerId.equals(c.getId()) || customerId.equals(c.getEmail())) {
                if (name != null) c.setName(name);
                if (mobile != null) c.setMobileNo(mobile);
                if (address != null) c.setAddress(address);
                return;
            }
        }
    }
}
