package com.flipfit.dao.impl;

import com.flipfit.bean.GymAdmin;
import com.flipfit.dao.GymAdminDAO;
import com.flipfit.util.DatabaseUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implementation of GymAdminDAO interface
 * Provides database operations for gym admin entities
 */
public class GymAdminDAOImpl implements GymAdminDAO {
    
    private static final String INSERT_ADMIN = 
        "INSERT INTO gym_admins (admin_id, name, password) VALUES (?, ?, ?)";
    
    private static final String SELECT_ADMIN_BY_ID = 
        "SELECT admin_id, name, password, created_at, updated_at FROM gym_admins WHERE admin_id = ?";
    
    private static final String SELECT_ADMIN_BY_NAME = 
        "SELECT admin_id, name, password, created_at, updated_at FROM gym_admins WHERE name = ?";
    
    private static final String SELECT_ALL_ADMINS = 
        "SELECT admin_id, name, password, created_at, updated_at FROM gym_admins ORDER BY name";
    
    private static final String UPDATE_ADMIN = 
        "UPDATE gym_admins SET name = ?, password = ?, updated_at = CURRENT_TIMESTAMP WHERE admin_id = ?";
    
    private static final String DELETE_ADMIN = 
        "DELETE FROM gym_admins WHERE admin_id = ?";
    
    private static final String CHECK_ADMIN_EXISTS = 
        "SELECT COUNT(*) FROM gym_admins WHERE admin_id = ?";
    
    private static final String VALIDATE_CREDENTIALS = 
        "SELECT COUNT(*) FROM gym_admins WHERE admin_id = ? AND password = ?";
    
    private static final String GET_ADMIN_COUNT = 
        "SELECT COUNT(*) FROM gym_admins";
    
    @Override
    public boolean createAdmin(GymAdmin admin) {
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(INSERT_ADMIN)) {
            
            pstmt.setString(1, admin.getAdminId());
            pstmt.setString(2, admin.getName());
            pstmt.setString(3, admin.getPassword());
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error creating gym admin: " + e.getMessage());
            return false;
        }
    }
    
    @Override
    public Optional<GymAdmin> getAdminById(String adminId) {
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SELECT_ADMIN_BY_ID)) {
            
            pstmt.setString(1, adminId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToAdmin(rs));
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting admin by ID: " + e.getMessage());
        }
        
        return Optional.empty();
    }
    
    @Override
    public Optional<GymAdmin> getAdminByName(String name) {
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SELECT_ADMIN_BY_NAME)) {
            
            pstmt.setString(1, name);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToAdmin(rs));
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting admin by name: " + e.getMessage());
        }
        
        return Optional.empty();
    }
    
    @Override
    public List<GymAdmin> getAllAdmins() {
        List<GymAdmin> admins = new ArrayList<>();
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SELECT_ALL_ADMINS);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                admins.add(mapResultSetToAdmin(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting all admins: " + e.getMessage());
        }
        
        return admins;
    }
    
    @Override
    public boolean updateAdmin(GymAdmin admin) {
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(UPDATE_ADMIN)) {
            
            pstmt.setString(1, admin.getName());
            pstmt.setString(2, admin.getPassword());
            pstmt.setString(3, admin.getAdminId());
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error updating gym admin: " + e.getMessage());
            return false;
        }
    }
    
    @Override
    public boolean deleteAdmin(String adminId) {
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(DELETE_ADMIN)) {
            
            pstmt.setString(1, adminId);
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error deleting gym admin: " + e.getMessage());
            return false;
        }
    }
    
    @Override
    public boolean adminExists(String adminId) {
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(CHECK_ADMIN_EXISTS)) {
            
            pstmt.setString(1, adminId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
            
        } catch (SQLException e) {
            System.err.println("Error checking admin existence: " + e.getMessage());
            return false;
        }
    }
    
    @Override
    public boolean validateCredentials(String adminId, String password) {
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(VALIDATE_CREDENTIALS)) {
            
            pstmt.setString(1, adminId);
            pstmt.setString(2, password);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
            
        } catch (SQLException e) {
            System.err.println("Error validating admin credentials: " + e.getMessage());
            return false;
        }
    }
    
    @Override
    public int getAdminCount() {
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(GET_ADMIN_COUNT);
             ResultSet rs = pstmt.executeQuery()) {
            
            if (rs.next()) {
                return rs.getInt(1);
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting admin count: " + e.getMessage());
        }
        
        return 0;
    }
    
    /**
     * Helper method to map ResultSet to GymAdmin object
     */
    private GymAdmin mapResultSetToAdmin(ResultSet rs) throws SQLException {
        GymAdmin admin = new GymAdmin();
        admin.setAdminId(rs.getString("admin_id"));
        admin.setName(rs.getString("name"));
        admin.setPassword(rs.getString("password"));
        // Note: created_at and updated_at are not in the GymAdmin bean, but available in DB
        return admin;
    }
}
