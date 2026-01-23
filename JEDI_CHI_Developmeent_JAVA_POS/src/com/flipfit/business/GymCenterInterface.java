/**
 * 
 */
package com.flipfit.business;

import java.util.List;

/**
 * 
 */
public interface GymCenterInterface {
	List<SlotImpl> getAvailableSlots(String gymId);
}
