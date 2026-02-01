package com.flipfit.dao;

import com.flipfit.constants.RoleConstants;

import java.util.Optional;

/**
 * Data Access Object interface for unified user authentication operations
 * Provides authentication and user validation across all user types
 */
public interface GymUserDAO {
    
    /**
     * Authenticate a user across all user types (Admin, Owner, Customer)
     * @param identifier The user identifier (adminId, emailId, or email)
     * @param password The password to validate
     * @return Optional containing the role if authentication successful, empty otherwise
     */
    Optional<String> authenticateUser(String identifier, String password);
    
    /**
     * Get the user ID for a successfully authenticated user
     * @param identifier The user identifier used for authentication
     * @return Optional containing the user ID if found, empty otherwise
     */
    Optional<String> getUserId(String identifier);
    
    /**
     * Validate user credentials for a specific role
     * @param identifier The user identifier
     * @param password The password to validate
     * @param role The role to validate against (ADMIN, OWNER, CUSTOMER)
     * @return Optional containing the user ID if credentials are valid for the role, empty otherwise
     */
    Optional<String> validateUserCredentials(String identifier, String password, String role);
    
    /**
     * Check if a user exists across all user types
     * @param identifier The user identifier
     * @return Optional containing the role if user exists, empty otherwise
     */
    Optional<String> checkUserExists(String identifier);
    
    /**
     * Get user details by ID and role
     * @param userId The user ID
     * @param role The user role
     * @return Optional containing user details as Object, empty otherwise
     */
    Optional<Object> getUserDetails(String userId, String role);
    
    /**
     * Update password for any user type
     * @param identifier The user identifier
     * @param oldPassword The old password for verification
     * @param newPassword The new password to set
     * @return true if password update successful, false otherwise
     */
    boolean updatePassword(String identifier, String oldPassword, String newPassword);
    
    /**
     * Get user statistics across all user types
     * @return UserStats object containing counts for each user type
     */
    UserStats getUserStatistics();
    
    /**
     * Inner class to hold user statistics
     */
    class UserStats {
        private int adminCount;
        private int ownerCount;
        private int customerCount;
        private int totalUsers;
        
        public UserStats(int adminCount, int ownerCount, int customerCount) {
            this.adminCount = adminCount;
            this.ownerCount = ownerCount;
            this.customerCount = customerCount;
            this.totalUsers = adminCount + ownerCount + customerCount;
        }
        
        public int getAdminCount() { return adminCount; }
        public int getOwnerCount() { return ownerCount; }
        public int getCustomerCount() { return customerCount; }
        public int getTotalUsers() { return totalUsers; }
        
        @Override
        public String toString() {
            return String.format("UserStats{Admins: %d, Owners: %d, Customers: %d, Total: %d}", 
                               adminCount, ownerCount, customerCount, totalUsers);
        }
    }
}
