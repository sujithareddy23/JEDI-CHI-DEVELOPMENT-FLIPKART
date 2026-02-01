package com.flipfit.dao;

import com.flipfit.bean.GymAdmin;

import java.util.List;
import java.util.Optional;

/**
 * Data Access Object interface for GymAdmin operations
 * Provides CRUD operations for gym admin entities
 */
public interface GymAdminDAO {
    
    /**
     * Create a new gym admin in the database
     * @param admin The gym admin object to create
     * @return true if creation successful, false otherwise
     */
    boolean createAdmin(GymAdmin admin);
    
    /**
     * Retrieve a gym admin by their admin ID
     * @param adminId The admin ID to search for
     * @return Optional containing the gym admin if found, empty otherwise
     */
    Optional<GymAdmin> getAdminById(String adminId);
    
    /**
     * Retrieve a gym admin by their name
     * @param name The admin name to search for
     * @return Optional containing the gym admin if found, empty otherwise
     */
    Optional<GymAdmin> getAdminByName(String name);
    
    /**
     * Retrieve all gym admins from the database
     * @return List of all gym admins
     */
    List<GymAdmin> getAllAdmins();
    
    /**
     * Update an existing gym admin's information
     * @param admin The gym admin object with updated information
     * @return true if update successful, false otherwise
     */
    boolean updateAdmin(GymAdmin admin);
    
    /**
     * Delete a gym admin by their admin ID
     * @param adminId The admin ID to delete
     * @return true if deletion successful, false otherwise
     */
    boolean deleteAdmin(String adminId);
    
    /**
     * Check if an admin exists by admin ID
     * @param adminId The admin ID to check
     * @return true if admin exists, false otherwise
     */
    boolean adminExists(String adminId);
    
    /**
     * Validate admin credentials for authentication
     * @param adminId The admin ID
     * @param password The password to validate
     * @return true if credentials are valid, false otherwise
     */
    boolean validateCredentials(String adminId, String password);
    
    /**
     * Get the total count of gym admins
     * @return Number of gym admins in the database
     */
    int getAdminCount();
}
