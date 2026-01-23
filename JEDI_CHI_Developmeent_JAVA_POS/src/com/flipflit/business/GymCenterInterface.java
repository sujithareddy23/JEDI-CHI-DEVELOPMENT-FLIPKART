/**
 * 
 */
package com.flipflit.business;

import java.util.List;

/**
 * 
 */
public interface GymCenterInterface {
	List<SlotImpl> getAvailableSlots(String gymId);
}
