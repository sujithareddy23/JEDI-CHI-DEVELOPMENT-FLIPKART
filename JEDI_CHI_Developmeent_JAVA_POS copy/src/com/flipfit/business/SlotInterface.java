package com.flipfit.business;

import com.flipfit.bean.Slot;
import java.time.LocalDate;
import java.util.List;

public interface SlotInterface {
    boolean isSlotAvailable(String slotId, LocalDate date);
    int getBookedCount(String slotId, LocalDate date);
    List<Slot> getSlotsByGym(String gymId);
    Slot getSlot(String slotId);
    boolean addSlot(Slot slot);
    boolean updateSlot(Slot slot);
    boolean deleteSlot(String slotId);
    List<Slot> getAllSlots();
}
