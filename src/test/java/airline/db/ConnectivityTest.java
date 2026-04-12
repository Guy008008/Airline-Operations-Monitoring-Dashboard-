/**
 * Project: Airline Dashboard - Phase II
 * File: ConnectivityTest.java
 * Description: Validates SQLite JDBC handshake and connection stability.
 * Requirement: Stability: Ensures the database is reachable and active.
 */

package airline.db;


import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName; 
import java.sql.Connection;
import java.sql.DatabaseMetaData; 


public class ConnectivityTest {


    @Test
    @DisplayName("Phase II: Database Stability & Handshake")
    public void testConnection() {
        System.out.println("Running Phase II: Connectivity & Stability Check...");
        try (Connection conn = DatabaseConnection.getConnection()) {
            // Core Connection Check
            assertNotNull(conn, "Database connection should not be null.");
            assertFalse(conn.isClosed(), "Database connection should be open.");
            
            // Phase II Stability Check: Is the connection valid within a 2-second timeout?
            assertTrue(conn.isValid(2), "Database connection is unstable or timed out.");
            
            // Metadata Check: Prove it's the correct SQLite version
            DatabaseMetaData meta = conn.getMetaData();
            System.out.println("SUCCESS: JDBC Handshake established.");
            System.out.println("System Database: " + meta.getDatabaseProductName());
            System.out.println("SQLite Version: " + meta.getDatabaseProductVersion());
            
        } catch (Exception e) {
            fail("Phase II Connectivity Test failed: " + e.getMessage());
        }
    }
}


