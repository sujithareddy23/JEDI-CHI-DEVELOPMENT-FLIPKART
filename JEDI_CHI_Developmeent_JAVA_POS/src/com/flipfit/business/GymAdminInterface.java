package com.flipfit.business;

import com.flipfit.bean.GymAdmin;
import com.flipfit.bean.GymCenter;
import com.flipfit.bean.GymOwner;
import java.util.List;

public interface GymAdminInterface {
    void validateGym(String gymId);
    void withdrawPermission(String gymId);
    List<GymOwner> listPendingPartners();
    List<GymOwner> listVerifiedPartners();
    List<GymCenter> listPendingGymCenters();
    List<GymCenter> listVerifiedGymCenters();
    List<GymCenter> searchGymsByLocation(String location);
	boolean createAdmin(GymAdmin admin);
	GymAdmin getAdmin(String adminId);
	boolean updateAdmin(GymAdmin admin);
	List<GymAdmin> getAllAdmins();
}
