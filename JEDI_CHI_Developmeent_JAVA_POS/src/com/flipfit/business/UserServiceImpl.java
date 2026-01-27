package com.flipfit.business;

import com.flipfit.constants.RoleConstants;
import com.flipfit.dao.GymUserDAO;
import com.flipfit.dao.impl.GymUserDAOImpl;
import com.flipfit.exception.InvalidCredentialsException;

public class UserServiceImpl implements UserServiceInterface {
    private String lastRole;
    private String lastUserId;
    private final GymUserDAO userDAO;

    public UserServiceImpl() {
        this.userDAO = new GymUserDAOImpl();
    }

    public UserServiceImpl(GymUserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Override
    public String authenticate(String identifier, String password) {
        lastRole = null;
        lastUserId = null;
        if (identifier == null || password == null) {
            throw new InvalidCredentialsException("Username and password are required.");
        }
        
        // Use DAO for authentication
        lastRole = userDAO.authenticateUser(identifier, password)
                .orElseThrow(() -> new InvalidCredentialsException("Invalid credentials."));
        
        // Get the user ID for the authenticated user
        lastUserId = userDAO.getUserId(identifier)
                .orElseThrow(() -> new InvalidCredentialsException("User ID not found."));
        
        return lastRole;
    }

    @Override
    public String getLoggedInUserId() {
        return lastUserId;
    }
}
