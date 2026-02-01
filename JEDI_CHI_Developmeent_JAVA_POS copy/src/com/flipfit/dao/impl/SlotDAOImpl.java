package com.flipfit.dao.impl;

import com.flipfit.bean.Slot;
import com.flipfit.dao.SlotDAO;
import com.flipfit.util.DatabaseUtil;

import java.sql.*;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implementation of SlotDAO interface
 * Provides database operations for slot entities
 */
public class SlotDAOImpl implements SlotDAO {
    
    private static final String INSERT_SLOT = 
        "INSERT INTO slots (slot_id, gym_id, total_capacity, start_time, end_time, is_active) VALUES (?, ?, ?, ?, ?, ?)";
    
    private static final String SELECT_SLOT_BY_ID = 
        "SELECT slot_id, gym_id, total_capacity, start_time, end_time, is_active, created_at, updated_at FROM slots WHERE slot_id = ?";
    
    private static final String SELECT_ALL_SLOTS = 
        "SELECT slot_id, gym_id, total_capacity, start_time, end_time, is_active, created_at, updated_at FROM slots ORDER BY gym_id, start_time";
    
    private static final String SELECT_SLOTS_BY_GYM = 
        "SELECT slot_id, gym_id, total_capacity, start_time, end_time, is_active, created_at, updated_at FROM slots WHERE gym_id = ? ORDER BY start_time";
    
    private static final String SELECT_ACTIVE_SLOTS_BY_GYM = 
        "SELECT slot_id, gym_id, total_capacity, start_time, end_time, is_active, created_at, updated_at FROM slots WHERE gym_id = ? AND is_active = true ORDER BY start_time";
    
    private static final String UPDATE_SLOT = 
        "UPDATE slots SET gym_id = ?, total_capacity = ?, start_time = ?, end_time = ?, is_active = ?, updated_at = CURRENT_TIMESTAMP WHERE slot_id = ?";
    
    private static final String DELETE_SLOT = 
        "DELETE FROM slots WHERE slot_id = ?";
    
    private static final String CHECK_SLOT_EXISTS = 
        "SELECT COUNT(*) FROM slots WHERE slot_id = ?";
    
    private static final String GET_SLOT_COUNT = 
        "SELECT COUNT(*) FROM slots";
    
    private static final String GET_SLOT_COUNT_BY_GYM = 
        "SELECT COUNT(*) FROM slots WHERE gym_id = ?";
    
    private static final String UPDATE_SLOT_STATUS = 
        "UPDATE slots SET is_active = ?, updated_at = CURRENT_TIMESTAMP WHERE slot_id = ?";
    
    private static final String SELECT_SLOTS_BY_TIME_RANGE = 
        "SELECT slot_id, gym_id, total_capacity, start_time, end_time, is_active, created_at, updated_at FROM slots WHERE gym_id = ? AND start_time >= ? AND end_time <= ? ORDER BY start_time";
    
