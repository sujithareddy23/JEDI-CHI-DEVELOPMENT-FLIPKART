package com.flipfit.business;

import com.flipfit.bean.GymCenter;
import com.flipfit.bean.Slot;
import com.flipfit.data.DataStore;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class GymCenterImpl implements GymCenterInterface {
    private final SlotInterface slotService = new SlotImpl();

    @Override
    public List<GymCenter> getCentersByCity(String city) {
        if (city == null) return new ArrayList<>();
        List<GymCenter> out = new ArrayList<>();
        for (GymCenter g : DataStore.getCenters().values()) {
            if (g.getLocation() != null && g.getLocation().toLowerCase().contains(city.toLowerCase().trim())
                    && g.isValidated()) {
                out.add(g);
            }
        }
        return out;
    }

    @Override
    public List<Slot> getAvailableSlots(String gymId) {
        return slotService.getSlotsByGym(gymId);
    }

    @Override
    public Map<Slot, Integer> getSlotAvailabilityForDate(String gymId, LocalDate date) {
        Map<Slot, Integer> map = new LinkedHashMap<>();
        List<Slot> slots = slotService.getSlotsByGym(gymId);
        for (Slot s : slots) {
            int booked = slotService.getBookedCount(s.getSlotId(), date);
            int free = Math.max(0, s.getTotalCapacity() - booked);
            map.put(s, free);
        }
        return map;
    }

    @Override
    public GymCenter getCenter(String gymId) {
        return DataStore.getCenters().get(gymId);
    }

    // Additional methods for DAO integration
    @Override
    public boolean createCenter(GymCenter center) {
        if (center == null || center.getGymId() == null) return false;
        DataStore.getCentersMutable().put(center.getGymId(), center);
        return true;
    }

    @Override
    public boolean updateCenter(GymCenter center) {
        if (center == null || center.getGymId() == null) return false;
        GymCenter existing = DataStore.getCenters().get(center.getGymId());
        if (existing == null) return false;
        DataStore.getCentersMutable().put(center.getGymId(), center);
        return true;
    }

    @Override
    public boolean deleteCenter(String gymId) {
        if (gymId == null) return false;
        return DataStore.getCentersMutable().remove(gymId) != null;
    }

    @Override
    public List<GymCenter> getAllCenters() {
        return new ArrayList<>(DataStore.getCenters().values());
    }

    @Override
    public List<GymCenter> getCentersByOwner(String ownerId) {
        List<GymCenter> out = new ArrayList<>();
        for (GymCenter g : DataStore.getCenters().values()) {
            if (ownerId != null && ownerId.equals(g.getOwnerId())) {
                out.add(g);
            }
        }
        return out;
    }

    @Override
    public List<GymCenter> getCentersByValidation(boolean validated) {
        List<GymCenter> out = new ArrayList<>();
        for (GymCenter g : DataStore.getCenters().values()) {
            if (g.isValidated() == validated) {
                out.add(g);
            }
        }
        return out;
    }

    @Override
    public List<GymCenter> getCentersByLocation(String location) {
        if (location == null) return new ArrayList<>();
        List<GymCenter> out = new ArrayList<>();
        String loc = location.toLowerCase().trim();
        for (GymCenter g : DataStore.getCenters().values()) {
            if (g.getLocation() != null && g.getLocation().toLowerCase().contains(loc)) {
                out.add(g);
            }
        }
        return out;
    }
}
