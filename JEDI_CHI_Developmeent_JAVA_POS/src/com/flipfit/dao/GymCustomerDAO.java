package com.flipfit.dao;

import com.flipfit.bean.GymCustomer;

import java.util.List;
import java.util.Optional;

/**
 * Data Access Object interface for GymCustomer operations
 * Provides CRUD operations for gym customer entities
 */
public interface GymCustomerDAO {
    
    /**
     * Create a new gym customer in the database
     * @param customer The gym customer object to create
     * @return true if creation successful, false otherwise
     */
    boolean createCustomer(GymCustomer customer);
    
    /**
     * Retrieve a gym customer by their ID
     * @param customerId The customer ID to search for
     * @return Optional containing the gym customer if found, empty otherwise
     */
    Optional<GymCustomer> getCustomerById(String customerId);
    
    /**
     * Retrieve a gym customer by their email
     * @param email The email to search for
     * @return Optional containing the gym customer if found, empty otherwise
     */
    Optional<GymCustomer> getCustomerByEmail(String email);
    
    /**
     * Retrieve a gym customer by their mobile number
     * @param mobileNo The mobile number to search for
     * @return Optional containing the gym customer if found, empty otherwise
     */
    Optional<GymCustomer> getCustomerByMobile(String mobileNo);
    
    /**
     * Retrieve all gym customers from the database
     * @return List of all gym customers
     */
    List<GymCustomer> getAllCustomers();
    
    /**
     * Search gym customers by name (partial match)
     * @param name The name or partial name to search for
     * @return List of gym customers matching the name criteria
     */
    List<GymCustomer> searchCustomersByName(String name);
    
    /**
     * Get gym customers by location
     * @param location The location to filter by
     * @return List of gym customers in the specified location
     */
    List<GymCustomer> getCustomersByLocation(String location);
    
    /**
     * Update an existing gym customer's information
     * @param customer The gym customer object with updated information
     * @return true if update successful, false otherwise
     */
    boolean updateCustomer(GymCustomer customer);
    
    /**
     * Update customer password
     * @param customerId The customer ID
     * @param newPassword The new password
     * @return true if update successful, false otherwise
     */
    boolean updateCustomerPassword(String customerId, String newPassword);
    
    /**
     * Delete a gym customer by their ID
     * @param customerId The customer ID to delete
     * @return true if deletion successful, false otherwise
     */
    boolean deleteCustomer(String customerId);
    
    /**
     * Check if a customer exists by ID
     * @param customerId The customer ID to check
     * @return true if customer exists, false otherwise
     */
    boolean customerExists(String customerId);
    
    /**
     * Check if a customer exists by email
     * @param email The email to check
     * @return true if customer exists, false otherwise
     */
    boolean customerExistsByEmail(String email);
    
    /**
     * Validate customer credentials for authentication
     * @param email The email
     * @param password The password to validate
     * @return Optional containing the customer ID if credentials are valid, empty otherwise
     */
    Optional<String> validateCredentials(String email, String password);
    
    /**
     * Get the total count of gym customers
     * @return Number of gym customers in the database
     */
    int getCustomerCount();
    
    /**
     * Get customers registered within a date range
     * @param startDate The start date
     * @param endDate The end date
     * @return List of customers registered within the date range
     */
    List<GymCustomer> getCustomersByDateRange(java.time.LocalDate startDate, java.time.LocalDate endDate);
}
