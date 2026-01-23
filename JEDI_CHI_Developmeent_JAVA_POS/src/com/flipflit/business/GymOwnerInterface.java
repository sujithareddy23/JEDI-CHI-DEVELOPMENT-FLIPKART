/**
 * 
 */
package com.flipflit.business;

import java.util.List;

/**
 * 
 */
public interface GymOwnerInterface {
	void registerGym(GymCenterImpl gym);
    void updateSlots(String gymId, List<SlotImpl> slots);
    void modifyOwnerProfile(GymOwnerImpl owner);

}
