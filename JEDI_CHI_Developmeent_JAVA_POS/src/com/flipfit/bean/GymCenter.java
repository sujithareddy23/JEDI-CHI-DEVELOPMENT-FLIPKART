package com.flipfit.bean;

import java.util.List;

public class GymCenter {
    private String gymId;
    private String name;
    private String location;
    private String contactNo;
    private List<Slot> slotList;
    private List<GymCustomer> userList;

    public String getGymId() { return gymId; }
    public void setGymId(String gymId) { this.gymId = gymId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getContactNo() { return contactNo; }
    public void setContactNo(String contactNo) { this.contactNo = contactNo; }

    public List<Slot> getSlotList() { return slotList; }
    public void setSlotList(List<Slot> slotList) { this.slotList = slotList; }

    public List<GymCustomer> getUserList() { return userList; }
    public void setUserList(List<GymCustomer> userList) { this.userList = userList; }
}
