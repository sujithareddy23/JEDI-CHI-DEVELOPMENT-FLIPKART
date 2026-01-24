package com.flipfit.business;

import com.flipfit.bean.GymAdmin;
import com.flipfit.bean.GymCustomer;
import com.flipfit.bean.GymOwner;
import com.flipfit.constants.RoleConstants;
import com.flipfit.data.DataStore;
import com.flipfit.exception.InvalidCredentialsException;

import java.util.Map;

public class UserServiceImpl implements UserServiceInterface {
    private String lastRole;
    private String lastUserId;

    @Override
    public String authenticate(String identifier, String password) {
        lastRole = null;
        lastUserId = null;
        if (identifier == null || password == null) {
            throw new InvalidCredentialsException("Username and password are required.");
        }
        Map<String, GymAdmin> admins = DataStore.getAdmins();
        GymAdmin a = admins.get(identifier);
        if (a != null && password.equals(a.getPassword())) {
            lastRole = RoleConstants.ADMIN;
            lastUserId = a.getAdminId();
            return lastRole;
        }
        Map<String, GymOwner> owners = DataStore.getOwners();
        GymOwner o = owners.get(identifier);
        if (o != null && password.equals(o.getPassword())) {
            lastRole = RoleConstants.OWNER;
            lastUserId = o.getEmailId();
            return lastRole;
        }
        Map<String, GymCustomer> customers = DataStore.getCustomers();
        GymCustomer c = customers.get(identifier);
        if (c != null && password.equals(c.getPassword())) {
            lastRole = RoleConstants.CUSTOMER;
            lastUserId = c.getEmail();
            return lastRole;
        }
        throw new InvalidCredentialsException("Invalid credentials.");
    }

    @Override
    public String getLoggedInUserId() {
        return lastUserId;
    }
}
