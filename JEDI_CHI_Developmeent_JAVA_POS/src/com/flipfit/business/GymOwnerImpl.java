package com.flipfit.business;

import com.flipfit.bean.GymCenter;
import com.flipfit.bean.GymOwner;
import com.flipfit.bean.Slot;
import com.flipfit.constants.IdPrefixConstants;
import com.flipfit.data.DataStore;
import com.flipfit.validation.OwnerValidation;
import com.flipfit.validation.GymCenterValidation;
import com.flipfit.validation.ValidationResult;

import java.util.ArrayList;
import java.util.List;

public class GymOwnerImpl implements GymOwnerInterface {

    @Override
    public boolean registerOwner(GymOwner owner) {
        ValidationResult vr = OwnerValidation.validateForSignUp(owner);
        if (!vr.isValid()) return false;
        if (DataStore.getOwners().containsKey(owner.getEmailId())) return false;
        owner.setValidated(false);
        if (owner.getId() == null) owner.setId(IdPrefixConstants.OWNER_PREFIX + (DataStore.getOwners().size() + 1));
        DataStore.getOwnersMutable().put(owner.getEmailId(), owner);
        return true;
    }

    @Override
    public void registerGym(GymCenter gym) {
        ValidationResult vr = GymCenterValidation.validateForRegistration(gym);
        if (!vr.isValid()) return;
        gym.setValidated(false);
        DataStore.getCentersMutable().put(gym.getGymId(), gym);
        if (gym.getSlotList() != null) {
            DataStore.getGymSlotsMutable().put(gym.getGymId(), new ArrayList<>(gym.getSlotList()));
        }
    }

    @Override
    public void updateSlots(String gymId, List<Slot> slots) {
        if (gymId == null || slots == null) return;
        DataStore.getGymSlotsMutable().put(gymId, new ArrayList<>(slots));
        GymCenter g = DataStore.getCenters().get(gymId);
        if (g != null) g.setSlotList(slots);
    }

    @Override
    public void modifyOwnerProfile(GymOwner owner) {
        if (owner == null || owner.getEmailId() == null) return;
        GymOwner o = DataStore.getOwnersMutable().get(owner.getEmailId());
        if (o != null) {
            if (owner.getOwnerName() != null) o.setOwnerName(owner.getOwnerName());
            if (owner.getPassword() != null) o.setPassword(owner.getPassword());
        }
    }

    @Override
    public List<GymCenter> getCentersByOwner(String ownerId) {
        List<GymCenter> out = new ArrayList<>();
        for (GymCenter g : DataStore.getCenters().values()) {
            if (ownerId != null && ownerId.equals(g.getOwnerId())) out.add(g);
        }
        return out;
    }
}
