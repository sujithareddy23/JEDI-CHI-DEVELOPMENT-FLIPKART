/**
 * 
 */
package com.flipfit.business;

import java.util.List;

/**
 * 
 */
public interface GymCustomerInterface {
	boolean signUp(GymCustomerImpl customer);
    boolean signIn(String email, String password);
    List<GymCenterImpl> searchGyms(String location);
    BookingImpl bookSlots(String customerId, String slotId);
    List<BookingImpl> viewBookings(String customerId);
    void cancelBooking(String bookingId);

}
