package com.flipfit.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Utility class for database connection management
 * Provides methods to get database connections and manage connection properties
 */
public class DatabaseUtil {
    
    // Database configuration constants
    private static final String DB_URL = "jdbc:mysql://localhost:3306/flipfit_gym";
    private static final String DB_USERNAME = "root";
    private static final String DB_PASSWORD = "";
    
    // JDBC driver class
    private static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    
    // Connection properties for better performance and security
    private static final Properties connectionProperties;
    
    static {
        // Load the JDBC driver
        try {
            Class.forName(JDBC_DRIVER);
        } catch (ClassNotFoundException e) {
            System.err.println("Failed to load MySQL JDBC driver: " + e.getMessage());
            throw new RuntimeException("MySQL JDBC Driver not found", e);
        }
        
        // Initialize connection properties
        connectionProperties = new Properties();
        connectionProperties.setProperty("user", DB_USERNAME);
        connectionProperties.setProperty("password", DB_PASSWORD);
        connectionProperties.setProperty("useSSL", "false");
        connectionProperties.setProperty("allowPublicKeyRetrieval", "true");
        connectionProperties.setProperty("serverTimezone", "UTC");
        connectionProperties.setProperty("useUnicode", "true");
        connectionProperties.setProperty("characterEncoding", "UTF-8");
        connectionProperties.setProperty("autoReconnect", "true");
        connectionProperties.setProperty("maxReconnects", "3");
        connectionProperties.setProperty("initialTimeout", "10");
        connectionProperties.setProperty("connectTimeout", "10000");
        connectionProperties.setProperty("socketTimeout", "30000");
    }
    
    /**
     * Get a database connection
     * @return Connection object to the FlipFit gym database
     * @throws SQLException if connection fails
     */
    public static Connection getConnection() throws SQLException {
        try {
            return DriverManager.getConnection(DB_URL, connectionProperties);
        } catch (SQLException e) {
            System.err.println("Failed to connect to database: " + e.getMessage());
            System.err.println("Database URL: " + DB_URL);
            System.err.println("Username: " + DB_USERNAME);
            throw e;
        }
    }
    
    /**
     * Get a database connection with custom credentials
     * @param username Database username
     * @param password Database password
     * @return Connection object to the FlipFit gym database
     * @throws SQLException if connection fails
     */
    public static Connection getConnection(String username, String password) throws SQLException {
        Properties props = new Properties(connectionProperties);
        props.setProperty("user", username);
        props.setProperty("password", password);
        
        try {
            return DriverManager.getConnection(DB_URL, props);
        } catch (SQLException e) {
            System.err.println("Failed to connect to database with custom credentials: " + e.getMessage());
            throw e;
        }
    }
    
    /**
     * Test the database connection
     * @return true if connection is successful, false otherwise
     */
    public static boolean testConnection() {
        try (Connection conn = getConnection()) {
            return conn != null && !conn.isClosed() && conn.isValid(5); // 5 second timeout
        } catch (SQLException e) {
            System.err.println("Database connection test failed: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Get database URL
     * @return Database connection URL
     */
    public static String getDatabaseUrl() {
        return DB_URL;
    }
    
    /**
     * Get database username
     * @return Database username
     */
    public static String getDatabaseUsername() {
        return DB_USERNAME;
    }
    
    /**
     * Close a connection safely
     * @param connection Connection to close
     */
    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                if (!connection.isClosed()) {
                    connection.close();
                }
            } catch (SQLException e) {
                System.err.println("Error closing database connection: " + e.getMessage());
            }
        }
    }
    
    /**
     * Rollback a transaction safely
     * @param connection Connection with active transaction
     */
    public static void rollbackTransaction(Connection connection) {
        if (connection != null) {
            try {
                if (!connection.isClosed()) {
                    connection.rollback();
                }
            } catch (SQLException e) {
                System.err.println("Error rolling back transaction: " + e.getMessage());
            }
        }
    }
    
    /**
     * Get database metadata information
     * @return String with database version and information
     */
    public static String getDatabaseInfo() {
        try (Connection conn = getConnection()) {
            return String.format(
                "Database Info: %s %s, URL: %s, User: %s",
                conn.getMetaData().getDatabaseProductName(),
                conn.getMetaData().getDatabaseProductVersion(),
                DB_URL,
                DB_USERNAME
            );
        } catch (SQLException e) {
            return "Error getting database info: " + e.getMessage();
        }
    }
    
    /**
     * Main method for testing database connection
     * @param args Command line arguments
     */
    public static void main(String[] args) {
        System.out.println("Testing FlipFit Database Connection...");
        System.out.println("Database URL: " + DB_URL);
        
        if (testConnection()) {
            System.out.println("✅ Database connection successful!");
            System.out.println(getDatabaseInfo());
        } else {
            System.out.println("❌ Database connection failed!");
            System.out.println("Please ensure:");
            System.out.println("1. MySQL server is running");
            System.out.println("2. Database 'flipfit_gym' exists");
            System.out.println("3. User 'flipfit_app' has proper permissions");
            System.out.println("4. MySQL JDBC connector is in classpath");
        }
    }
}
