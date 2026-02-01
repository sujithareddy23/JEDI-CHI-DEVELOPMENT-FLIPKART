package com.flipfit.bean;

import javax.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.List;

@Entity
@Table(name = "gym_centers")
public class GymCenter {
    @Id
    @Column(name = "gym_id")
    private String gymId;
    
    @Column(name = "name")
    private String name;
    
    @Column(name = "location")
    private String location;  // city or area, e.g. "Bangalore - Bellandur"
    
    @Column(name = "contact_no")
    private String contactNo;
    
    @Column(name = "owner_id")
    private String ownerId;
    
    @Column(name = "validated")
    private boolean validated;
    
    @OneToMany(mappedBy = "gymCenter", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
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
