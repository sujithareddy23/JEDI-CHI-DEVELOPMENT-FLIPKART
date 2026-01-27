package com.flipfit.business;

import com.flipfit.bean.GymCenter;
import com.flipfit.bean.GymOwner;
import com.flipfit.bean.Slot;
import com.flipfit.constants.IdPrefixConstants;
import com.flipfit.dao.GymOwnerDAO;
import com.flipfit.dao.impl.GymOwnerDAOImpl;
import com.flipfit.exception.AlreadyExistsException;
import com.flipfit.exception.ValidationException;
import com.flipfit.validation.OwnerValidation;
import com.flipfit.validation.GymCenterValidation;
import com.flipfit.validation.ValidationResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class GymOwnerImpl implements GymOwnerInterface {
    private final GymOwnerDAO ownerDAO;
    private final GymCenterInterface centerService;
    private final SlotInterface slotService;

    public GymOwnerImpl() {
        this.ownerDAO = new GymOwnerDAOImpl();
        this.centerService = new GymCenterImpl();
        this.slotService = new SlotImpl();
    }

    public GymOwnerImpl(GymOwnerDAO ownerDAO, GymCenterInterface centerService, SlotInterface slotService) {
        this.ownerDAO = ownerDAO;
        this.centerService = centerService;
        this.slotService = slotService;
    }

    @Override
    public void registerOwner(GymOwner owner) {
        ValidationResult vr = OwnerValidation.validateForSignUp(owner);
        if (!vr.isValid()) throw new ValidationException(vr.getMessage());
        
        if (ownerDAO.ownerExistsByEmail(owner.getEmailId())) {
            throw new AlreadyExistsException("Email already registered.");
        }
        
        owner.setValidated(false);
        if (owner.getId() == null) {
            owner.setId(IdPrefixConstants.OWNER_PREFIX + (ownerDAO.getOwnerCount() + 1));
        }
        
        if (!ownerDAO.createOwner(owner)) {
            throw new ValidationException("Failed to register owner.");
        }
    }

    @Override
    public void registerGym(GymCenter gym) {
        ValidationResult vr = GymCenterValidation.validateForRegistration(gym);
        if (!vr.isValid()) throw new ValidationException(vr.getMessage());
        
        gym.setValidated(false);
        
        // Create gym center (this would need GymCenterDAO implementation)
        // For now, we'll use the existing center service
        if (!centerService.createCenter(gym)) {
            throw new ValidationException("Failed to register gym center.");
        }
        
        // Add slots if provided
        if (gym.getSlotList() != null && !gym.getSlotList().isEmpty()) {
            for (Slot slot : gym.getSlotList()) {
                slot.setGymId(gym.getGymId());
                slotService.addSlot(slot);
            }
        }
    }

    @Override
    public void updateSlots(String gymId, List<Slot> slots) {
        if (gymId == null || slots == null) return;
        
        // Remove existing slots for this gym
        List<Slot> existingSlots = slotService.getSlotsByGym(gymId);
        for (Slot existingSlot : existingSlots) {
            slotService.deleteSlot(existingSlot.getSlotId());
        }
        
        // Add new slots
        for (Slot slot : slots) {
            slot.setGymId(gymId);
            slotService.addSlot(slot);
        }
    }

    @Override
    public void modifyOwnerProfile(GymOwner owner) {
        if (owner == null || owner.getEmailId() == null) return;
        
        Optional<GymOwner> existingOwnerOpt = ownerDAO.getOwnerByEmail(owner.getEmailId());
        if (existingOwnerOpt.isPresent()) {
            GymOwner existingOwner = existingOwnerOpt.get();
            if (owner.getOwnerName() != null) existingOwner.setOwnerName(owner.getOwnerName());
            if (owner.getPassword() != null) existingOwner.setPassword(owner.getPassword());
            if (owner.getIdProof() != null) existingOwner.setIdProof(owner.getIdProof());
            if (owner.getPanNo() != null) existingOwner.setPanNo(owner.getPanNo());
            if (owner.getGstNo() != null) existingOwner.setGstNo(owner.getGstNo());
            
            if (!ownerDAO.updateOwner(existingOwner)) {
                throw new ValidationException("Failed to update owner profile.");
            }
        }
    }

    @Override
    public List<GymCenter> getCentersByOwner(String ownerId) {
        return centerService.getCentersByOwner(ownerId);
    }

    @Override
    public boolean createOwner(GymOwner owner) {
        return ownerDAO.createOwner(owner);
    }

    @Override
    public GymOwner getOwner(String ownerId) {
        return ownerDAO.getOwnerById(ownerId).orElse(null);
    }

    @Override
    public boolean updateOwner(GymOwner owner) {
        return ownerDAO.updateOwner(owner);
    }

    @Override
    public List<GymOwner> getAllOwners() {
        return ownerDAO.getAllOwners();
    }
}
