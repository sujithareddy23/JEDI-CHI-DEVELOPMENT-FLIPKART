package com.flipfit.dao.impl;

import com.flipfit.bean.GymOwner;
import com.flipfit.dao.GymOwnerDAO;
import com.flipfit.util.DatabaseUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implementation of GymOwnerDAO interface
 * Provides database operations for gym owner entities
 */
public class GymOwnerDAOImpl implements GymOwnerDAO {
    
    private static final String INSERT_OWNER = 
        "INSERT INTO gym_owners (id, owner_name, email_id, password, id_proof, pan_no, gst_no, validated) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
    
    private static final String SELECT_OWNER_BY_ID = 
        "SELECT id, owner_name, email_id, password, id_proof, pan_no, gst_no, validated, created_at, updated_at FROM gym_owners WHERE id = ?";
    
    private static final String SELECT_OWNER_BY_EMAIL = 
        "SELECT id, owner_name, email_id, password, id_proof, pan_no, gst_no, validated, created_at, updated_at FROM gym_owners WHERE email_id = ?";
    
    private static final String SELECT_OWNER_BY_PAN = 
        "SELECT id, owner_name, email_id, password, id_proof, pan_no, gst_no, validated, created_at, updated_at FROM gym_owners WHERE pan_no = ?";
    
    private static final String SELECT_ALL_OWNERS = 
        "SELECT id, owner_name, email_id, password, id_proof, pan_no, gst_no, validated, created_at, updated_at FROM gym_owners ORDER BY owner_name";
    
    private static final String SELECT_OWNERS_BY_VALIDATION = 
        "SELECT id, owner_name, email_id, password, id_proof, pan_no, gst_no, validated, created_at, updated_at FROM gym_owners WHERE validated = ? ORDER BY owner_name";
    
    private static final String UPDATE_OWNER = 
        "UPDATE gym_owners SET owner_name = ?, email_id = ?, password = ?, id_proof = ?, pan_no = ?, gst_no = ?, updated_at = CURRENT_TIMESTAMP WHERE id = ?";
    
    private static final String UPDATE_VALIDATION = 
        "UPDATE gym_owners SET validated = ?, updated_at = CURRENT_TIMESTAMP WHERE id = ?";
    
    private static final String DELETE_OWNER = 
        "DELETE FROM gym_owners WHERE id = ?";
    
    private static final String CHECK_OWNER_EXISTS = 
        "SELECT COUNT(*) FROM gym_owners WHERE id = ?";
    
    private static final String CHECK_OWNER_EXISTS_EMAIL = 
        "SELECT COUNT(*) FROM gym_owners WHERE email_id = ?";
    
    private static final String VALIDATE_CREDENTIALS = 
        "SELECT id FROM gym_owners WHERE email_id = ? AND password = ?";
    
    private static final String GET_OWNER_COUNT = 
        "SELECT COUNT(*) FROM gym_owners";
    
    private static final String GET_OWNER_COUNT_BY_VALIDATION = 
        "SELECT COUNT(*) FROM gym_owners WHERE validated = ?";
    
