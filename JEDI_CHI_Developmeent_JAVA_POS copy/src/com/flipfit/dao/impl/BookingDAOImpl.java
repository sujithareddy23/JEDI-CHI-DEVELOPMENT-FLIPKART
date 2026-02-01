package com.flipfit.dao.impl;

import com.flipfit.bean.Booking;
import com.flipfit.dao.BookingDAO;
import com.flipfit.util.DatabaseUtil;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implementation of BookingDAO interface
 * Provides database operations for booking entities
 */
public class BookingDAOImpl implements BookingDAO {
    
    private static final String INSERT_BOOKING = 
        "INSERT INTO bookings (id, booking_date, status_id, customer_id, slot_id, gym_id, slot_start_time, slot_end_time, payment_status, payment_type) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    
    private static final String SELECT_BOOKING_BY_ID = 
        "SELECT b.id, b.booking_date, b.status_id, bs.status_name, b.customer_id, b.slot_id, b.gym_id, b.slot_start_time, b.slot_end_time, b.booking_time, b.payment_status, b.payment_type, b.created_at, b.updated_at FROM bookings b JOIN booking_status bs ON b.status_id = bs.status_id WHERE b.id = ?";
    
    private static final String SELECT_ALL_BOOKINGS = 
        "SELECT b.id, b.booking_date, b.status_id, bs.status_name, b.customer_id, b.slot_id, b.gym_id, b.slot_start_time, b.slot_end_time, b.booking_time, b.payment_status, b.payment_type, b.created_at, b.updated_at FROM bookings b JOIN booking_status bs ON b.status_id = bs.status_id ORDER BY b.booking_time DESC";
    
    private static final String SELECT_BOOKINGS_BY_CUSTOMER = 
        "SELECT b.id, b.booking_date, b.status_id, bs.status_name, b.customer_id, b.slot_id, b.gym_id, b.slot_start_time, b.slot_end_time, b.booking_time, b.payment_status, b.payment_type, b.created_at, b.updated_at FROM bookings b JOIN booking_status bs ON b.status_id = bs.status_id WHERE b.customer_id = ? ORDER BY b.booking_date DESC";
    
    private static final String SELECT_BOOKINGS_BY_CUSTOMER_AND_DATE = 
        "SELECT b.id, b.booking_date, b.status_id, bs.status_name, b.customer_id, b.slot_id, b.gym_id, b.slot_start_time, b.slot_end_time, b.booking_time, b.payment_status, b.payment_type, b.created_at, b.updated_at FROM bookings b JOIN booking_status bs ON b.status_id = bs.status_id WHERE b.customer_id = ? AND b.booking_date = ? ORDER BY b.slot_start_time";
    
    private static final String SELECT_BOOKINGS_BY_SLOT = 
        "SELECT b.id, b.booking_date, b.status_id, bs.status_name, b.customer_id, b.slot_id, b.gym_id, b.slot_start_time, b.slot_end_time, b.booking_time, b.payment_status, b.payment_type, b.created_at, b.updated_at FROM bookings b JOIN booking_status bs ON b.status_id = bs.status_id WHERE b.slot_id = ? ORDER BY b.booking_date DESC";
    
    private static final String SELECT_BOOKINGS_BY_SLOT_AND_DATE = 
        "SELECT b.id, b.booking_date, b.status_id, bs.status_name, b.customer_id, b.slot_id, b.gym_id, b.slot_start_time, b.slot_end_time, b.booking_time, b.payment_status, b.payment_type, b.created_at, b.updated_at FROM bookings b JOIN booking_status bs ON b.status_id = bs.status_id WHERE b.slot_id = ? AND b.booking_date = ? ORDER BY b.booking_time";
    
    private static final String SELECT_BOOKINGS_BY_GYM = 
        "SELECT b.id, b.booking_date, b.status_id, bs.status_name, b.customer_id, b.slot_id, b.gym_id, b.slot_start_time, b.slot_end_time, b.booking_time, b.payment_status, b.payment_type, b.created_at, b.updated_at FROM bookings b JOIN booking_status bs ON b.status_id = bs.status_id WHERE b.gym_id = ? ORDER BY b.booking_date DESC";
    
    private static final String SELECT_BOOKINGS_BY_STATUS = 
        "SELECT b.id, b.booking_date, b.status_id, bs.status_name, b.customer_id, b.slot_id, b.gym_id, b.slot_start_time, b.slot_end_time, b.booking_time, b.payment_status, b.payment_type, b.created_at, b.updated_at FROM bookings b JOIN booking_status bs ON b.status_id = bs.status_id WHERE bs.status_name = ? ORDER BY b.booking_time DESC";
    
