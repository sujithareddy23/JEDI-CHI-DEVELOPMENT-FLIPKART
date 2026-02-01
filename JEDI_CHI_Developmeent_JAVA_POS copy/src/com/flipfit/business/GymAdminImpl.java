package com.flipfit.business;

import com.flipfit.bean.GymCenter;
import com.flipfit.bean.GymOwner;
import com.flipfit.dao.GymOwnerDAO;
import com.flipfit.dao.impl.GymOwnerDAOImpl;
import com.flipfit.exception.NotFoundException;

import java.util.List;
import java.util.Optional;

public class GymAdminImpl implements GymAdminInterface {
    private final GymOwnerDAO ownerDAO;
    private final GymCenterInterface centerService;

    public GymAdminImpl() {
        this.ownerDAO = new GymOwnerDAOImpl();
        this.centerService = new GymCenterImpl();
    }

    public GymAdminImpl(GymOwnerDAO ownerDAO, GymCenterInterface centerService) {
        this.ownerDAO = ownerDAO;
        this.centerService = centerService;
    }

    @Override
    public void validateGym(String gymId) {
        GymCenter gym = centerService.getCenter(gymId);
        if (gym == null) throw new NotFoundException("Gym not found: " + gymId);
        
        // Update gym validation status
        gym.setValidated(true);
        if (!centerService.updateCenter(gym)) {
            throw new NotFoundException("Failed to update gym validation status.");
        }
        
        // Update owner validation status
        Optional<GymOwner> ownerOpt = ownerDAO.getOwnerById(gym.getOwnerId());
        if (ownerOpt.isPresent()) {
            GymOwner owner = ownerOpt.get();
            owner.setValidated(true);
            if (!ownerDAO.updateOwner(owner)) {
                throw new NotFoundException("Failed to update owner validation status.");
            }
        }
    }

    @Override
    public void withdrawPermission(String gymId) {
        GymCenter gym = centerService.getCenter(gymId);
        if (gym == null) throw new NotFoundException("Gym not found: " + gymId);
        
        gym.setValidated(false);
        if (!centerService.updateCenter(gym)) {
            throw new NotFoundException("Failed to withdraw gym permission.");
        }
    }

    @Override
    public List<GymOwner> listPendingPartners() {
        return ownerDAO.getOwnersByValidationStatus(false);
    }

    @Override
    public List<GymOwner> listVerifiedPartners() {
        return ownerDAO.getOwnersByValidationStatus(true);
    }

    @Override
    public List<GymCenter> listPendingGymCenters() {
        return centerService.getCentersByValidation(false);
    }

    @Override
    public List<GymCenter> listVerifiedGymCenters() {
        return centerService.getCentersByValidation(true);
    }

    @Override
    public List<GymCenter> searchGymsByLocation(String location) {
        return centerService.getCentersByLocation(location);
    }

    @Override
    public boolean createAdmin(com.flipfit.bean.GymAdmin admin) {
        // This would need GymAdminDAO implementation
        // For now, returning false as placeholder
        return false;
    }

    @Override
    public com.flipfit.bean.GymAdmin getAdmin(String adminId) {
        // This would need GymAdminDAO implementation
        // For now, returning null as placeholder
        return null;
    }

    @Override
    public boolean updateAdmin(com.flipfit.bean.GymAdmin admin) {
        // This would need GymAdminDAO implementation
        // For now, returning false as placeholder
        return false;
    }

    @Override
    public List<com.flipfit.bean.GymAdmin> getAllAdmins() {
        // This would need GymAdminDAO implementation
        // For now, returning empty list as placeholder
        return List.of();
    }
}
