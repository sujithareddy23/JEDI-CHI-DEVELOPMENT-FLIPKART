package com.flipfit.business;

import com.flipfit.bean.Booking;
import com.flipfit.bean.Slot;
import com.flipfit.dao.SlotDAO;
import com.flipfit.dao.BookingDAO;
import com.flipfit.dao.impl.SlotDAOImpl;
import com.flipfit.dao.impl.BookingDAOImpl;
import com.flipfit.exception.NotFoundException;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class SlotImpl implements SlotInterface {
    private final SlotDAO slotDAO;
    private final BookingDAO bookingDAO;

    public SlotImpl() {
        this.slotDAO = new SlotDAOImpl();
        this.bookingDAO = new BookingDAOImpl();
    }

    public SlotImpl(SlotDAO slotDAO, BookingDAO bookingDAO) {
        this.slotDAO = slotDAO;
        this.bookingDAO = bookingDAO;
    }

    @Override
    public boolean isSlotAvailable(String slotId, LocalDate date) {
        Optional<Slot> slotOpt = slotDAO.getSlotById(slotId);
        if (!slotOpt.isPresent()) {
            throw new NotFoundException("Slot not found: " + slotId);
        }
        
        Slot slot = slotOpt.get();
        int bookedCount = bookingDAO.getConfirmedBookingCountBySlotAndDate(slotId, date);
        return bookedCount < slot.getTotalCapacity();
    }

    @Override
    public int getBookedCount(String slotId, LocalDate date) {
        return bookingDAO.getConfirmedBookingCountBySlotAndDate(slotId, date);
    }

    @Override
    public List<Slot> getSlotsByGym(String gymId) {
        return slotDAO.getActiveSlotsByGym(gymId);
    }

    @Override
    public Slot getSlot(String slotId) {
        Optional<Slot> slotOpt = slotDAO.getSlotById(slotId);
        if (!slotOpt.isPresent()) {
            throw new NotFoundException("Slot not found: " + slotId);
        }
        return slotOpt.get();
    }

    @Override
    public boolean addSlot(Slot slot) {
        if (slot == null || slot.getSlotId() == null) return false;
        return slotDAO.createSlot(slot);
    }

    @Override
    public boolean updateSlot(Slot slot) {
        if (slot == null || slot.getSlotId() == null) return false;
        return slotDAO.updateSlot(slot);
    }

    @Override
    public boolean deleteSlot(String slotId) {
        if (slotId == null) return false;
        return slotDAO.deleteSlot(slotId);
    }

    @Override
    public List<Slot> getAllSlots() {
        return slotDAO.getAllSlots();
    }
}