    private static final String SELECT_BOOKINGS_BY_DATE_RANGE = 
        "SELECT b.id, b.booking_date, b.status_id, bs.status_name, b.customer_id, b.slot_id, b.gym_id, b.slot_start_time, b.slot_end_time, b.booking_time, b.payment_status, b.payment_type, b.created_at, b.updated_at FROM bookings b JOIN booking_status bs ON b.status_id = bs.status_id WHERE b.booking_date BETWEEN ? AND ? ORDER BY b.booking_date DESC";
    
    private static final String UPDATE_BOOKING = 
        "UPDATE bookings SET booking_date = ?, status_id = ?, customer_id = ?, slot_id = ?, gym_id = ?, slot_start_time = ?, slot_end_time = ?, payment_status = ?, payment_type = ?, updated_at = CURRENT_TIMESTAMP WHERE id = ?";
    
    private static final String DELETE_BOOKING = 
        "DELETE FROM bookings WHERE id = ?";
    
    private static final String CHECK_BOOKING_EXISTS = 
        "SELECT COUNT(*) FROM bookings WHERE id = ?";
    
    private static final String GET_BOOKING_COUNT = 
        "SELECT COUNT(*) FROM bookings";
    
    private static final String GET_BOOKING_COUNT_BY_CUSTOMER = 
        "SELECT COUNT(*) FROM bookings WHERE customer_id = ?";
    
    private static final String GET_BOOKING_COUNT_BY_SLOT_AND_DATE = 
        "SELECT COUNT(*) FROM bookings WHERE slot_id = ? AND booking_date = ?";
    
    private static final String GET_CONFIRMED_BOOKING_COUNT_BY_SLOT_AND_DATE = 
        "SELECT COUNT(*) FROM bookings b JOIN booking_status bs ON b.status_id = bs.status_id WHERE b.slot_id = ? AND b.booking_date = ? AND bs.status_name = 'CONFIRMED'";
    
    private static final String UPDATE_BOOKING_STATUS = 
        "UPDATE bookings SET status_id = (SELECT status_id FROM booking_status WHERE status_name = ?), updated_at = CURRENT_TIMESTAMP WHERE id = ?";
    
    private static final String SELECT_WAITLISTED_BOOKINGS_BY_SLOT_AND_DATE = 
        "SELECT b.id, b.booking_date, b.status_id, bs.status_name, b.customer_id, b.slot_id, b.gym_id, b.slot_start_time, b.slot_end_time, b.booking_time, b.payment_status, b.payment_type, b.created_at, b.updated_at FROM bookings b JOIN booking_status bs ON b.status_id = bs.status_id WHERE b.slot_id = ? AND b.booking_date = ? AND bs.status_name = 'WAITLISTED' ORDER BY b.booking_time ASC";
    
    private static final String SELECT_EARLIEST_WAITLISTED_BOOKING = 
        "SELECT b.id, b.booking_date, b.status_id, bs.status_name, b.customer_id, b.slot_id, b.gym_id, b.slot_start_time, b.slot_end_time, b.booking_time, b.payment_status, b.payment_type, b.created_at, b.updated_at FROM bookings b JOIN booking_status bs ON b.status_id = bs.status_id WHERE b.slot_id = ? AND b.booking_date = ? AND bs.status_name = 'WAITLISTED' ORDER BY b.booking_time ASC LIMIT 1";
    
