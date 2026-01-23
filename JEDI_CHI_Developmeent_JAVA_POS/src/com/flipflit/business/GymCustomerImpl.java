/**
 * 
 */
package com.flipflit.business;

import java.util.List;

/**
 * 
 */
public class GymCustomerImpl implements GymCustomerInterface{

	@Override
	public boolean signUp(GymCustomerImpl customer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean signIn(String email, String password) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<GymCenterImpl> searchGyms(String location) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BookingImpl bookSlots(String customerId, String slotId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<BookingImpl> viewBookings(String customerId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void cancelBooking(String bookingId) {
		// TODO Auto-generated method stub
		
	}

}
