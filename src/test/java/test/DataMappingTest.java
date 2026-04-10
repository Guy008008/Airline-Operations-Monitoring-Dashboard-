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

public class DataMappingTest {
  
  @Test
  public void testDataRetrieval() {
      System.out.println("Running Test 2: Data Retrieval Check...");
      try (Connection conn = DatabaseConnection.getConnection()) {
          airline.dao.FlightDAO dao = new airline.dao.FlightDAO(conn);
          java.util.List<airline.model.Flight> flights = dao.getAllFlights();

          assertNotNull(flights, "Flight list should not be null.");
          // Verifies the project requirement of 30-50 records
          assertTrue(flights.size() >= 30 && flights.size() <= 50, 
              "Flight count should be between 30 and 50. Found: " + flights.size());
          
          System.out.println("SUCCESS: Retrieved " + flights.size() + " records from SQLite.");
      } catch (Exception e) {
          fail("Data retrieval test failed: " + e.getMessage());
      }
  }

}
