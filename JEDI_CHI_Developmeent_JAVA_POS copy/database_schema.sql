-- FlipFit Gym Management System - MySQL Database Schema
-- Created for migrating from in-memory Java Collections to MySQL database

-- Create database
CREATE DATABASE IF NOT EXISTS flipfit_gym;
USE flipfit_gym;

-- Drop existing tables if they exist (for clean re-creation)
SET FOREIGN_KEY_CHECKS = 0;
DROP TABLE IF EXISTS notifications;
DROP TABLE IF EXISTS bookings;
DROP TABLE IF EXISTS slots;
DROP TABLE IF EXISTS gym_centers;
DROP TABLE IF EXISTS gym_customers;
DROP TABLE IF EXISTS gym_owners;
DROP TABLE IF EXISTS gym_admins;
SET FOREIGN_KEY_CHECKS = 1;

-- =============================================
-- 1. USER MANAGEMENT TABLES
-- =============================================

-- Gym Admins Table
CREATE TABLE gym_admins (
    admin_id VARCHAR(50) PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    password VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_admin_name (name)
);

-- Gym Owners Table
CREATE TABLE gym_owners (
    id VARCHAR(50) PRIMARY KEY,
    owner_name VARCHAR(100) NOT NULL,
    email_id VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    id_proof VARCHAR(100),
    pan_no VARCHAR(20) UNIQUE,
    gst_no VARCHAR(20),
    validated BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_owner_email (email_id),
    INDEX idx_owner_name (owner_name),
    INDEX idx_owner_validated (validated)
);

-- Gym Customers Table
CREATE TABLE gym_customers (
    id VARCHAR(50) PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    mobile_no VARCHAR(20),
    address TEXT,
    other_details TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_customer_email (email),
    INDEX idx_customer_name (name),
    INDEX idx_customer_mobile (mobile_no)
);

-- =============================================
-- 2. GYM MANAGEMENT TABLES
-- =============================================

-- Gym Centers Table
CREATE TABLE gym_centers (
    gym_id VARCHAR(50) PRIMARY KEY,
    name VARCHAR(200) NOT NULL,
    location VARCHAR(200) NOT NULL,
    contact_no VARCHAR(20),
    owner_id VARCHAR(50) NOT NULL,
    validated BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (owner_id) REFERENCES gym_owners(id) ON DELETE CASCADE,
    INDEX idx_center_location (location),
    INDEX idx_center_owner (owner_id),
    INDEX idx_center_validated (validated),
    INDEX idx_center_name (name)
);

-- Slots Table
CREATE TABLE slots (
    slot_id VARCHAR(50) PRIMARY KEY,
    gym_id VARCHAR(50) NOT NULL,
    total_capacity INT NOT NULL DEFAULT 1,
    start_time TIME NOT NULL,
    end_time TIME NOT NULL,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (gym_id) REFERENCES gym_centers(gym_id) ON DELETE CASCADE,
    INDEX idx_slot_gym (gym_id),
    INDEX idx_slot_time (start_time, end_time),
    INDEX idx_slot_active (is_active)
);

-- =============================================
-- 3. BOOKING MANAGEMENT TABLES
-- =============================================

-- Booking Status Enum
CREATE TABLE booking_status (
    status_id INT PRIMARY KEY AUTO_INCREMENT,
    status_name VARCHAR(20) UNIQUE NOT NULL
);

-- Insert booking status values
INSERT INTO booking_status (status_name) VALUES 
('CONFIRMED'),
('CANCELLED'),
('WAITLISTED');

-- Bookings Table
CREATE TABLE bookings (
    id VARCHAR(50) PRIMARY KEY,
    booking_date DATE NOT NULL,
    status_id INT NOT NULL,
    customer_id VARCHAR(50) NOT NULL,
    slot_id VARCHAR(50) NOT NULL,
    gym_id VARCHAR(50) NOT NULL,
    slot_start_time TIME NOT NULL,
    slot_end_time TIME NOT NULL,
    booking_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    payment_status VARCHAR(20) DEFAULT 'PENDING',
    payment_type VARCHAR(50),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (status_id) REFERENCES booking_status(status_id),
    FOREIGN KEY (customer_id) REFERENCES gym_customers(id) ON DELETE CASCADE,
    FOREIGN KEY (slot_id) REFERENCES slots(slot_id) ON DELETE CASCADE,
    FOREIGN KEY (gym_id) REFERENCES gym_centers(gym_id) ON DELETE CASCADE,
    INDEX idx_booking_customer (customer_id),
    INDEX idx_booking_date (booking_date),
    INDEX idx_booking_status (status_id),
    INDEX idx_booking_slot (slot_id),
    INDEX idx_booking_gym (gym_id),
    INDEX idx_booking_customer_date (customer_id, booking_date)
);

