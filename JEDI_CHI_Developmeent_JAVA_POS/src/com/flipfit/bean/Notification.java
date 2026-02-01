package com.flipfit.bean;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "notifications")
public class Notification {
    @Id
    @Column(name = "notification_id")
    private String notificationId;
    
    @Column(name = "customer_id")
    private String customerId;
    
    @Column(name = "message")
    private String message;
    
    @Column(name = "timestamp")
    private Instant timestamp;
    
    @Column(name = "is_read")
    private boolean read;

    public String getNotificationId() { return notificationId; }
    public void setNotificationId(String notificationId) { this.notificationId = notificationId; }

    public String getCustomerId() { return customerId; }
    public void setCustomerId(String customerId) { this.customerId = customerId; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public Instant getTimestamp() { return timestamp; }
    public void setTimestamp(Instant timestamp) { this.timestamp = timestamp; }

    public boolean isRead() { return read; }
    public void setRead(boolean read) { this.read = read; }
}