package com.flipfit.dao;

import com.flipfit.bean.Slot;

import java.util.List;
import java.util.Optional;

/**
 * Data Access Object interface for Slot operations
 * Provides CRUD operations for slot entities
 */
public interface SlotDAO {
    
    /**
     * Create a new slot in the database
     * @param slot The slot object to create
     * @return true if creation successful, false otherwise
     */
    boolean createSlot(Slot slot);
    
    /**
     * Retrieve a slot by its ID
     * @param slotId The slot ID to search for
     * @return Optional containing the slot if found, empty otherwise
     */
    Optional<Slot> getSlotById(String slotId);
    
    /**
     * Retrieve all slots from the database
     * @return List of all slots
     */
    List<Slot> getAllSlots();
    
    /**
     * Retrieve slots by gym ID
     * @param gymId The gym ID to filter by
     * @return List of slots for the specified gym
     */
    List<Slot> getSlotsByGym(String gymId);
    
    /**
     * Retrieve active slots by gym ID
     * @param gymId The gym ID to filter by
     * @return List of active slots for the specified gym
     */
    List<Slot> getActiveSlotsByGym(String gymId);
    
    /**
     * Update an existing slot
     * @param slot The slot object with updated information
     * @return true if update successful, false otherwise
     */
    boolean updateSlot(Slot slot);
    
    /**
     * Delete a slot by its ID
     * @param slotId The slot ID to delete
     * @return true if deletion successful, false otherwise
     */
    boolean deleteSlot(String slotId);
    
    /**
     * Check if a slot exists by ID
     * @param slotId The slot ID to check
     * @return true if slot exists, false otherwise
     */
    boolean slotExists(String slotId);
    
    /**
     * Get the total count of slots
     * @return Number of slots in the database
     */
    int getSlotCount();
    
    /**
     * Get the count of slots by gym ID
     * @param gymId The gym ID to count slots for
     * @return Number of slots for the specified gym
     */
    int getSlotCountByGym(String gymId);
    
    /**
     * Update slot active status
     * @param slotId The slot ID to update
     * @param active The new active status
     * @return true if update successful, false otherwise
     */
    boolean updateSlotStatus(String slotId, boolean active);
    
    /**
     * Get slots by time range
     * @param gymId The gym ID
     * @param startTime Start time filter
     * @param endTime End time filter
     * @return List of slots within the time range
     */
    List<Slot> getSlotsByTimeRange(String gymId, java.time.LocalTime startTime, java.time.LocalTime endTime);
}
