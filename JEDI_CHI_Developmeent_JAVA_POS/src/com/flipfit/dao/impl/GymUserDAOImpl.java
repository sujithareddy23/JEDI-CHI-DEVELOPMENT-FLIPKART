package com.flipfit.dao.impl;

import com.flipfit.bean.GymAdmin;
import com.flipfit.bean.GymCustomer;
import com.flipfit.bean.GymOwner;
import com.flipfit.constants.RoleConstants;
import com.flipfit.dao.GymAdminDAO;
import com.flipfit.dao.GymCustomerDAO;
import com.flipfit.dao.GymOwnerDAO;
import com.flipfit.dao.GymUserDAO;

import java.util.Optional;

/**
 * Implementation of GymUserDAO interface
 * Provides unified authentication operations across all user types
 */
public class GymUserDAOImpl implements GymUserDAO {
    
    private final GymAdminDAO adminDAO;
    private final GymOwnerDAO ownerDAO;
    private final GymCustomerDAO customerDAO;
    
    public GymUserDAOImpl() {
        this.adminDAO = new GymAdminDAOImpl();
        this.ownerDAO = new GymOwnerDAOImpl();
        this.customerDAO = new GymCustomerDAOImpl();
    }
    
    public GymUserDAOImpl(GymAdminDAO adminDAO, GymOwnerDAO ownerDAO, GymCustomerDAO customerDAO) {
        this.adminDAO = adminDAO;
        this.ownerDAO = ownerDAO;
        this.customerDAO = customerDAO;
    }
    
    @Override
    public Optional<String> authenticateUser(String identifier, String password) {
        // Try admin first
        if (adminDAO.validateCredentials(identifier, password)) {
            return Optional.of(RoleConstants.ADMIN);
        }
        
        // Try owner (uses email as identifier)
        Optional<String> ownerId = ownerDAO.validateCredentials(identifier, password);
        if (ownerId.isPresent()) {
            return Optional.of(RoleConstants.OWNER);
        }
        
        // Try customer (uses email as identifier)
        Optional<String> customerId = customerDAO.validateCredentials(identifier, password);
        if (customerId.isPresent()) {
            return Optional.of(RoleConstants.CUSTOMER);
        }
        
        return Optional.empty();
    }
    
    @Override
    public Optional<String> getUserId(String identifier) {
        // Try admin first
        Optional<GymAdmin> admin = adminDAO.getAdminById(identifier);
        if (admin.isPresent()) {
            return Optional.of(admin.get().getAdminId());
        }
        
        // Try owner (uses email as identifier)
        Optional<GymOwner> owner = ownerDAO.getOwnerByEmail(identifier);
        if (owner.isPresent()) {
            return Optional.of(owner.get().getId());
        }
        
        // Try customer (uses email as identifier)
        Optional<GymCustomer> customer = customerDAO.getCustomerByEmail(identifier);
        if (customer.isPresent()) {
            return Optional.of(customer.get().getId());
        }
        
        return Optional.empty();
    }
    
    @Override
    public Optional<String> validateUserCredentials(String identifier, String password, String role) {
        switch (role) {
            case RoleConstants.ADMIN:
                if (adminDAO.validateCredentials(identifier, password)) {
                    return Optional.of(identifier); // adminId is the identifier
                }
                break;
                
            case RoleConstants.OWNER:
                return ownerDAO.validateCredentials(identifier, password); // returns ownerId
                
            case RoleConstants.CUSTOMER:
                return customerDAO.validateCredentials(identifier, password); // returns customerId
                
            default:
                return Optional.empty();
        }
        
        return Optional.empty();
    }
    
    @Override
    public Optional<String> checkUserExists(String identifier) {
        // Try admin first
        if (adminDAO.adminExists(identifier)) {
            return Optional.of(RoleConstants.ADMIN);
        }
        
        // Try owner (uses email as identifier)
        if (ownerDAO.ownerExistsByEmail(identifier)) {
            return Optional.of(RoleConstants.OWNER);
        }
        
        // Try customer (uses email as identifier)
        if (customerDAO.customerExistsByEmail(identifier)) {
            return Optional.of(RoleConstants.CUSTOMER);
        }
        
        return Optional.empty();
    }
    
    @Override
    public Optional<Object> getUserDetails(String userId, String role) {
        switch (role) {
            case RoleConstants.ADMIN:
                return adminDAO.getAdminById(userId).map(admin -> (Object) admin);
                
            case RoleConstants.OWNER:
                return ownerDAO.getOwnerById(userId).map(owner -> (Object) owner);
                
            case RoleConstants.CUSTOMER:
                return customerDAO.getCustomerById(userId).map(customer -> (Object) customer);
                
            default:
                return Optional.empty();
        }
    }
    
    @Override
    public boolean updatePassword(String identifier, String oldPassword, String newPassword) {
        // Try admin first
        if (adminDAO.validateCredentials(identifier, oldPassword)) {
            GymAdmin admin = adminDAO.getAdminById(identifier).orElse(null);
            if (admin != null) {
                admin.setPassword(newPassword);
                return adminDAO.updateAdmin(admin);
            }
        }
        
        // Try owner (uses email as identifier)
        Optional<String> ownerId = ownerDAO.validateCredentials(identifier, oldPassword);
        if (ownerId.isPresent()) {
            return customerDAO.updateCustomerPassword(ownerId.get(), newPassword);
        }
        
        // Try customer (uses email as identifier)
        Optional<String> customerId = customerDAO.validateCredentials(identifier, oldPassword);
        if (customerId.isPresent()) {
            return customerDAO.updateCustomerPassword(customerId.get(), newPassword);
        }
        
        return false;
    }
    
    @Override
    public UserStats getUserStatistics() {
        int adminCount = adminDAO.getAdminCount();
        int ownerCount = ownerDAO.getOwnerCount();
        int customerCount = customerDAO.getCustomerCount();
        
        return new UserStats(adminCount, ownerCount, customerCount);
    }
}
