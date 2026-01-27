package com.flipfit.business;

import com.flipfit.bean.Booking;
import com.flipfit.bean.BookingStatus;
import com.flipfit.bean.Slot;
import com.flipfit.data.DataStore;
import com.flipfit.exception.NotFoundException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SlotImpl implements SlotInterface {

    @Override
    public boolean isSlotAvailable(String slotId, LocalDate date) {
        Slot s = getSlot(slotId);
        int booked = getBookedCount(slotId, date);
        return booked < s.getTotalCapacity();
    }

    @Override
    public int getBookedCount(String slotId, LocalDate date) {
        int count = 0;
        for (Booking b : DataStore.getBookings()) {
            if (BookingStatus.CONFIRMED.equals(b.getStatus())
                    && slotId.equals(b.getSlotId())
                    && date.equals(b.getBookingDate())) {
                count++;
            }
        }
        return count;
    }

    @Override
    public List<Slot> getSlotsByGym(String gymId) {
        Map<String, List<Slot>> m = DataStore.getGymSlots();
        List<Slot> list = m.get(gymId);
        return list == null ? new ArrayList<>() : new ArrayList<>(list);
    }

    @Override
    public Slot getSlot(String slotId) {
        Map<String, List<Slot>> m = DataStore.getGymSlots();
        for (List<Slot> list : m.values()) {
            for (Slot s : list) {
                if (slotId.equals(s.getSlotId())) return s;
            }
        }
        throw new NotFoundException("Slot not found: " + slotId);
    }
}
