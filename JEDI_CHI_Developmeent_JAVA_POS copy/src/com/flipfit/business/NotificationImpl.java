package com.flipfit.business;

import com.flipfit.bean.Notification;
import com.flipfit.dao.NotificationDAO;
import com.flipfit.dao.impl.NotificationDAOImpl;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public class NotificationImpl implements NotificationInterface {
    private final NotificationDAO notificationDAO;

    public NotificationImpl() {
        this.notificationDAO = new NotificationDAOImpl();
    }

    public NotificationImpl(NotificationDAO notificationDAO) {
        this.notificationDAO = notificationDAO;
    }

    @Override
    public void sendNotification(String customerId, String message) {
        Notification notification = new Notification();
        notification.setNotificationId(generateNotificationId());
        notification.setCustomerId(customerId);
        notification.setMessage(message);
        notification.setTimestamp(Instant.now());
        notification.setRead(false);
        notification.setNotificationType("GENERAL");
        
        notificationDAO.createNotification(notification);
    }

    @Override
    public void markAsRead(String notificationId) {
        notificationDAO.markAsRead(notificationId);
    }

    public List<Notification> getNotificationsForCustomer(String customerId) {
        return notificationDAO.getNotificationsByCustomer(customerId);
    }

    /**
     * Generate a unique notification ID
     */
    private String generateNotificationId() {
        return "NT" + System.currentTimeMillis();
    }
}
