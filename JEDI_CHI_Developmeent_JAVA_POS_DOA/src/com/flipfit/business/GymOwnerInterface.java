package com.flipfit.business;

import com.flipfit.bean.GymCenter;
import com.flipfit.bean.GymOwner;
import com.flipfit.bean.Slot;
import java.util.List;

public interface GymOwnerInterface {
    void registerOwner(GymOwner owner);
    void registerGym(GymCenter gym);
    void updateSlots(String gymId, List<Slot> slots);
    void modifyOwnerProfile(GymOwner owner);
    List<GymCenter> getCentersByOwner(String ownerId);
}
