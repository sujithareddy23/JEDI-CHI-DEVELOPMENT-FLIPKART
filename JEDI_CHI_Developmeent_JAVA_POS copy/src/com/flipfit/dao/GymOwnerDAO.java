package com.flipfit.dao;

import com.flipfit.bean.GymOwner;

import java.util.List;
import java.util.Optional;

/**
 * Data Access Object interface for GymOwner operations
 * Provides CRUD operations for gym owner entities
 */
public interface GymOwnerDAO {
    
    /**
     * Create a new gym owner in the database
     * @param owner The gym owner object to create
     * @return true if creation successful, false otherwise
     */
    boolean createOwner(GymOwner owner);
    
    /**
     * Retrieve a gym owner by their ID
     * @param ownerId The owner ID to search for
     * @return Optional containing the gym owner if found, empty otherwise
     */
    Optional<GymOwner> getOwnerById(String ownerId);
    
    /**
     * Retrieve a gym owner by their email ID
     * @param emailId The email ID to search for
     * @return Optional containing the gym owner if found, empty otherwise
     */
    Optional<GymOwner> getOwnerByEmail(String emailId);
    
    /**
     * Retrieve a gym owner by their PAN number
     * @param panNo The PAN number to search for
     * @return Optional containing the gym owner if found, empty otherwise
     */
    Optional<GymOwner> getOwnerByPan(String panNo);
    
    /**
     * Retrieve all gym owners from the database
     * @return List of all gym owners
     */
    List<GymOwner> getAllOwners();
    
    /**
     * Retrieve gym owners by validation status
     * @param validated The validation status to filter by
     * @return List of gym owners with the specified validation status
     */
    List<GymOwner> getOwnersByValidationStatus(boolean validated);
    
    /**
     * Update an existing gym owner's information
     * @param owner The gym owner object with updated information
     * @return true if update successful, false otherwise
     */
    boolean updateOwner(GymOwner owner);
    
    /**
     * Update the validation status of a gym owner
     * @param ownerId The owner ID to update
     * @param validated The new validation status
     * @return true if update successful, false otherwise
     */
    boolean updateOwnerValidation(String ownerId, boolean validated);
    
    /**
     * Delete a gym owner by their ID
     * @param ownerId The owner ID to delete
     * @return true if deletion successful, false otherwise
     */
    boolean deleteOwner(String ownerId);
    
    /**
     * Check if an owner exists by ID
     * @param ownerId The owner ID to check
     * @return true if owner exists, false otherwise
     */
    boolean ownerExists(String ownerId);
    
    /**
     * Check if an owner exists by email ID
     * @param emailId The email ID to check
     * @return true if owner exists, false otherwise
     */
    boolean ownerExistsByEmail(String emailId);
    
    /**
     * Validate owner credentials for authentication
     * @param emailId The email ID
     * @param password The password to validate
     * @return Optional containing the owner ID if credentials are valid, empty otherwise
     */
    Optional<String> validateCredentials(String emailId, String password);
    
    /**
     * Get the total count of gym owners
     * @return Number of gym owners in the database
     */
    int getOwnerCount();
    
    /**
     * Get the count of gym owners by validation status
     * @param validated The validation status to count
     * @return Number of gym owners with the specified validation status
     */
    int getOwnerCountByValidation(boolean validated);
}
