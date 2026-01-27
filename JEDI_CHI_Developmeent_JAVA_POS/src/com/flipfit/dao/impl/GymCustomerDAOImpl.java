package com.flipfit.dao.impl;

import com.flipfit.bean.GymCustomer;
import com.flipfit.dao.GymCustomerDAO;
import com.flipfit.util.DatabaseUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implementation of GymCustomerDAO interface
 * Provides database operations for gym customer entities
 */
public class GymCustomerDAOImpl implements GymCustomerDAO {
    
    private static final String INSERT_CUSTOMER = 
        "INSERT INTO gym_customers (id, name, email, password, mobile_no, address, other_details) VALUES (?, ?, ?, ?, ?, ?, ?)";
    
    private static final String SELECT_CUSTOMER_BY_ID = 
        "SELECT id, name, email, password, mobile_no, address, other_details, created_at, updated_at FROM gym_customers WHERE id = ?";
    
    private static final String SELECT_CUSTOMER_BY_EMAIL = 
        "SELECT id, name, email, password, mobile_no, address, other_details, created_at, updated_at FROM gym_customers WHERE email = ?";
    
    private static final String SELECT_CUSTOMER_BY_MOBILE = 
        "SELECT id, name, email, password, mobile_no, address, other_details, created_at, updated_at FROM gym_customers WHERE mobile_no = ?";
    
    private static final String SELECT_ALL_CUSTOMERS = 
        "SELECT id, name, email, password, mobile_no, address, other_details, created_at, updated_at FROM gym_customers ORDER BY name";
    
    private static final String SEARCH_CUSTOMERS_BY_NAME = 
        "SELECT id, name, email, password, mobile_no, address, other_details, created_at, updated_at FROM gym_customers WHERE name LIKE ? ORDER BY name";
    
    private static final String SELECT_CUSTOMERS_BY_LOCATION = 
        "SELECT id, name, email, password, mobile_no, address, other_details, created_at, updated_at FROM gym_customers WHERE address LIKE ? ORDER BY name";
    
    private static final String UPDATE_CUSTOMER = 
        "UPDATE gym_customers SET name = ?, email = ?, password = ?, mobile_no = ?, address = ?, other_details = ?, updated_at = CURRENT_TIMESTAMP WHERE id = ?";
    
    private static final String UPDATE_PASSWORD = 
        "UPDATE gym_customers SET password = ?, updated_at = CURRENT_TIMESTAMP WHERE id = ?";
    
    private static final String DELETE_CUSTOMER = 
        "DELETE FROM gym_customers WHERE id = ?";
    
    private static final String CHECK_CUSTOMER_EXISTS = 
        "SELECT COUNT(*) FROM gym_customers WHERE id = ?";
    
    private static final String CHECK_CUSTOMER_EXISTS_EMAIL = 
        "SELECT COUNT(*) FROM gym_customers WHERE email = ?";
    
    private static final String VALIDATE_CREDENTIALS = 
        "SELECT id FROM gym_customers WHERE email = ? AND password = ?";
    
    private static final String GET_CUSTOMER_COUNT = 
        "SELECT COUNT(*) FROM gym_customers";
    
    private static final String SELECT_CUSTOMERS_BY_DATE_RANGE = 
        "SELECT id, name, email, password, mobile_no, address, other_details, created_at, updated_at FROM gym_customers WHERE DATE(created_at) BETWEEN ? AND ? ORDER BY created_at";
    
