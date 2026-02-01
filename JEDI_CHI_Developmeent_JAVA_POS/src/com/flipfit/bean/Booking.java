package com.flipfit.bean;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "bookings")
public class Booking {
    @Id
    @Column(name = "id")
    private String id;
    
    @Column(name = "booking_date")
    private LocalDate bookingDate;
    
    @Column(name = "status_id")
    private int statusId;
    
    @Column(name = "customer_id")
    private String customerId;
    
    @Column(name = "slot_id")
    private String slotId;
    
    @Column(name = "gym_id")
    private String gymId;
    
    @Column(name = "slot_start_time")
    private java.time.LocalTime slotStartTime;
    
    @Column(name = "slot_end_time")
    private java.time.LocalTime slotEndTime;
    
    @Column(name = "payment_status")
    private String paymentStatus;
    
    @Column(name = "payment_type")
    private String paymentType;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public LocalDate getBookingDate() { return bookingDate; }
    public void setBookingDate(LocalDate bookingDate) { this.bookingDate = bookingDate; }

    public int getStatusId() { return statusId; }
    public void setStatusId(int statusId) { this.statusId = statusId; }

    public String getCustomerId() { return customerId; }
    public void setCustomerId(String customerId) { this.customerId = customerId; }

    public String getSlotId() { return slotId; }
    public void setSlotId(String slotId) { this.slotId = slotId; }

    public String getGymId() { return gymId; }
    public void setGymId(String gymId) { this.gymId = gymId; }

    public java.time.LocalTime getSlotStartTime() { return slotStartTime; }
    public void setSlotStartTime(java.time.LocalTime slotStartTime) { this.slotStartTime = slotStartTime; }

    public java.time.LocalTime getSlotEndTime() { return slotEndTime; }
    public void setSlotEndTime(java.time.LocalTime slotEndTime) { this.slotEndTime = slotEndTime; }

    public String getPaymentStatus() { return paymentStatus; }
    public void setPaymentStatus(String paymentStatus) { this.paymentStatus = paymentStatus; }

    public String getPaymentType() { return paymentType; }
    public void setPaymentType(String paymentType) { this.paymentType = paymentType; }
}