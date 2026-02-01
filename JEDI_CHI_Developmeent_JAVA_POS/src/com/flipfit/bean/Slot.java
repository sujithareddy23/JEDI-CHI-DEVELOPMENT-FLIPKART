package com.flipfit.bean;

import javax.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.LocalTime;

@Entity
@Table(name = "slots")
public class Slot {
    @Id
    @Column(name = "slot_id")
    private String slotId;
    
    @Column(name = "gym_id")
    private String gymId;
    
    @Column(name = "total_capacity")
    private int totalCapacity;
    
    @Column(name = "start_time")
    private LocalTime startTime;
    
    @Column(name = "end_time")
    private LocalTime endTime;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gym_id", insertable = false, updatable = false)
    @JsonIgnore
    private GymCenter gymCenter;

    public String getSlotId() { return slotId; }
    public void setSlotId(String slotId) { this.slotId = slotId; }

    public String getGymId() { return gymId; }
    public void setGymId(String gymId) { this.gymId = gymId; }

    public int getTotalCapacity() { return totalCapacity; }
    public void setTotalCapacity(int totalCapacity) { this.totalCapacity = totalCapacity; }

    public LocalTime getStartTime() { return startTime; }
    public void setStartTime(LocalTime startTime) { this.startTime = startTime; }

    public LocalTime getEndTime() { return endTime; }
    public void setEndTime(LocalTime endTime) { this.endTime = endTime; }
    
    public GymCenter getGymCenter() { return gymCenter; }
    public void setGymCenter(GymCenter gymCenter) { this.gymCenter = gymCenter; }
}