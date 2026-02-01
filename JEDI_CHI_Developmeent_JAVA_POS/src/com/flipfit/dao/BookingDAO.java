package com.flipfit.dao;

import com.flipfit.bean.Booking;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Data Access Object interface for Booking operations
 * Provides CRUD operations for booking entities
 */
public interface BookingDAO {
    
    /**
     * Create a new booking in the database
     * @param booking The booking object to create
     * @return true if creation successful, false otherwise
     */
    boolean createBooking(Booking booking);
    
    /**
     * Retrieve a booking by its ID
     * @param bookingId The booking ID to search for
     * @return Optional containing the booking if found, empty otherwise
     */
    Optional<Booking> getBookingById(String bookingId);
    
    /**
     * Retrieve all bookings from the database
     * @return List of all bookings
     */
    List<Booking> getAllBookings();
    
    /**
     * Retrieve bookings by customer ID
     * @param customerId The customer ID to filter by
     * @return List of bookings for the specified customer
     */
    List<Booking> getBookingsByCustomer(String customerId);
    
    /**
     * Retrieve bookings by customer and date
     * @param customerId The customer ID
     * @param date The booking date
     * @return List of bookings for the customer on the specified date
     */
    List<Booking> getBookingsByCustomerAndDate(String customerId, LocalDate date);
    
    /**
     * Retrieve bookings by slot ID
     * @param slotId The slot ID to filter by
     * @return List of bookings for the specified slot
     */
    List<Booking> getBookingsBySlot(String slotId);
    
    /**
     * Retrieve bookings by slot and date
     * @param slotId The slot ID
     * @param date The booking date
     * @return List of bookings for the slot on the specified date
     */
    List<Booking> getBookingsBySlotAndDate(String slotId, LocalDate date);
    
    /**
     * Retrieve bookings by gym ID
     * @param gymId The gym ID to filter by
     * @return List of bookings for the specified gym
     */
    List<Booking> getBookingsByGym(String gymId);
    
    /**
     * Retrieve bookings by status
     * @param status The booking status to filter by
     * @return List of bookings with the specified status
     */
    List<Booking> getBookingsByStatus(String status);
    
    /**
     * Retrieve bookings by date range
     * @param startDate The start date
     * @param endDate The end date
     * @return List of bookings within the date range
     */
    List<Booking> getBookingsByDateRange(LocalDate startDate, LocalDate endDate);
    
    /**
     * Update an existing booking
     * @param booking The booking object with updated information
     * @return true if update successful, false otherwise
     */
    boolean updateBooking(Booking booking);
    
    /**
     * Delete a booking by its ID
     * @param bookingId The booking ID to delete
     * @return true if deletion successful, false otherwise
     */
    boolean deleteBooking(String bookingId);
    
    /**
     * Check if a booking exists by ID
     * @param bookingId The booking ID to check
     * @return true if booking exists, false otherwise
     */
    boolean bookingExists(String bookingId);
    
    /**
     * Get the total count of bookings
     * @return Number of bookings in the database
     */
    int getBookingCount();
    
    /**
     * Get the count of bookings by customer
     * @param customerId The customer ID
     * @return Number of bookings for the specified customer
     */
    int getBookingCountByCustomer(String customerId);
    
    /**
     * Get the count of bookings by slot and date
     * @param slotId The slot ID
     * @param date The booking date
     * @return Number of bookings for the slot on the specified date
     */
    int getBookingCountBySlotAndDate(String slotId, LocalDate date);
    
    /**
     * Get the count of confirmed bookings by slot and date
     * @param slotId The slot ID
     * @param date The booking date
     * @return Number of confirmed bookings for the slot on the specified date
     */
    int getConfirmedBookingCountBySlotAndDate(String slotId, LocalDate date);
    
    /**
     * Update booking status
     * @param bookingId The booking ID to update
     * @param status The new status
     * @return true if update successful, false otherwise
     */
    boolean updateBookingStatus(String bookingId, String status);
    
    /**
     * Get waitlisted bookings by slot and date
     * @param slotId The slot ID
     * @param date The booking date
     * @return List of waitlisted bookings ordered by booking time
     */
    List<Booking> getWaitlistedBookingsBySlotAndDate(String slotId, LocalDate date);
    
    /**
     * Get the earliest waitlisted booking for a slot and date
     * @param slotId The slot ID
     * @param date The booking date
     * @return Optional containing the earliest waitlisted booking if found, empty otherwise
     */
    Optional<Booking> getEarliestWaitlistedBooking(String slotId, LocalDate date);
}
