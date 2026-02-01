package com.flipfit.dao.impl;

import com.flipfit.bean.Notification;
import com.flipfit.dao.NotificationDAO;
import com.flipfit.util.DatabaseUtil;

import java.sql.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implementation of NotificationDAO interface
 * Provides database operations for notification entities
 */
public class NotificationDAOImpl implements NotificationDAO {
    
    private static final String INSERT_NOTIFICATION = 
        "INSERT INTO notifications (notification_id, customer_id, message, notification_timestamp, is_read, notification_type) VALUES (?, ?, ?, ?, ?, ?)";
    
    private static final String SELECT_NOTIFICATION_BY_ID = 
        "SELECT notification_id, customer_id, message, notification_timestamp, is_read, notification_type, created_at, updated_at FROM notifications WHERE notification_id = ?";
    
    private static final String SELECT_ALL_NOTIFICATIONS = 
        "SELECT notification_id, customer_id, message, notification_timestamp, is_read, notification_type, created_at, updated_at FROM notifications ORDER BY notification_timestamp DESC";
    
    private static final String SELECT_NOTIFICATIONS_BY_CUSTOMER = 
        "SELECT notification_id, customer_id, message, notification_timestamp, is_read, notification_type, created_at, updated_at FROM notifications WHERE customer_id = ? ORDER BY notification_timestamp DESC";
    
    private static final String SELECT_UNREAD_NOTIFICATIONS_BY_CUSTOMER = 
        "SELECT notification_id, customer_id, message, notification_timestamp, is_read, notification_type, created_at, updated_at FROM notifications WHERE customer_id = ? AND is_read = false ORDER BY notification_timestamp DESC";
    
    private static final String SELECT_NOTIFICATIONS_BY_CUSTOMER_AND_DATE_RANGE = 
        "SELECT notification_id, customer_id, message, notification_timestamp, is_read, notification_type, created_at, updated_at FROM notifications WHERE customer_id = ? AND notification_timestamp BETWEEN ? AND ? ORDER BY notification_timestamp DESC";
    
    private static final String UPDATE_NOTIFICATION = 
        "UPDATE notifications SET customer_id = ?, message = ?, is_read = ?, notification_type = ?, updated_at = CURRENT_TIMESTAMP WHERE notification_id = ?";
    
    private static final String MARK_AS_READ = 
        "UPDATE notifications SET is_read = true, updated_at = CURRENT_TIMESTAMP WHERE notification_id = ?";
    
    private static final String MARK_ALL_AS_READ_FOR_CUSTOMER = 
        "UPDATE notifications SET is_read = true, updated_at = CURRENT_TIMESTAMP WHERE customer_id = ? AND is_read = false";
    
    private static final String DELETE_NOTIFICATION = 
        "DELETE FROM notifications WHERE notification_id = ?";
    
    private static final String DELETE_NOTIFICATIONS_BY_CUSTOMER = 
        "DELETE FROM notifications WHERE customer_id = ?";
    
    private static final String CHECK_NOTIFICATION_EXISTS = 
        "SELECT COUNT(*) FROM notifications WHERE notification_id = ?";
    
    private static final String GET_NOTIFICATION_COUNT = 
        "SELECT COUNT(*) FROM notifications";
    
    private static final String GET_NOTIFICATION_COUNT_BY_CUSTOMER = 
        "SELECT COUNT(*) FROM notifications WHERE customer_id = ?";
    
    private static final String GET_UNREAD_NOTIFICATION_COUNT_BY_CUSTOMER = 
        "SELECT COUNT(*) FROM notifications WHERE customer_id = ? AND is_read = false";
    
    private static final String DELETE_OLD_NOTIFICATIONS = 
        "DELETE FROM notifications WHERE notification_timestamp < ?";
    
