package airline.dao;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import airline.dao.FlightDAO;
import airline.db.DatabaseConnection;
import airline.model.Flight;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class RobustnessTest {
  
  @Test
  public void testInvalidQuery() {
      System.out.println("Running Test 3: Robustness Check (Negative Test)...");
      // Intentional error: searching for a table that does not exist
      String invalidSql = "SELECT * FROM non_existent_table";
      
      try (Connection conn = DatabaseConnection.getConnection();
           java.sql.PreparedStatement stmt = conn.prepareStatement(invalidSql)) {
          
          stmt.executeQuery();
          fail("The system should have thrown an exception for an invalid table name.");
          
      } catch (java.sql.SQLException e) {
          // The test passes because the exception was caught successfully
          assertNotNull(e.getMessage(), "SQLException should contain an error message.");
          System.out.println("SUCCESS: System gracefully caught database error: " + e.getMessage());
      }
  }
  
}
