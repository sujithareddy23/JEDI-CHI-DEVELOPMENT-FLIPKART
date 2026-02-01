package com.flipfit.bean;

import java.util.List;

public class GymCenter {
    private String gymId;
    private String name;
    private String location;  // city or area, e.g. "Bangalore - Bellandur"
    private String contactNo;
    private String ownerId;
    private boolean validated;
    private List<Slot> slotList;

    public String getGymId() { return gymId; }
    public void setGymId(String gymId) { this.gymId = gymId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getContactNo() { return contactNo; }
    public void setContactNo(String contactNo) { this.contactNo = contactNo; }

    public String getOwnerId() { return ownerId; }
    public void setOwnerId(String ownerId) { this.ownerId = ownerId; }

    public boolean isValidated() { return validated; }
    public void setValidated(boolean validated) { this.validated = validated; }

    public List<Slot> getSlotList() { return slotList; }
    public void setSlotList(List<Slot> slotList) { this.slotList = slotList; }
}
