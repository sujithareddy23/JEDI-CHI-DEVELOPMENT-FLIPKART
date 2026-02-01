package com.flipfit.dao;

import com.flipfit.bean.GymCenter;

import java.util.List;
import java.util.Optional;

/**
 * Data Access Object interface for GymCenter operations
 * Provides CRUD operations for gym center entities
 */
public interface GymCenterDAO {
    
    /**
     * Create a new gym center in the database
     * @param center The gym center object to create
     * @return true if creation successful, false otherwise
     */
    boolean createCenter(GymCenter center);
    
    /**
     * Retrieve a gym center by its ID
     * @param gymId The gym ID to search for
     * @return Optional containing the gym center if found, empty otherwise
     */
    Optional<GymCenter> getCenterById(String gymId);
    
    /**
     * Retrieve all gym centers from the database
     * @return List of all gym centers
     */
    List<GymCenter> getAllCenters();
    
    /**
     * Retrieve gym centers by city
     * @param city The city to filter by
     * @return List of gym centers in the specified city
     */
    List<GymCenter> getCentersByCity(String city);
    
    /**
     * Retrieve gym centers by owner ID
     * @param ownerId The owner ID to filter by
     * @return List of gym centers owned by the specified owner
     */
    List<GymCenter> getCentersByOwner(String ownerId);
    
    /**
     * Retrieve gym centers by validation status
     * @param validated The validation status to filter by
     * @return List of gym centers with the specified validation status
     */
    List<GymCenter> getCentersByValidation(boolean validated);
    
    /**
     * Retrieve gym centers by location (partial match)
     * @param location The location to search for
     * @return List of gym centers matching the location criteria
     */
    List<GymCenter> getCentersByLocation(String location);
    
    /**
     * Retrieve validated gym centers by city
     * @param city The city to filter by
     * @return List of validated gym centers in the specified city
     */
    List<GymCenter> getValidatedCentersByCity(String city);
    
    /**
     * Update an existing gym center
     * @param center The gym center object with updated information
     * @return true if update successful, false otherwise
     */
    boolean updateCenter(GymCenter center);
    
    /**
     * Delete a gym center by its ID
     * @param gymId The gym ID to delete
     * @return true if deletion successful, false otherwise
     */
    boolean deleteCenter(String gymId);
    
    /**
     * Check if a gym center exists by ID
     * @param gymId The gym ID to check
     * @return true if gym center exists, false otherwise
     */
    boolean centerExists(String gymId);
    
    /**
     * Get the total count of gym centers
     * @return Number of gym centers in the database
     */
    int getCenterCount();
    
    /**
     * Get the count of gym centers by validation status
     * @param validated The validation status to count
     * @return Number of gym centers with the specified validation status
     */
    int getCenterCountByValidation(boolean validated);
    
    /**
     * Get the count of gym centers by owner
     * @param ownerId The owner ID
     * @return Number of gym centers owned by the specified owner
     */
    int getCenterCountByOwner(String ownerId);
    
    /**
     * Update gym center validation status
     * @param gymId The gym ID to update
     * @param validated The new validation status
     * @return true if update successful, false otherwise
     */
    boolean updateCenterValidation(String gymId, boolean validated);
}
