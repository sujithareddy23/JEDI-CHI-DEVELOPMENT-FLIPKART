package com.flipfit.dao.impl;

import com.flipfit.bean.GymCenter;
import com.flipfit.dao.GymCenterDAO;
import com.flipfit.util.DatabaseUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implementation of GymCenterDAO interface
 * Provides database operations for gym center entities
 */
public class GymCenterDAOImpl implements GymCenterDAO {
    
    private static final String INSERT_CENTER = 
        "INSERT INTO gym_centers (gym_id, name, location, contact_no, owner_id, validated) VALUES (?, ?, ?, ?, ?, ?)";
    
    private static final String SELECT_CENTER_BY_ID = 
        "SELECT gym_id, name, location, contact_no, owner_id, validated, created_at, updated_at FROM gym_centers WHERE gym_id = ?";
    
    private static final String SELECT_ALL_CENTERS = 
        "SELECT gym_id, name, location, contact_no, owner_id, validated, created_at, updated_at FROM gym_centers ORDER BY name";
    
    private static final String SELECT_CENTERS_BY_CITY = 
        "SELECT gym_id, name, location, contact_no, owner_id, validated, created_at, updated_at FROM gym_centers WHERE location LIKE ? ORDER BY name";
    
    private static final String SELECT_CENTERS_BY_OWNER = 
        "SELECT gym_id, name, location, contact_no, owner_id, validated, created_at, updated_at FROM gym_centers WHERE owner_id = ? ORDER BY name";
    
    private static final String SELECT_CENTERS_BY_VALIDATION = 
        "SELECT gym_id, name, location, contact_no, owner_id, validated, created_at, updated_at FROM gym_centers WHERE validated = ? ORDER BY name";
    
    private static final String SELECT_CENTERS_BY_LOCATION = 
        "SELECT gym_id, name, location, contact_no, owner_id, validated, created_at, updated_at FROM gym_centers WHERE location LIKE ? ORDER BY name";
    
    private static final String SELECT_VALIDATED_CENTERS_BY_CITY = 
        "SELECT gym_id, name, location, contact_no, owner_id, validated, created_at, updated_at FROM gym_centers WHERE location LIKE ? AND validated = true ORDER BY name";
    
    private static final String UPDATE_CENTER = 
        "UPDATE gym_centers SET name = ?, location = ?, contact_no = ?, owner_id = ?, validated = ?, updated_at = CURRENT_TIMESTAMP WHERE gym_id = ?";
    
    private static final String DELETE_CENTER = 
        "DELETE FROM gym_centers WHERE gym_id = ?";
    
    private static final String CHECK_CENTER_EXISTS = 
        "SELECT COUNT(*) FROM gym_centers WHERE gym_id = ?";
    
    private static final String GET_CENTER_COUNT = 
        "SELECT COUNT(*) FROM gym_centers";
    
    private static final String GET_CENTER_COUNT_BY_VALIDATION = 
        "SELECT COUNT(*) FROM gym_centers WHERE validated = ?";
    
    private static final String GET_CENTER_COUNT_BY_OWNER = 
        "SELECT COUNT(*) FROM gym_centers WHERE owner_id = ?";
    
    private static final String UPDATE_CENTER_VALIDATION = 
        "UPDATE gym_centers SET validated = ?, updated_at = CURRENT_TIMESTAMP WHERE gym_id = ?";
    
