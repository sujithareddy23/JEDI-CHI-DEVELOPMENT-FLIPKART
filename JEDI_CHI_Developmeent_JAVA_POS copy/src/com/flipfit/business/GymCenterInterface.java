package com.flipfit.business;

import com.flipfit.bean.GymCenter;
import com.flipfit.bean.Slot;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface GymCenterInterface {
    List<GymCenter> getCentersByCity(String city);
    List<Slot> getAvailableSlots(String gymId);
    Map<Slot, Integer> getSlotAvailabilityForDate(String gymId, LocalDate date);
    GymCenter getCenter(String gymId);
    
    // Additional methods needed for DAO integration
    boolean createCenter(GymCenter center);
    boolean updateCenter(GymCenter center);
    boolean deleteCenter(String gymId);
    List<GymCenter> getAllCenters();
    List<GymCenter> getCentersByOwner(String ownerId);
    List<GymCenter> getCentersByValidation(boolean validated);
    List<GymCenter> getCentersByLocation(String location);
}