    @Override
    public boolean createSlot(Slot slot) {
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(INSERT_SLOT)) {
            
            pstmt.setString(1, slot.getSlotId());
            pstmt.setString(2, slot.getGymId());
            pstmt.setInt(3, slot.getTotalCapacity());
            pstmt.setTime(4, Time.valueOf(slot.getStartTime()));
            pstmt.setTime(5, Time.valueOf(slot.getEndTime()));
            pstmt.setBoolean(6, true); // Default to active
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error creating slot: " + e.getMessage());
            return false;
        }
    }
    
    @Override
    public Optional<Slot> getSlotById(String slotId) {
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SELECT_SLOT_BY_ID)) {
            
            pstmt.setString(1, slotId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToSlot(rs));
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting slot by ID: " + e.getMessage());
        }
        
        return Optional.empty();
    }
    
    @Override
    public List<Slot> getAllSlots() {
        List<Slot> slots = new ArrayList<>();
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SELECT_ALL_SLOTS);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                slots.add(mapResultSetToSlot(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting all slots: " + e.getMessage());
        }
        
        return slots;
    }
    
    @Override
    public List<Slot> getSlotsByGym(String gymId) {
        List<Slot> slots = new ArrayList<>();
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SELECT_SLOTS_BY_GYM)) {
            
            pstmt.setString(1, gymId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    slots.add(mapResultSetToSlot(rs));
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting slots by gym: " + e.getMessage());
        }
        
        return slots;
    }
    
    @Override
    public List<Slot> getActiveSlotsByGym(String gymId) {
        List<Slot> slots = new ArrayList<>();
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SELECT_ACTIVE_SLOTS_BY_GYM)) {
            
            pstmt.setString(1, gymId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    slots.add(mapResultSetToSlot(rs));
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting active slots by gym: " + e.getMessage());
        }
        
        return slots;
    }
    
    @Override
    public boolean updateSlot(Slot slot) {
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(UPDATE_SLOT)) {
            
            pstmt.setString(1, slot.getGymId());
            pstmt.setInt(2, slot.getTotalCapacity());
            pstmt.setTime(3, Time.valueOf(slot.getStartTime()));
            pstmt.setTime(4, Time.valueOf(slot.getEndTime()));
            pstmt.setBoolean(5, true); // Default to active
            pstmt.setString(6, slot.getSlotId());
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error updating slot: " + e.getMessage());
            return false;
        }
    }
    
    @Override
    public boolean deleteSlot(String slotId) {
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(DELETE_SLOT)) {
            
            pstmt.setString(1, slotId);
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error deleting slot: " + e.getMessage());
            return false;
        }
    }
    
    @Override
    public boolean slotExists(String slotId) {
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(CHECK_SLOT_EXISTS)) {
            
            pstmt.setString(1, slotId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
            
        } catch (SQLException e) {
            System.err.println("Error checking slot existence: " + e.getMessage());
            return false;
        }
    }
    
    @Override
    public int getSlotCount() {
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(GET_SLOT_COUNT);
             ResultSet rs = pstmt.executeQuery()) {
            
            if (rs.next()) {
                return rs.getInt(1);
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting slot count: " + e.getMessage());
        }
        
        return 0;
    }
    
    @Override
    public int getSlotCountByGym(String gymId) {
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(GET_SLOT_COUNT_BY_GYM)) {
            
            pstmt.setString(1, gymId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting slot count by gym: " + e.getMessage());
        }
        
        return 0;
    }
    
    @Override
    public boolean updateSlotStatus(String slotId, boolean active) {
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(UPDATE_SLOT_STATUS)) {
            
            pstmt.setBoolean(1, active);
            pstmt.setString(2, slotId);
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error updating slot status: " + e.getMessage());
            return false;
        }
    }
    
    @Override
    public List<Slot> getSlotsByTimeRange(String gymId, LocalTime startTime, LocalTime endTime) {
        List<Slot> slots = new ArrayList<>();
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SELECT_SLOTS_BY_TIME_RANGE)) {
            
            pstmt.setString(1, gymId);
            pstmt.setTime(2, Time.valueOf(startTime));
            pstmt.setTime(3, Time.valueOf(endTime));
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    slots.add(mapResultSetToSlot(rs));
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting slots by time range: " + e.getMessage());
        }
        
        return slots;
    }
    
    /**
     * Helper method to map ResultSet to Slot object
     */
    private Slot mapResultSetToSlot(ResultSet rs) throws SQLException {
        Slot slot = new Slot();
        slot.setSlotId(rs.getString("slot_id"));
        slot.setGymId(rs.getString("gym_id"));
        slot.setTotalCapacity(rs.getInt("total_capacity"));
        slot.setStartTime(rs.getTime("start_time").toLocalTime());
        slot.setEndTime(rs.getTime("end_time").toLocalTime());
        return slot;
    }
}