    @Override
    public boolean createCenter(GymCenter center) {
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(INSERT_CENTER)) {
            
            pstmt.setString(1, center.getGymId());
            pstmt.setString(2, center.getName());
            pstmt.setString(3, center.getLocation());
            pstmt.setString(4, center.getContactNo());
            pstmt.setString(5, center.getOwnerId());
            pstmt.setBoolean(6, center.isValidated());
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error creating gym center: " + e.getMessage());
            return false;
        }
    }
    
    @Override
    public Optional<GymCenter> getCenterById(String gymId) {
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SELECT_CENTER_BY_ID)) {
            
            pstmt.setString(1, gymId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToCenter(rs));
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting center by ID: " + e.getMessage());
        }
        
        return Optional.empty();
    }
    
    @Override
    public List<GymCenter> getAllCenters() {
        List<GymCenter> centers = new ArrayList<>();
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SELECT_ALL_CENTERS);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                centers.add(mapResultSetToCenter(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting all centers: " + e.getMessage());
        }
        
        return centers;
    }
    
    @Override
    public List<GymCenter> getCentersByCity(String city) {
        List<GymCenter> centers = new ArrayList<>();
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SELECT_CENTERS_BY_CITY)) {
            
            pstmt.setString(1, "%" + city + "%");
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    centers.add(mapResultSetToCenter(rs));
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting centers by city: " + e.getMessage());
        }
        
        return centers;
    }
    
    @Override
    public List<GymCenter> getCentersByOwner(String ownerId) {
        List<GymCenter> centers = new ArrayList<>();
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SELECT_CENTERS_BY_OWNER)) {
            
            pstmt.setString(1, ownerId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    centers.add(mapResultSetToCenter(rs));
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting centers by owner: " + e.getMessage());
        }
        
        return centers;
    }
    
    @Override
    public List<GymCenter> getCentersByValidation(boolean validated) {
        List<GymCenter> centers = new ArrayList<>();
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SELECT_CENTERS_BY_VALIDATION)) {
            
            pstmt.setBoolean(1, validated);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    centers.add(mapResultSetToCenter(rs));
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting centers by validation: " + e.getMessage());
        }
        
        return centers;
    }
    
    @Override
    public List<GymCenter> getCentersByLocation(String location) {
        List<GymCenter> centers = new ArrayList<>();
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SELECT_CENTERS_BY_LOCATION)) {
            
            pstmt.setString(1, "%" + location + "%");
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    centers.add(mapResultSetToCenter(rs));
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting centers by location: " + e.getMessage());
        }
        
        return centers;
    }
    
    @Override
    public List<GymCenter> getValidatedCentersByCity(String city) {
        List<GymCenter> centers = new ArrayList<>();
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SELECT_VALIDATED_CENTERS_BY_CITY)) {
            
            pstmt.setString(1, "%" + city + "%");
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    centers.add(mapResultSetToCenter(rs));
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting validated centers by city: " + e.getMessage());
        }
        
        return centers;
    }
    
    @Override
    public boolean updateCenter(GymCenter center) {
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(UPDATE_CENTER)) {
            
            pstmt.setString(1, center.getName());
            pstmt.setString(2, center.getLocation());
            pstmt.setString(3, center.getContactNo());
            pstmt.setString(4, center.getOwnerId());
            pstmt.setBoolean(5, center.isValidated());
            pstmt.setString(6, center.getGymId());
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error updating gym center: " + e.getMessage());
            return false;
        }
    }
    
    @Override
    public boolean deleteCenter(String gymId) {
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(DELETE_CENTER)) {
            
            pstmt.setString(1, gymId);
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error deleting gym center: " + e.getMessage());
            return false;
        }
    }
    
    @Override
    public boolean centerExists(String gymId) {
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(CHECK_CENTER_EXISTS)) {
            
            pstmt.setString(1, gymId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
            
        } catch (SQLException e) {
            System.err.println("Error checking center existence: " + e.getMessage());
            return false;
        }
    }
    
    @Override
    public int getCenterCount() {
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(GET_CENTER_COUNT);
             ResultSet rs = pstmt.executeQuery()) {
            
            if (rs.next()) {
                return rs.getInt(1);
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting center count: " + e.getMessage());
        }
        
        return 0;
    }
    
    @Override
    public int getCenterCountByValidation(boolean validated) {
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(GET_CENTER_COUNT_BY_VALIDATION)) {
            
            pstmt.setBoolean(1, validated);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting center count by validation: " + e.getMessage());
        }
        
        return 0;
    }
    
    @Override
    public int getCenterCountByOwner(String ownerId) {
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(GET_CENTER_COUNT_BY_OWNER)) {
            
            pstmt.setString(1, ownerId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting center count by owner: " + e.getMessage());
        }
        
        return 0;
    }
    
    @Override
    public boolean updateCenterValidation(String gymId, boolean validated) {
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(UPDATE_CENTER_VALIDATION)) {
            
            pstmt.setBoolean(1, validated);
            pstmt.setString(2, gymId);
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error updating center validation: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Helper method to map ResultSet to GymCenter object
     */
    private GymCenter mapResultSetToCenter(ResultSet rs) throws SQLException {
        GymCenter center = new GymCenter();
        center.setGymId(rs.getString("gym_id"));
        center.setName(rs.getString("name"));
        center.setLocation(rs.getString("location"));
        center.setContactNo(rs.getString("contact_no"));
        center.setOwnerId(rs.getString("owner_id"));
        center.setValidated(rs.getBoolean("validated"));
        return center;
    }
}
