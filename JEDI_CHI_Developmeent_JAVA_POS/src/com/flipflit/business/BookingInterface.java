/**
 * 
 */
package com.flipflit.business;

/**
 * 
 */
public interface BookingInterface {
	
	BookingImpl bookSlots(String userId, String slotId, String date);

    
    void choosePaymentMode(String bookingId, String paymentType);

    
    

}