    @Override
    public boolean createBooking(Booking booking) {
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(INSERT_BOOKING)) {
            
            pstmt.setString(1, booking.getId());
            pstmt.setDate(2, Date.valueOf(booking.getBookingDate()));
            pstmt.setInt(3, getStatusId(booking.getStatus().toString()));
            pstmt.setString(4, booking.getCustomerId());
            pstmt.setString(5, booking.getSlotId());
            pstmt.setString(6, booking.getGymId());
            pstmt.setTime(7, Time.valueOf(booking.getSlotStartTime()));
            pstmt.setTime(8, Time.valueOf(booking.getSlotEndTime()));
            pstmt.setString(9, booking.getPaymentStatus() != null ? booking.getPaymentStatus() : "PENDING");
            pstmt.setString(10, booking.getPaymentType());
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error creating booking: " + e.getMessage());
            return false;
        }
    }
    
    @Override
    public Optional<Booking> getBookingById(String bookingId) {
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SELECT_BOOKING_BY_ID)) {
            
            pstmt.setString(1, bookingId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToBooking(rs));
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting booking by ID: " + e.getMessage());
        }
        
        return Optional.empty();
    }
    
    @Override
    public List<Booking> getAllBookings() {
        List<Booking> bookings = new ArrayList<>();
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SELECT_ALL_BOOKINGS);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                bookings.add(mapResultSetToBooking(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting all bookings: " + e.getMessage());
        }
        
        return bookings;
    }
    
    @Override
    public List<Booking> getBookingsByCustomer(String customerId) {
        List<Booking> bookings = new ArrayList<>();
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SELECT_BOOKINGS_BY_CUSTOMER)) {
            
            pstmt.setString(1, customerId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    bookings.add(mapResultSetToBooking(rs));
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting bookings by customer: " + e.getMessage());
        }
        
        return bookings;
    }
    
    @Override
    public List<Booking> getBookingsByCustomerAndDate(String customerId, LocalDate date) {
        List<Booking> bookings = new ArrayList<>();
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SELECT_BOOKINGS_BY_CUSTOMER_AND_DATE)) {
            
            pstmt.setString(1, customerId);
            pstmt.setDate(2, Date.valueOf(date));
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    bookings.add(mapResultSetToBooking(rs));
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting bookings by customer and date: " + e.getMessage());
        }
        
        return bookings;
    }
    
    @Override
    public List<Booking> getBookingsBySlot(String slotId) {
        List<Booking> bookings = new ArrayList<>();
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SELECT_BOOKINGS_BY_SLOT)) {
            
            pstmt.setString(1, slotId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    bookings.add(mapResultSetToBooking(rs));
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting bookings by slot: " + e.getMessage());
        }
        
        return bookings;
    }
    
    @Override
    public List<Booking> getBookingsBySlotAndDate(String slotId, LocalDate date) {
        List<Booking> bookings = new ArrayList<>();
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SELECT_BOOKINGS_BY_SLOT_AND_DATE)) {
            
            pstmt.setString(1, slotId);
            pstmt.setDate(2, Date.valueOf(date));
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    bookings.add(mapResultSetToBooking(rs));
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting bookings by slot and date: " + e.getMessage());
        }
        
        return bookings;
    }
    
    @Override
    public List<Booking> getBookingsByGym(String gymId) {
        List<Booking> bookings = new ArrayList<>();
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SELECT_BOOKINGS_BY_GYM)) {
            
            pstmt.setString(1, gymId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    bookings.add(mapResultSetToBooking(rs));
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting bookings by gym: " + e.getMessage());
        }
        
        return bookings;
    }
    
    @Override
    public List<Booking> getBookingsByStatus(String status) {
        List<Booking> bookings = new ArrayList<>();
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SELECT_BOOKINGS_BY_STATUS)) {
            
            pstmt.setString(1, status);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    bookings.add(mapResultSetToBooking(rs));
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting bookings by status: " + e.getMessage());
        }
        
        return bookings;
    }
    
    @Override
    public List<Booking> getBookingsByDateRange(LocalDate startDate, LocalDate endDate) {
        List<Booking> bookings = new ArrayList<>();
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SELECT_BOOKINGS_BY_DATE_RANGE)) {
            
            pstmt.setDate(1, Date.valueOf(startDate));
            pstmt.setDate(2, Date.valueOf(endDate));
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    bookings.add(mapResultSetToBooking(rs));
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting bookings by date range: " + e.getMessage());
        }
        
        return bookings;
    }
    
    @Override
    public boolean updateBooking(Booking booking) {
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(UPDATE_BOOKING)) {
            
            pstmt.setDate(1, Date.valueOf(booking.getBookingDate()));
            pstmt.setInt(2, getStatusId(booking.getStatus().toString()));
            pstmt.setString(3, booking.getCustomerId());
            pstmt.setString(4, booking.getSlotId());
            pstmt.setString(5, booking.getGymId());
            pstmt.setTime(6, Time.valueOf(booking.getSlotStartTime()));
            pstmt.setTime(7, Time.valueOf(booking.getSlotEndTime()));
            pstmt.setString(8, booking.getPaymentStatus() != null ? booking.getPaymentStatus() : "PENDING");
            pstmt.setString(9, booking.getPaymentType());
            pstmt.setString(10, booking.getId());
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error updating booking: " + e.getMessage());
            return false;
        }
    }
    
    @Override
    public boolean deleteBooking(String bookingId) {
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(DELETE_BOOKING)) {
            
            pstmt.setString(1, bookingId);
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error deleting booking: " + e.getMessage());
            return false;
        }
    }
    
    @Override
    public boolean bookingExists(String bookingId) {
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(CHECK_BOOKING_EXISTS)) {
            
            pstmt.setString(1, bookingId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
            
        } catch (SQLException e) {
            System.err.println("Error checking booking existence: " + e.getMessage());
            return false;
        }
    }
    
    @Override
    public int getBookingCount() {
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(GET_BOOKING_COUNT);
             ResultSet rs = pstmt.executeQuery()) {
            
            if (rs.next()) {
                return rs.getInt(1);
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting booking count: " + e.getMessage());
        }
        
        return 0;
    }
    
    @Override
    public int getBookingCountByCustomer(String customerId) {
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(GET_BOOKING_COUNT_BY_CUSTOMER)) {
            
            pstmt.setString(1, customerId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting booking count by customer: " + e.getMessage());
        }
        
        return 0;
    }
    
    @Override
    public int getBookingCountBySlotAndDate(String slotId, LocalDate date) {
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(GET_BOOKING_COUNT_BY_SLOT_AND_DATE)) {
            
            pstmt.setString(1, slotId);
            pstmt.setDate(2, Date.valueOf(date));
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting booking count by slot and date: " + e.getMessage());
        }
        
        return 0;
    }
    
    @Override
    public int getConfirmedBookingCountBySlotAndDate(String slotId, LocalDate date) {
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(GET_CONFIRMED_BOOKING_COUNT_BY_SLOT_AND_DATE)) {
            
            pstmt.setString(1, slotId);
            pstmt.setDate(2, Date.valueOf(date));
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting confirmed booking count by slot and date: " + e.getMessage());
        }
        
        return 0;
    }
    
    @Override
    public boolean updateBookingStatus(String bookingId, String status) {
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(UPDATE_BOOKING_STATUS)) {
            
            pstmt.setString(1, status);
            pstmt.setString(2, bookingId);
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error updating booking status: " + e.getMessage());
            return false;
        }
    }
    
    @Override
    public List<Booking> getWaitlistedBookingsBySlotAndDate(String slotId, LocalDate date) {
        List<Booking> bookings = new ArrayList<>();
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SELECT_WAITLISTED_BOOKINGS_BY_SLOT_AND_DATE)) {
            
            pstmt.setString(1, slotId);
            pstmt.setDate(2, Date.valueOf(date));
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    bookings.add(mapResultSetToBooking(rs));
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting waitlisted bookings: " + e.getMessage());
        }
        
        return bookings;
    }
    
    @Override
    public Optional<Booking> getEarliestWaitlistedBooking(String slotId, LocalDate date) {
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SELECT_EARLIEST_WAITLISTED_BOOKING)) {
            
            pstmt.setString(1, slotId);
            pstmt.setDate(2, Date.valueOf(date));
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToBooking(rs));
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting earliest waitlisted booking: " + e.getMessage());
        }
        
        return Optional.empty();
    }
    
    /**
     * Helper method to get status ID from status name
     */
    private int getStatusId(String statusName) {
        switch (statusName.toUpperCase()) {
            case "CONFIRMED":
                return 1;
            case "CANCELLED":
                return 2;
            case "WAITLISTED":
                return 3;
            default:
                return 3; // Default to WAITLISTED
        }
    }
    
    /**
     * Helper method to map ResultSet to Booking object
     */
    private Booking mapResultSetToBooking(ResultSet rs) throws SQLException {
        Booking booking = new Booking();
        booking.setId(rs.getString("id"));
        booking.setBookingDate(rs.getDate("booking_date").toLocalDate());
        
        // Set status from status_name
        String statusName = rs.getString("status_name");
        switch (statusName) {
            case "CONFIRMED":
                booking.setStatus(com.flipfit.bean.BookingStatus.CONFIRMED);
                break;
            case "CANCELLED":
                booking.setStatus(com.flipfit.bean.BookingStatus.CANCELLED);
                break;
            case "WAITLISTED":
                booking.setStatus(com.flipfit.bean.BookingStatus.WAITLISTED);
                break;
        }
        
        booking.setCustomerId(rs.getString("customer_id"));
        booking.setSlotId(rs.getString("slot_id"));
        booking.setGymId(rs.getString("gym_id"));
        booking.setSlotStartTime(rs.getTime("slot_start_time").toLocalTime());
        booking.setSlotEndTime(rs.getTime("slot_end_time").toLocalTime());
        booking.setPaymentStatus(rs.getString("payment_status"));
        booking.setPaymentType(rs.getString("payment_type"));
        
        return booking;
    }
}
