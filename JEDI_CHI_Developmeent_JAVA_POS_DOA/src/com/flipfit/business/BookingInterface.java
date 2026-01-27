package com.flipfit.business;

import com.flipfit.bean.Booking;
import java.time.LocalDate;
import java.util.List;

public interface BookingInterface {
    Booking bookSlot(String customerId, String slotId, LocalDate date);
    void cancelBooking(String bookingId);
    void choosePaymentMode(String bookingId, String paymentType);
    List<Booking> getBookingsByCustomer(String customerId);
    List<Booking> getBookingsByCustomerAndDay(String customerId, LocalDate date);
    Booking getBooking(String bookingId);
}
