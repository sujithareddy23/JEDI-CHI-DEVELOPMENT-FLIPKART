package com.flipfit.business;

import com.flipfit.bean.Booking;
import com.flipfit.bean.GymCenter;
import com.flipfit.bean.GymCustomer;
import com.flipfit.bean.Slot;
import com.flipfit.constants.IdPrefixConstants;
import com.flipfit.dao.GymCustomerDAO;
import com.flipfit.dao.impl.GymCustomerDAOImpl;
import com.flipfit.exception.AlreadyExistsException;
import com.flipfit.exception.BookingException;
import com.flipfit.exception.ValidationException;
import com.flipfit.validation.CustomerValidation;
import com.flipfit.validation.ValidationResult;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class GymCustomerImpl implements GymCustomerInterface {
    private final GymCenterInterface centerService = new GymCenterImpl();
    private final BookingImpl bookingService = new BookingImpl();
    private final SlotImpl slotService = new SlotImpl();
    private final GymCustomerDAO customerDAO;

    public GymCustomerImpl() {
        this.customerDAO = new GymCustomerDAOImpl();
    }

    public GymCustomerImpl(GymCustomerDAO customerDAO) {
        this.customerDAO = customerDAO;
    }

    @Override
    public void signUp(GymCustomer customer) {
        ValidationResult vr = CustomerValidation.validateForSignUp(customer);
        if (!vr.isValid()) throw new ValidationException(vr.getMessage());
        
        if (customerDAO.customerExistsByEmail(customer.getEmail())) {
            throw new AlreadyExistsException("Email already registered.");
        }
        
        // Generate unique ID
        String id = IdPrefixConstants.CUSTOMER_PREFIX + (customerDAO.getCustomerCount() + 1);
        customer.setId(id);
        
        if (!customerDAO.createCustomer(customer)) {
            throw new ValidationException("Failed to create customer account.");
        }
    }

    @Override
    public boolean signIn(String email, String password) {
        return customerDAO.validateCredentials(email, password).isPresent();
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
        Optional<GymCustomer> customerOpt = customerDAO.getCustomerById(customerId);
        if (!customerOpt.isPresent()) {
            // Try by email if ID lookup fails
            customerOpt = customerDAO.getCustomerByEmail(customerId);
        }
        
        if (customerOpt.isPresent()) {
            GymCustomer customer = customerOpt.get();
            if (name != null) customer.setName(name);
            if (mobile != null) customer.setMobileNo(mobile);
            if (address != null) customer.setAddress(address);
            
            if (!customerDAO.updateCustomer(customer)) {
                throw new ValidationException("Failed to update customer profile.");
            }
        } else {
            throw new ValidationException("Customer not found.");
        }
    }

    @Override
    public boolean createCustomer(GymCustomer customer) {
        return customerDAO.createCustomer(customer);
    }

    @Override
    public GymCustomer getCustomer(String customerId) {
        return customerDAO.getCustomerById(customerId).orElse(null);
    }

    @Override
    public boolean updateCustomer(GymCustomer customer) {
        return customerDAO.updateCustomer(customer);
    }

    @Override
    public List<GymCustomer> getAllCustomers() {
        return customerDAO.getAllCustomers();
    }
}
