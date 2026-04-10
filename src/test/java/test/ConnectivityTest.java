package test.java.test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import airline.dao.FlightDAO;
import airline.db.DatabaseConnection;
import airline.model.Flight;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class ConnectivityTest {

  @Test
  public void testConnection() {
      System.out.println("Running Test 1: Connectivity Check...");
      try (Connection conn = DatabaseConnection.getConnection()) {
          assertNotNull(conn, "Database connection should not be null.");
          assertFalse(conn.isClosed(), "Database connection should be open.");
          System.out.println("SUCCESS: JDBC Handshake established.");
      } catch (Exception e) {
          fail("Database connection failed: " + e.getMessage());
      }
  }
  
}
