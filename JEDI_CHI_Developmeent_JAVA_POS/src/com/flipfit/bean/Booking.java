package com.flipfit.bean;

import java.util.Date;

public class Booking {
    private String id;
    private Date bookingDate;
    private Booking status;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public Date getBookingDate() { return bookingDate; }
    public void setBookingDate(Date bookingDate) { this.bookingDate = bookingDate; }

    public Booking getStatus() { return status; }
    public void setStatus(Booking status) { this.status = status; }
}