package com.flipfit.bean;

import java.time.LocalDate;

public class Booking {
    private String id;
    private LocalDate bookingDate;
    private BookingStatus status;
    private String customerId;
    private String slotId;
    private String gymId;
    private java.time.LocalTime slotStartTime;
    private java.time.LocalTime slotEndTime;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public LocalDate getBookingDate() { return bookingDate; }
    public void setBookingDate(LocalDate bookingDate) { this.bookingDate = bookingDate; }

    public BookingStatus getStatus() { return status; }
    public void setStatus(BookingStatus status) { this.status = status; }

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
}