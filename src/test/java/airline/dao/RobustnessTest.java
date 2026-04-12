/**
 * Project: Airline Dashboard - Phase II
 * File: RobustnessTest.java
 * Description: Tests system behavior against empty results and invalid inputs.
 * Requirement: Robustness: Proves the app doesn't crash on edge cases.
 */

package airline.dao;


import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import airline.service.FilterService; 
import java.util.ArrayList;
import java.util.List;


public class RobustnessTest {
  
  @Test
  @DisplayName("Phase II: Empty Result Set Handling")
  public void testNoResultsFound() {
      // Simulate a search that finds nothing (flight number "REM777")
      FilterService filterService = new FilterService();
      List<airline.model.Flight> emptyList = new ArrayList<>();
      
      // Robustness: Does the filter crash when searching an empty list?
      assertDoesNotThrow(() -> {
          List<airline.model.Flight> results = filterService.filterFlights(emptyList, "REM777", "All", "All", "All");
          assertNotNull(results, "Service should return empty list, not null");
          assertEquals(0, results.size(), "Result size should be 0");
      }, "System should handle empty searches without crashing.");
      
      System.out.println("SUCCESS: Robustness check passed for empty search results.");
  }


  @Test
  @DisplayName("Phase II: Malicious Input Handling (SQL Injection Safety)")
  public void testSpecialCharacterRobustness() {
      // Testing if searching for a single quote ' breaks SQL
      String maliciousInput = "JFK' OR '1'='1"; 
      
      try (java.sql.Connection conn = airline.db.DatabaseConnection.getConnection()) {
          FlightDAO dao = new FlightDAO(conn);
          // This should just return 0 results, NOT crash the app
          List<airline.model.Flight> results = dao.searchByFlightNumber(maliciousInput);
          assertNotNull(results);
          System.out.println("SUCCESS: System safely handled special characters in search.");
      } catch (Exception e) {
          fail("System crashed on special characters: " + e.getMessage());
      }
  }
}



