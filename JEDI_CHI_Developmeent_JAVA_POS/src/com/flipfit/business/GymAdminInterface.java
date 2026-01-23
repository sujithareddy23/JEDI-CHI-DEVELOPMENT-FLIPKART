/**
 * 
 */
package com.flipfit.business;

import java.util.List;

/**
 * 
 */
public interface GymAdminInterface {
	void validateGym(String gymId);
    void withdrawPermission(String gymId);
    List<GymOwnerImpl> listPendingPartners();
    List<GymOwnerImpl> listVerifiedPartners();
    List<GymCenterImpl> searchGyms(String location);
    void profileChanges(String adminId, Object updatedDetails);
    void modifyProfile(String customerId, Object updatedDetails);

}