    @Override
    public boolean createNotification(Notification notification) {
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(INSERT_NOTIFICATION)) {
            
            pstmt.setString(1, notification.getNotificationId());
            pstmt.setString(2, notification.getCustomerId());
            pstmt.setString(3, notification.getMessage());
            pstmt.setTimestamp(4, Timestamp.from(notification.getTimestamp()));
            pstmt.setBoolean(5, notification.isRead());
            pstmt.setString(6, notification.getNotificationType() != null ? notification.getNotificationType() : "GENERAL");
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error creating notification: " + e.getMessage());
            return false;
        }
    }
    
    @Override
    public Optional<Notification> getNotificationById(String notificationId) {
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SELECT_NOTIFICATION_BY_ID)) {
            
            pstmt.setString(1, notificationId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToNotification(rs));
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting notification by ID: " + e.getMessage());
        }
        
        return Optional.empty();
    }
    
    @Override
    public List<Notification> getAllNotifications() {
        List<Notification> notifications = new ArrayList<>();
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SELECT_ALL_NOTIFICATIONS);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                notifications.add(mapResultSetToNotification(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting all notifications: " + e.getMessage());
        }
        
        return notifications;
    }
    
    @Override
    public List<Notification> getNotificationsByCustomer(String customerId) {
        List<Notification> notifications = new ArrayList<>();
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SELECT_NOTIFICATIONS_BY_CUSTOMER)) {
            
            pstmt.setString(1, customerId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    notifications.add(mapResultSetToNotification(rs));
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting notifications by customer: " + e.getMessage());
        }
        
        return notifications;
    }
    
    @Override
    public List<Notification> getUnreadNotificationsByCustomer(String customerId) {
        List<Notification> notifications = new ArrayList<>();
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SELECT_UNREAD_NOTIFICATIONS_BY_CUSTOMER)) {
            
            pstmt.setString(1, customerId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    notifications.add(mapResultSetToNotification(rs));
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting unread notifications by customer: " + e.getMessage());
        }
        
        return notifications;
    }
    
    @Override
    public List<Notification> getNotificationsByCustomerAndDateRange(String customerId, Instant startDate, Instant endDate) {
        List<Notification> notifications = new ArrayList<>();
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SELECT_NOTIFICATIONS_BY_CUSTOMER_AND_DATE_RANGE)) {
            
            pstmt.setString(1, customerId);
            pstmt.setTimestamp(2, Timestamp.from(startDate));
            pstmt.setTimestamp(3, Timestamp.from(endDate));
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    notifications.add(mapResultSetToNotification(rs));
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting notifications by customer and date range: " + e.getMessage());
        }
        
        return notifications;
    }
    
    @Override
    public boolean updateNotification(Notification notification) {
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(UPDATE_NOTIFICATION)) {
            
            pstmt.setString(1, notification.getCustomerId());
            pstmt.setString(2, notification.getMessage());
            pstmt.setBoolean(3, notification.isRead());
            pstmt.setString(4, notification.getNotificationType() != null ? notification.getNotificationType() : "GENERAL");
            pstmt.setString(5, notification.getNotificationId());
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error updating notification: " + e.getMessage());
            return false;
        }
    }
    
    @Override
    public boolean markAsRead(String notificationId) {
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(MARK_AS_READ)) {
            
            pstmt.setString(1, notificationId);
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error marking notification as read: " + e.getMessage());
            return false;
        }
    }
    
    @Override
    public int markAllAsReadForCustomer(String customerId) {
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(MARK_ALL_AS_READ_FOR_CUSTOMER)) {
            
            pstmt.setString(1, customerId);
            
            return pstmt.executeUpdate();
            
        } catch (SQLException e) {
            System.err.println("Error marking all notifications as read for customer: " + e.getMessage());
            return 0;
        }
    }
    
    @Override
    public boolean deleteNotification(String notificationId) {
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(DELETE_NOTIFICATION)) {
            
            pstmt.setString(1, notificationId);
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error deleting notification: " + e.getMessage());
            return false;
        }
    }
    
    @Override
    public int deleteNotificationsByCustomer(String customerId) {
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(DELETE_NOTIFICATIONS_BY_CUSTOMER)) {
            
            pstmt.setString(1, customerId);
            
            return pstmt.executeUpdate();
            
        } catch (SQLException e) {
            System.err.println("Error deleting notifications by customer: " + e.getMessage());
            return 0;
        }
    }
    
    @Override
    public boolean notificationExists(String notificationId) {
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(CHECK_NOTIFICATION_EXISTS)) {
            
            pstmt.setString(1, notificationId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
            
        } catch (SQLException e) {
            System.err.println("Error checking notification existence: " + e.getMessage());
            return false;
        }
    }
    
    @Override
    public int getNotificationCount() {
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(GET_NOTIFICATION_COUNT);
             ResultSet rs = pstmt.executeQuery()) {
            
            if (rs.next()) {
                return rs.getInt(1);
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting notification count: " + e.getMessage());
        }
        
        return 0;
    }
    
    @Override
    public int getNotificationCountByCustomer(String customerId) {
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(GET_NOTIFICATION_COUNT_BY_CUSTOMER)) {
            
            pstmt.setString(1, customerId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting notification count by customer: " + e.getMessage());
        }
        
        return 0;
    }
    
    @Override
    public int getUnreadNotificationCountByCustomer(String customerId) {
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(GET_UNREAD_NOTIFICATION_COUNT_BY_CUSTOMER)) {
            
            pstmt.setString(1, customerId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting unread notification count by customer: " + e.getMessage());
        }
        
        return 0;
    }
    
    @Override
    public int deleteOldNotifications(Instant beforeDate) {
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(DELETE_OLD_NOTIFICATIONS)) {
            
            pstmt.setTimestamp(1, Timestamp.from(beforeDate));
            
            return pstmt.executeUpdate();
            
        } catch (SQLException e) {
            System.err.println("Error deleting old notifications: " + e.getMessage());
            return 0;
        }
    }
    
    /**
     * Helper method to map ResultSet to Notification object
     */
    private Notification mapResultSetToNotification(ResultSet rs) throws SQLException {
        Notification notification = new Notification();
        notification.setNotificationId(rs.getString("notification_id"));
        notification.setCustomerId(rs.getString("customer_id"));
        notification.setMessage(rs.getString("message"));
        notification.setTimestamp(rs.getTimestamp("notification_timestamp").toInstant());
        notification.setRead(rs.getBoolean("is_read"));
        notification.setNotificationType(rs.getString("notification_type"));
        return notification;
    }
}
