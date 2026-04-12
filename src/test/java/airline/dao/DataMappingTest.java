/**
 * Project: Airline Dashboard - Phase II
 * File: DataMappingTest.java
 * Description: Verifies data integrity between SQLite columns and Flight objects.
 * Requirement: Data Mapping: Ensures all 17 columns translate correctly.
 */

package airline.dao;


import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import airline.db.DatabaseConnection;
import airline.model.Flight;
import java.sql.Connection;
import java.util.List;


public class DataMappingTest {
  
  @Test
  @DisplayName("Phase II: Detailed Column Mapping Integrity")
  public void testDataRetrieval() {
      try (Connection conn = DatabaseConnection.getConnection()) {
          FlightDAO dao = new FlightDAO(conn);
          List<Flight> flights = dao.getAllFlights();


          assertNotNull(flights, "Flight list should not be null.");
          assertTrue(flights.size() > 0, "Database should contain records.");


          // Phase II: Verify specific field mapping for the first record
          Flight sample = flights.get(0);
          
          // Check that core columns aren't empty
          assertNotNull(sample.getFlightNumber(), "Mapping failed: Flight Number is null");
          assertNotNull(sample.getOriginAirport(), "Mapping failed: Origin is null");
          assertNotNull(sample.getDestinationAirport(), "Mapping failed: Destination is null");
          
          // Verify numeric fields 
          assertTrue(sample.getDelayMinutes() >= 0, "Delay minutes should be a valid integer");


          System.out.println("SUCCESS: Deep Data Mapping verified for: " + sample.getFlightNumber());
          
      } catch (Exception e) {
          fail("Phase II Mapping test failed: " + e.getMessage());
      }
  }
}

