package com.flipfit.business;

import com.flipfit.bean.Booking;
import com.flipfit.bean.GymCenter;
import com.flipfit.bean.GymCustomer;
import com.flipfit.bean.Slot;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface GymCustomerInterface {
    void signUp(GymCustomer customer);
    boolean signIn(String email, String password);
    List<GymCenter> searchGymsByCity(String city);
    Booking bookSlot(String customerId, String slotId, LocalDate date);
    List<Booking> viewBookings(String customerId);
    List<Booking> viewBookingsByDay(String customerId, LocalDate date);
    void cancelBooking(String customerId, String bookingId);
    Slot findNearestAvailableSlot(String customerId, String gymId, LocalDate date, java.time.LocalTime after);
    void modifyProfile(String customerId, String name, String mobile, String address);
	boolean createCustomer(GymCustomer customer);
	GymCustomer getCustomer(String customerId);
	boolean updateCustomer(GymCustomer customer);
	List<GymCustomer> getAllCustomers();
}