    @Override
    public boolean createCustomer(GymCustomer customer) {
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(INSERT_CUSTOMER)) {
            
            pstmt.setString(1, customer.getId());
            pstmt.setString(2, customer.getName());
            pstmt.setString(3, customer.getEmail());
            pstmt.setString(4, customer.getPassword());
            pstmt.setString(5, customer.getMobileNo());
            pstmt.setString(6, customer.getAddress());
            pstmt.setString(7, customer.getOtherDetails());
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error creating gym customer: " + e.getMessage());
            return false;
        }
    }
    
    @Override
    public Optional<GymCustomer> getCustomerById(String customerId) {
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SELECT_CUSTOMER_BY_ID)) {
            
            pstmt.setString(1, customerId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToCustomer(rs));
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting customer by ID: " + e.getMessage());
        }
        
        return Optional.empty();
    }
    
    @Override
    public Optional<GymCustomer> getCustomerByEmail(String email) {
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SELECT_CUSTOMER_BY_EMAIL)) {
            
            pstmt.setString(1, email);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToCustomer(rs));
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting customer by email: " + e.getMessage());
        }
        
        return Optional.empty();
    }
    
    @Override
    public Optional<GymCustomer> getCustomerByMobile(String mobileNo) {
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SELECT_CUSTOMER_BY_MOBILE)) {
            
            pstmt.setString(1, mobileNo);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToCustomer(rs));
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting customer by mobile: " + e.getMessage());
        }
        
        return Optional.empty();
    }
    
    @Override
    public List<GymCustomer> getAllCustomers() {
        List<GymCustomer> customers = new ArrayList<>();
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SELECT_ALL_CUSTOMERS);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                customers.add(mapResultSetToCustomer(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting all customers: " + e.getMessage());
        }
        
        return customers;
    }
    
    @Override
    public List<GymCustomer> searchCustomersByName(String name) {
        List<GymCustomer> customers = new ArrayList<>();
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SEARCH_CUSTOMERS_BY_NAME)) {
            
            pstmt.setString(1, "%" + name + "%");
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    customers.add(mapResultSetToCustomer(rs));
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error searching customers by name: " + e.getMessage());
        }
        
        return customers;
    }
    
    @Override
    public List<GymCustomer> getCustomersByLocation(String location) {
        List<GymCustomer> customers = new ArrayList<>();
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SELECT_CUSTOMERS_BY_LOCATION)) {
            
            pstmt.setString(1, "%" + location + "%");
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    customers.add(mapResultSetToCustomer(rs));
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting customers by location: " + e.getMessage());
        }
        
        return customers;
    }
    
    @Override
    public boolean updateCustomer(GymCustomer customer) {
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(UPDATE_CUSTOMER)) {
            
            pstmt.setString(1, customer.getName());
            pstmt.setString(2, customer.getEmail());
            pstmt.setString(3, customer.getPassword());
            pstmt.setString(4, customer.getMobileNo());
            pstmt.setString(5, customer.getAddress());
            pstmt.setString(6, customer.getOtherDetails());
            pstmt.setString(7, customer.getId());
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error updating gym customer: " + e.getMessage());
            return false;
        }
    }
    
    @Override
    public boolean updateCustomerPassword(String customerId, String newPassword) {
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(UPDATE_PASSWORD)) {
            
            pstmt.setString(1, newPassword);
            pstmt.setString(2, customerId);
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error updating customer password: " + e.getMessage());
            return false;
        }
    }
    
    @Override
    public boolean deleteCustomer(String customerId) {
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(DELETE_CUSTOMER)) {
            
            pstmt.setString(1, customerId);
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error deleting gym customer: " + e.getMessage());
            return false;
        }
    }
    
    @Override
    public boolean customerExists(String customerId) {
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(CHECK_CUSTOMER_EXISTS)) {
            
            pstmt.setString(1, customerId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
            
        } catch (SQLException e) {
            System.err.println("Error checking customer existence: " + e.getMessage());
            return false;
        }
    }
    
    @Override
    public boolean customerExistsByEmail(String email) {
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(CHECK_CUSTOMER_EXISTS_EMAIL)) {
            
            pstmt.setString(1, email);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
            
        } catch (SQLException e) {
            System.err.println("Error checking customer existence by email: " + e.getMessage());
            return false;
        }
    }
    
    @Override
    public Optional<String> validateCredentials(String email, String password) {
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(VALIDATE_CREDENTIALS)) {
            
            pstmt.setString(1, email);
            pstmt.setString(2, password);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(rs.getString("id"));
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error validating customer credentials: " + e.getMessage());
        }
        
        return Optional.empty();
    }
    
    @Override
    public int getCustomerCount() {
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(GET_CUSTOMER_COUNT);
             ResultSet rs = pstmt.executeQuery()) {
            
            if (rs.next()) {
                return rs.getInt(1);
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting customer count: " + e.getMessage());
        }
        
        return 0;
    }
    
    @Override
    public List<GymCustomer> getCustomersByDateRange(java.time.LocalDate startDate, java.time.LocalDate endDate) {
        List<GymCustomer> customers = new ArrayList<>();
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SELECT_CUSTOMERS_BY_DATE_RANGE)) {
            
            pstmt.setDate(1, Date.valueOf(startDate));
            pstmt.setDate(2, Date.valueOf(endDate));
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    customers.add(mapResultSetToCustomer(rs));
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting customers by date range: " + e.getMessage());
        }
        
        return customers;
    }
    
    /**
     * Helper method to map ResultSet to GymCustomer object
     */
    private GymCustomer mapResultSetToCustomer(ResultSet rs) throws SQLException {
        GymCustomer customer = new GymCustomer();
        customer.setId(rs.getString("id"));
        customer.setName(rs.getString("name"));
        customer.setEmail(rs.getString("email"));
        customer.setPassword(rs.getString("password"));
        customer.setMobileNo(rs.getString("mobile_no"));
        customer.setAddress(rs.getString("address"));
        customer.setOtherDetails(rs.getString("other_details"));
        return customer;
    }
}
