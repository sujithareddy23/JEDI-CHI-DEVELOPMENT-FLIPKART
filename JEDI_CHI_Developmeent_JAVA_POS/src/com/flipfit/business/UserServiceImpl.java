package com.flipfit.business;

import com.flipfit.bean.GymAdmin;
import com.flipfit.bean.GymCustomer;
import com.flipfit.bean.GymOwner;
import com.flipfit.data.DataStore;

import java.util.Map;

public class UserServiceImpl implements UserServiceInterface {
    private String lastRole;
    private String lastUserId;

    @Override
    public String authenticate(String identifier, String password) {
        lastRole = null;
        lastUserId = null;
        if (identifier == null || password == null) return null;
        Map<String, GymAdmin> admins = DataStore.getAdmins();
        GymAdmin a = admins.get(identifier);
        if (a != null && password.equals(a.getPassword())) {
            lastRole = "ADMIN";
            lastUserId = a.getAdminId();
            return lastRole;
        }
        Map<String, GymOwner> owners = DataStore.getOwners();
        GymOwner o = owners.get(identifier);
        if (o != null && password.equals(o.getPassword())) {
            lastRole = "OWNER";
            lastUserId = o.getEmailId();
            return lastRole;
        }
        Map<String, GymCustomer> customers = DataStore.getCustomers();
        GymCustomer c = customers.get(identifier);
        if (c != null && password.equals(c.getPassword())) {
            lastRole = "CUSTOMER";
            lastUserId = c.getEmail();
            return lastRole;
        }
        return null;
    }

    @Override
    public String getLoggedInUserId() {
        return lastUserId;
    }
}