    @Override
    public boolean createOwner(GymOwner owner) {
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(INSERT_OWNER)) {
            
            pstmt.setString(1, owner.getId());
            pstmt.setString(2, owner.getOwnerName());
            pstmt.setString(3, owner.getEmailId());
            pstmt.setString(4, owner.getPassword());
            pstmt.setString(5, owner.getIdProof());
            pstmt.setString(6, owner.getPanNo());
            pstmt.setString(7, owner.getGstNo());
            pstmt.setBoolean(8, owner.isValidated());
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error creating gym owner: " + e.getMessage());
            return false;
        }
    }
    
    @Override
    public Optional<GymOwner> getOwnerById(String ownerId) {
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SELECT_OWNER_BY_ID)) {
            
            pstmt.setString(1, ownerId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToOwner(rs));
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting owner by ID: " + e.getMessage());
        }
        
        return Optional.empty();
    }
    
    @Override
    public Optional<GymOwner> getOwnerByEmail(String emailId) {
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SELECT_OWNER_BY_EMAIL)) {
            
            pstmt.setString(1, emailId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToOwner(rs));
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting owner by email: " + e.getMessage());
        }
        
        return Optional.empty();
    }
    
    @Override
    public Optional<GymOwner> getOwnerByPan(String panNo) {
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SELECT_OWNER_BY_PAN)) {
            
            pstmt.setString(1, panNo);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToOwner(rs));
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting owner by PAN: " + e.getMessage());
        }
        
        return Optional.empty();
    }
    
    @Override
    public List<GymOwner> getAllOwners() {
        List<GymOwner> owners = new ArrayList<>();
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SELECT_ALL_OWNERS);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                owners.add(mapResultSetToOwner(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting all owners: " + e.getMessage());
        }
        
        return owners;
    }
    
    @Override
    public List<GymOwner> getOwnersByValidationStatus(boolean validated) {
        List<GymOwner> owners = new ArrayList<>();
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SELECT_OWNERS_BY_VALIDATION)) {
            
            pstmt.setBoolean(1, validated);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    owners.add(mapResultSetToOwner(rs));
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting owners by validation status: " + e.getMessage());
        }
        
        return owners;
    }
    
    @Override
    public boolean updateOwner(GymOwner owner) {
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(UPDATE_OWNER)) {
            
            pstmt.setString(1, owner.getOwnerName());
            pstmt.setString(2, owner.getEmailId());
            pstmt.setString(3, owner.getPassword());
            pstmt.setString(4, owner.getIdProof());
            pstmt.setString(5, owner.getPanNo());
            pstmt.setString(6, owner.getGstNo());
            pstmt.setString(7, owner.getId());
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error updating gym owner: " + e.getMessage());
            return false;
        }
    }
    
    @Override
    public boolean updateOwnerValidation(String ownerId, boolean validated) {
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(UPDATE_VALIDATION)) {
            
            pstmt.setBoolean(1, validated);
            pstmt.setString(2, ownerId);
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error updating owner validation: " + e.getMessage());
            return false;
        }
    }
    
    @Override
    public boolean deleteOwner(String ownerId) {
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(DELETE_OWNER)) {
            
            pstmt.setString(1, ownerId);
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error deleting gym owner: " + e.getMessage());
            return false;
        }
    }
    
    @Override
    public boolean ownerExists(String ownerId) {
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(CHECK_OWNER_EXISTS)) {
            
            pstmt.setString(1, ownerId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
            
        } catch (SQLException e) {
            System.err.println("Error checking owner existence: " + e.getMessage());
            return false;
        }
    }
    
    @Override
    public boolean ownerExistsByEmail(String emailId) {
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(CHECK_OWNER_EXISTS_EMAIL)) {
            
            pstmt.setString(1, emailId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
            
        } catch (SQLException e) {
            System.err.println("Error checking owner existence by email: " + e.getMessage());
            return false;
        }
    }
    
    @Override
    public Optional<String> validateCredentials(String emailId, String password) {
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(VALIDATE_CREDENTIALS)) {
            
            pstmt.setString(1, emailId);
            pstmt.setString(2, password);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(rs.getString("id"));
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error validating owner credentials: " + e.getMessage());
        }
        
        return Optional.empty();
    }
    
    @Override
    public int getOwnerCount() {
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(GET_OWNER_COUNT);
             ResultSet rs = pstmt.executeQuery()) {
            
            if (rs.next()) {
                return rs.getInt(1);
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting owner count: " + e.getMessage());
        }
        
        return 0;
    }
    
    @Override
    public int getOwnerCountByValidation(boolean validated) {
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(GET_OWNER_COUNT_BY_VALIDATION)) {
            
            pstmt.setBoolean(1, validated);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting owner count by validation: " + e.getMessage());
        }
        
        return 0;
    }
    
    /**
     * Helper method to map ResultSet to GymOwner object
     */
    private GymOwner mapResultSetToOwner(ResultSet rs) throws SQLException {
        GymOwner owner = new GymOwner();
        owner.setId(rs.getString("id"));
        owner.setOwnerName(rs.getString("owner_name"));
        owner.setEmailId(rs.getString("email_id"));
        owner.setPassword(rs.getString("password"));
        owner.setIdProof(rs.getString("id_proof"));
        owner.setPanNo(rs.getString("pan_no"));
        owner.setGstNo(rs.getString("gst_no"));
        owner.setValidated(rs.getBoolean("validated"));
        return owner;
    }
}
