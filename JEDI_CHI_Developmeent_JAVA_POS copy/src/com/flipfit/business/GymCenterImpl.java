package com.flipfit.business;

import com.flipfit.bean.GymCenter;
import com.flipfit.bean.Slot;
import com.flipfit.dao.GymCenterDAO;
import com.flipfit.dao.impl.GymCenterDAOImpl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class GymCenterImpl implements GymCenterInterface {
    private final SlotInterface slotService;
    private final GymCenterDAO centerDAO;

    public GymCenterImpl() {
        this.slotService = new SlotImpl();
        this.centerDAO = new GymCenterDAOImpl();
    }

    public GymCenterImpl(SlotInterface slotService, GymCenterDAO centerDAO) {
        this.slotService = slotService;
        this.centerDAO = centerDAO;
    }

    @Override
    public List<GymCenter> getCentersByCity(String city) {
        if (city == null) return new ArrayList<>();
        return centerDAO.getValidatedCentersByCity(city);
    }

    @Override
    public List<Slot> getAvailableSlots(String gymId) {
        return slotService.getSlotsByGym(gymId);
    }

    @Override
    public Map<Slot, Integer> getSlotAvailabilityForDate(String gymId, LocalDate date) {
        Map<Slot, Integer> map = new LinkedHashMap<>();
        List<Slot> slots = slotService.getSlotsByGym(gymId);
        for (Slot slot : slots) {
            int booked = slotService.getBookedCount(slot.getSlotId(), date);
            int free = Math.max(0, slot.getTotalCapacity() - booked);
            map.put(slot, free);
        }
        return map;
    }

    @Override
    public GymCenter getCenter(String gymId) {
        Optional<GymCenter> centerOpt = centerDAO.getCenterById(gymId);
        return centerOpt.orElse(null);
    }

    @Override
    public boolean createCenter(GymCenter center) {
        if (center == null || center.getGymId() == null) return false;
        return centerDAO.createCenter(center);
    }

    @Override
    public boolean updateCenter(GymCenter center) {
        if (center == null || center.getGymId() == null) return false;
        return centerDAO.updateCenter(center);
    }

    @Override
    public boolean deleteCenter(String gymId) {
        if (gymId == null) return false;
        return centerDAO.deleteCenter(gymId);
    }

    @Override
    public List<GymCenter> getAllCenters() {
        return centerDAO.getAllCenters();
    }

    @Override
    public List<GymCenter> getCentersByOwner(String ownerId) {
        return centerDAO.getCentersByOwner(ownerId);
    }

    @Override
    public List<GymCenter> getCentersByValidation(boolean validated) {
        return centerDAO.getCentersByValidation(validated);
    }

    @Override
    public List<GymCenter> getCentersByLocation(String location) {
        return centerDAO.getCentersByLocation(location);
    }
}