-- =============================================
-- 4. NOTIFICATION SYSTEM TABLES
-- =============================================

-- Notifications Table
CREATE TABLE notifications (
    notification_id VARCHAR(50) PRIMARY KEY,
    customer_id VARCHAR(50) NOT NULL,
    message TEXT NOT NULL,
    notification_timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    is_read BOOLEAN DEFAULT FALSE,
    notification_type VARCHAR(50) DEFAULT 'GENERAL',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (customer_id) REFERENCES gym_customers(id) ON DELETE CASCADE,
    INDEX idx_notification_customer (customer_id),
    INDEX idx_notification_read (is_read),
    INDEX idx_notification_type (notification_type),
    INDEX idx_notification_timestamp (notification_timestamp)
);

-- =============================================
-- 5. SYSTEM CONFIGURATION TABLES
-- =============================================

-- System Configuration Table
CREATE TABLE system_config (
    config_key VARCHAR(100) PRIMARY KEY,
    config_value TEXT NOT NULL,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Insert default system configuration
INSERT INTO system_config (config_key, config_value, description) VALUES
('booking_prefix', 'BK', 'Prefix for booking IDs'),
('notification_prefix', 'NT', 'Prefix for notification IDs'),
('slot_prefix', 'SL', 'Prefix for slot IDs'),
('max_booking_per_day', '5', 'Maximum bookings allowed per customer per day'),
('waitlist_limit', '10', 'Maximum waitlist entries per slot'),
('booking_cancellation_hours', '2', 'Minimum hours before booking for cancellation');

-- =============================================
-- 6. AUDIT LOGS TABLE
-- =============================================

-- Audit Logs Table
CREATE TABLE audit_logs (
    log_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id VARCHAR(50),
    user_type VARCHAR(20), -- ADMIN, OWNER, CUSTOMER
    action VARCHAR(100) NOT NULL,
    table_name VARCHAR(50),
    record_id VARCHAR(50),
    old_values JSON,
    new_values JSON,
    ip_address VARCHAR(45),
    user_agent TEXT,
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_audit_user (user_id, user_type),
    INDEX idx_audit_action (action),
    INDEX idx_audit_table (table_name),
    INDEX idx_audit_timestamp (timestamp)
);

-- =============================================
-- 7. VIEWS FOR COMMON QUERIES
-- =============================================

-- View for Gym Centers with Owner Details
CREATE VIEW gym_centers_details AS
SELECT 
    gc.gym_id,
    gc.name AS gym_name,
    gc.location,
    gc.contact_no,
    gc.validated,
    go.owner_name,
    go.email_id AS owner_email,
    go.pan_no,
    gc.created_at
FROM gym_centers gc
JOIN gym_owners go ON gc.owner_id = go.id;

-- View for Bookings with Customer and Slot Details
CREATE VIEW booking_details AS
SELECT 
    b.id AS booking_id,
    b.booking_date,
    bs.status_name AS booking_status,
    gc.name AS customer_name,
    gc.email AS customer_email,
    gc.mobile_no AS customer_mobile,
    g.name AS gym_name,
    g.location AS gym_location,
    s.start_time,
    s.end_time,
    b.payment_status,
    b.payment_type,
    b.booking_time
FROM bookings b
JOIN booking_status bs ON b.status_id = bs.status_id
JOIN gym_customers gc ON b.customer_id = gc.id
JOIN gym_centers g ON b.gym_id = g.gym_id
JOIN slots s ON b.slot_id = s.slot_id;

-- View for Available Slots by Date
CREATE VIEW available_slots AS
SELECT 
    s.slot_id,
    s.gym_id,
    g.name AS gym_name,
    g.location,
    s.start_time,
    s.end_time,
    s.total_capacity,
    COUNT(b.id) AS booked_count,
    (s.total_capacity - COUNT(b.id)) AS available_count
FROM slots s
JOIN gym_centers g ON s.gym_id = g.gym_id
LEFT JOIN bookings b ON s.slot_id = b.slot_id 
    AND b.booking_date = CURDATE() 
    AND b.status_id = (SELECT status_id FROM booking_status WHERE status_name = 'CONFIRMED')
WHERE s.is_active = TRUE
    AND g.validated = TRUE
GROUP BY s.slot_id, s.gym_id, g.name, g.location, s.start_time, s.end_time, s.total_capacity
HAVING available_count > 0;

-- =============================================
-- 8. STORED PROCEDURES
-- =============================================

DELIMITER //

-- Procedure for creating a new booking
CREATE PROCEDURE create_booking(
    IN p_customer_id VARCHAR(50),
    IN p_slot_id VARCHAR(50),
    IN p_booking_date DATE,
    IN p_payment_type VARCHAR(50)
)
BEGIN
    DECLARE v_gym_id VARCHAR(50);
    DECLARE v_slot_start TIME;
    DECLARE v_slot_end TIME;
    DECLARE v_capacity INT;
    DECLARE v_booked_count INT;
    DECLARE v_booking_id VARCHAR(50);
    DECLARE v_status_id INT;
    
    -- Get slot details
    SELECT gym_id, start_time, end_time, total_capacity 
    INTO v_gym_id, v_slot_start, v_slot_end, v_capacity
    FROM slots WHERE slot_id = p_slot_id;
    
    -- Check current bookings for this slot and date
    SELECT COUNT(*) INTO v_booked_count
    FROM bookings 
    WHERE slot_id = p_slot_id 
        AND booking_date = p_booking_date 
        AND status_id = (SELECT status_id FROM booking_status WHERE status_name = 'CONFIRMED');
    
    -- Generate booking ID
    SET v_booking_id = CONCAT('BK', DATE_FORMAT(NOW(), '%Y%m%d'), LPAD(CONNECTION_ID(), 6, '0'));
    
    -- Determine booking status
    IF v_booked_count < v_capacity THEN
        SET v_status_id = (SELECT status_id FROM booking_status WHERE status_name = 'CONFIRMED');
    ELSE
        SET v_status_id = (SELECT status_id FROM booking_status WHERE status_name = 'WAITLISTED');
    END IF;
    
    -- Insert booking
    INSERT INTO bookings (
        id, booking_date, status_id, customer_id, slot_id, gym_id,
        slot_start_time, slot_end_time, payment_type
    ) VALUES (
        v_booking_id, p_booking_date, v_status_id, p_customer_id, p_slot_id, v_gym_id,
        v_slot_start, v_slot_end, p_payment_type
    );
    
    -- Return booking ID
    SELECT v_booking_id AS booking_id, 
           (SELECT status_name FROM booking_status WHERE status_id = v_status_id) AS status;
END //

-- Procedure for cancelling a booking
CREATE PROCEDURE cancel_booking(
    IN p_booking_id VARCHAR(50),
    IN p_customer_id VARCHAR(50)
)
BEGIN
    DECLARE v_status_id INT;
    DECLARE v_waitlist_booking_id VARCHAR(50);
    
    -- Update booking status to cancelled
    UPDATE bookings 
    SET status_id = (SELECT status_id FROM booking_status WHERE status_name = 'CANCELLED')
    WHERE id = p_booking_id AND customer_id = p_customer_id;
    
    -- Check if there's a waitlisted booking for this slot
    SELECT id INTO v_waitlist_booking_id
    FROM bookings 
    WHERE slot_id = (SELECT slot_id FROM bookings WHERE id = p_booking_id)
        AND booking_date = (SELECT booking_date FROM bookings WHERE id = p_booking_id)
        AND status_id = (SELECT status_id FROM booking_status WHERE status_name = 'WAITLISTED')
    ORDER BY booking_time ASC
    LIMIT 1;
    
    -- Promote from waitlist if exists
    IF v_waitlist_booking_id IS NOT NULL THEN
        UPDATE bookings 
        SET status_id = (SELECT status_id FROM booking_status WHERE status_name = 'CONFIRMED')
        WHERE id = v_waitlist_booking_id;
        
        -- Create notification for promoted customer
        INSERT INTO notifications (notification_id, customer_id, message, notification_type)
        VALUES (
            CONCAT('NT', DATE_FORMAT(NOW(), '%Y%m%d'), LPAD(CONNECTION_ID(), 6, '0')),
            (SELECT customer_id FROM bookings WHERE id = v_waitlist_booking_id),
            CONCAT('Your waitlisted booking ', v_waitlist_booking_id, ' has been confirmed!'),
            'BOOKING_CONFIRMED'
        );
    END IF;
    
    SELECT ROW_COUNT() AS rows_affected;
END //

DELIMITER ;

-- =============================================
-- 9. TRIGGERS FOR AUDIT LOGGING
-- =============================================

DELIMITER //

-- Trigger for booking changes
CREATE TRIGGER booking_audit_insert
AFTER INSERT ON bookings
FOR EACH ROW
BEGIN
    INSERT INTO audit_logs (user_id, user_type, action, table_name, record_id, new_values)
    VALUES (
        NEW.customer_id, 
        'CUSTOMER', 
        'INSERT', 
        'bookings', 
        NEW.id, 
        JSON_OBJECT(
            'booking_id', NEW.id,
            'customer_id', NEW.customer_id,
            'slot_id', NEW.slot_id,
            'booking_date', NEW.booking_date,
            'status', (SELECT status_name FROM booking_status WHERE status_id = NEW.status_id)
        )
    );
END //

CREATE TRIGGER booking_audit_update
AFTER UPDATE ON bookings
FOR EACH ROW
BEGIN
    INSERT INTO audit_logs (user_id, user_type, action, table_name, record_id, old_values, new_values)
    VALUES (
        NEW.customer_id, 
        'CUSTOMER', 
        'UPDATE', 
        'bookings', 
        NEW.id, 
        JSON_OBJECT(
            'old_status', (SELECT status_name FROM booking_status WHERE status_id = OLD.status_id),
            'new_status', (SELECT status_name FROM booking_status WHERE status_id = NEW.status_id)
        ),
        JSON_OBJECT(
            'booking_id', NEW.id,
            'customer_id', NEW.customer_id,
            'slot_id', NEW.slot_id,
            'booking_date', NEW.booking_date,
            'status', (SELECT status_name FROM booking_status WHERE status_id = NEW.status_id)
        )
    );
END //

DELIMITER ;

-- =============================================
-- 10. SAMPLE DATA INSERTION
-- =============================================

-- Insert sample admin
INSERT INTO gym_admins (admin_id, name, password) VALUES
('admin1', 'System Administrator', 'admin123');

-- Insert sample gym owners
INSERT INTO gym_owners (id, owner_name, email_id, password, pan_no, gst_no, validated) VALUES
('owner1', 'John Smith', 'owner1@flipfit.com', 'owner123', 'PAN123456', 'GST123456', TRUE),
('owner2', 'Jane Doe', 'owner2@flipfit.com', 'owner123', 'PAN789012', 'GST789012', TRUE),
('owner3', 'Mike Johnson', 'owner3@flipfit.com', 'owner123', 'PAN345678', 'GST345678', FALSE);

-- Insert sample gym centers
INSERT INTO gym_centers (gym_id, name, location, contact_no, owner_id, validated) VALUES
('gym1', 'FitZone Bellandur', 'Bangalore - Bellandur', '+91-80-12345678', 'owner1', TRUE),
('gym2', 'PowerHouse Indiranagar', 'Bangalore - Indiranagar', '+91-80-87654321', 'owner2', TRUE),
('gym3', 'Elite Fitness Koramangala', 'Bangalore - Koramangala', '+91-80-98765432', 'owner3', FALSE);

-- Insert sample slots
INSERT INTO slots (slot_id, gym_id, total_capacity, start_time, end_time) VALUES
-- FitZone Bellandur slots
('SLgym106', 'gym1', 20, '06:00:00', '07:00:00'),
('SLgym107', 'gym1', 20, '07:00:00', '08:00:00'),
('SLgym108', 'gym1', 20, '08:00:00', '09:00:00'),
('SLgym109', 'gym1', 25, '17:00:00', '18:00:00'),
('SLgym110', 'gym1', 25, '18:00:00', '19:00:00'),
-- PowerHouse Indiranagar slots
('SLgym206', 'gym2', 15, '06:00:00', '07:00:00'),
('SLgym207', 'gym2', 15, '07:00:00', '08:00:00'),
('SLgym208', 'gym2', 20, '17:00:00', '18:00:00'),
('SLgym209', 'gym2', 20, '18:00:00', '19:00:00'),
-- Elite Fitness Koramangala slots
('SLgym306', 'gym3', 10, '06:00:00', '07:00:00'),
('SLgym307', 'gym3', 10, '07:00:00', '08:00:00');

-- Insert sample customers
INSERT INTO gym_customers (id, name, email, password, mobile_no, address) VALUES
('customer1', 'Alice Brown', 'customer1@flipfit.com', 'customer123', '+91-9876543210', 'Bangalore - Whitefield'),
('customer2', 'Bob Wilson', 'customer2@flipfit.com', 'customer123', '+91-9876543211', 'Bangalore - Marathahalli'),
('customer3', 'Charlie Davis', 'customer3@flipfit.com', 'customer123', '+91-9876543212', 'Bangalore - HSR Layout');

-- =============================================
-- 11. INDEX OPTIMIZATION
-- =============================================

-- Composite indexes for common query patterns
CREATE INDEX idx_booking_composite ON bookings(customer_id, booking_date, status_id);
CREATE INDEX idx_slot_gym_time ON slots(gym_id, start_time, end_time);
CREATE INDEX idx_notification_customer_read ON notifications(customer_id, is_read, notification_timestamp);

-- =============================================
-- 12. SECURITY AND PERFORMANCE
-- =============================================

-- Create read-only user for reporting
CREATE USER 'flipfit_readonly'@'localhost' IDENTIFIED BY 'readonly_password';
GRANT SELECT ON flipfit_gym.* TO 'flipfit_readonly'@'localhost';

-- Create application user with limited privileges
CREATE USER 'flipfit_app'@'localhost' IDENTIFIED BY 'app_password';
GRANT SELECT, INSERT, UPDATE, DELETE ON flipfit_gym.* TO 'flipfit_app'@'localhost';
GRANT EXECUTE ON PROCEDURE flipfit_gym.create_booking TO 'flipfit_app'@'localhost';
GRANT EXECUTE ON PROCEDURE flipfit_gym.cancel_booking TO 'flipfit_app'@'localhost';

-- =============================================
-- 13. BACKUP AND MAINTENANCE
-- =============================================

-- Create backup table for historical data
CREATE TABLE bookings_archive LIKE bookings;

-- Procedure for archiving old bookings
DELIMITER //
CREATE PROCEDURE archive_old_bookings()
BEGIN
    -- Move bookings older than 1 year to archive
    INSERT INTO bookings_archive 
    SELECT * FROM bookings 
    WHERE booking_date < DATE_SUB(CURDATE(), INTERVAL 1 YEAR);
    
    -- Delete archived bookings from main table
    DELETE FROM bookings 
    WHERE booking_date < DATE_SUB(CURDATE(), INTERVAL 1 YEAR);
    
    SELECT ROW_COUNT() AS archived_count;
END //
DELIMITER ;

-- =============================================
-- SCHEMA COMPLETION MESSAGE
-- =============================================

SELECT 'FlipFit Gym Management System - MySQL Schema Created Successfully!' AS status,
       COUNT(*) AS total_tables_created
FROM information_schema.tables 
WHERE table_schema = 'flipfit_gym';

-- Show summary of created tables
SELECT 
    TABLE_NAME as table_name,
    TABLE_ROWS as estimated_rows,
    DATA_LENGTH as data_size_bytes
FROM information_schema.tables 
WHERE table_schema = 'flipfit_gym' 
ORDER BY TABLE_NAME;
