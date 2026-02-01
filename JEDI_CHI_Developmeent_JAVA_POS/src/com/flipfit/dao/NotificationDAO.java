package com.flipfit.dao;

import com.flipfit.bean.Notification;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

/**
 * Data Access Object interface for Notification operations
 * Provides CRUD operations for notification entities
 */
public interface NotificationDAO {
    
    /**
     * Create a new notification in the database
     * @param notification The notification object to create
     * @return true if creation successful, false otherwise
     */
    boolean createNotification(Notification notification);
    
    /**
     * Retrieve a notification by its ID
     * @param notificationId The notification ID to search for
     * @return Optional containing the notification if found, empty otherwise
     */
    Optional<Notification> getNotificationById(String notificationId);
    
    /**
     * Retrieve all notifications from the database
     * @return List of all notifications
     */
    List<Notification> getAllNotifications();
    
    /**
     * Retrieve notifications by customer ID
     * @param customerId The customer ID to filter by
     * @return List of notifications for the specified customer
     */
    List<Notification> getNotificationsByCustomer(String customerId);
    
    /**
     * Retrieve unread notifications by customer ID
     * @param customerId The customer ID to filter by
     * @return List of unread notifications for the specified customer
     */
    List<Notification> getUnreadNotificationsByCustomer(String customerId);
    
    /**
     * Retrieve notifications by customer ID and date range
     * @param customerId The customer ID
     * @param startDate The start date
     * @param endDate The end date
     * @return List of notifications within the date range
     */
    List<Notification> getNotificationsByCustomerAndDateRange(String customerId, Instant startDate, Instant endDate);
    
    /**
     * Update an existing notification
     * @param notification The notification object with updated information
     * @return true if update successful, false otherwise
     */
    boolean updateNotification(Notification notification);
    
    /**
     * Mark a notification as read
     * @param notificationId The notification ID to mark as read
     * @return true if update successful, false otherwise
     */
    boolean markAsRead(String notificationId);
    
    /**
     * Mark all notifications as read for a customer
     * @param customerId The customer ID
     * @return Number of notifications marked as read
     */
    int markAllAsReadForCustomer(String customerId);
    
    /**
     * Delete a notification by its ID
     * @param notificationId The notification ID to delete
     * @return true if deletion successful, false otherwise
     */
    boolean deleteNotification(String notificationId);
    
    /**
     * Delete all notifications for a customer
     * @param customerId The customer ID
     * @return Number of notifications deleted
     */
    int deleteNotificationsByCustomer(String customerId);
    
    /**
     * Check if a notification exists by ID
     * @param notificationId The notification ID to check
     * @return true if notification exists, false otherwise
     */
    boolean notificationExists(String notificationId);
    
    /**
     * Get the total count of notifications
     * @return Number of notifications in the database
     */
    int getNotificationCount();
    
    /**
     * Get the count of notifications by customer
     * @param customerId The customer ID
     * @return Number of notifications for the specified customer
     */
    int getNotificationCountByCustomer(String customerId);
    
    /**
     * Get the count of unread notifications by customer
     * @param customerId The customer ID
     * @return Number of unread notifications for the specified customer
     */
    int getUnreadNotificationCountByCustomer(String customerId);
    
    /**
     * Delete old notifications (older than specified date)
     * @param beforeDate The date before which notifications should be deleted
     * @return Number of notifications deleted
     */
    int deleteOldNotifications(Instant beforeDate);
}
