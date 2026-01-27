package com.flipfit.business;

import com.flipfit.bean.Notification;
import com.flipfit.data.DataStore;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class NotificationImpl implements NotificationInterface {

    @Override
    public void sendNotification(String customerId, String message) {
        Notification n = new Notification();
        n.setNotificationId(DataStore.nextNotificationId());
        n.setCustomerId(customerId);
        n.setMessage(message);
        n.setTimestamp(Instant.now());
        n.setRead(false);
        DataStore.getNotificationsMutable().add(n);
    }

    @Override
    public void markAsRead(String notificationId) {
        for (Notification n : DataStore.getNotifications()) {
            if (notificationId.equals(n.getNotificationId())) {
                n.setRead(true);
                return;
            }
        }
    }

    public List<Notification> getNotificationsForCustomer(String customerId) {
        List<Notification> out = new ArrayList<>();
        for (Notification n : DataStore.getNotifications()) {
            if (customerId.equals(n.getCustomerId())) out.add(n);
        }
        return out;
    }
}
