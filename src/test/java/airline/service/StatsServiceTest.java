/**
 * Project: Airline Dashboard - Phase II
 * File: StatsServiceTest.java
 * Description: Validates statistical calculations and system latency.
 * Requirement: Latency: Ensures end-to-end processing is < 500ms.
 */

package airline.service;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import airline.model.Flight;
import java.util.Arrays;
import java.util.List;
import airline.db.DatabaseConnection;
import airline.dao.FlightDAO;



import static org.junit.jupiter.api.Assertions.*;


@DisplayName("Phase II: StatsService Automation Tests")
public class StatsServiceTest {


    private StatsService service;
    private List<Flight> mockFlights;


    @BeforeEach
    void setUp() {
        service = new StatsService();
        
        // f1 (30m delay), f2 (Cancelled), f3 (60m delay)
        Flight f1 = new Flight(1, "AA101", "American", "DFW", "LAX", null, "10:00", "12:00", 30, "On Time", "Commercial", "N", "", null, "1PC", "First", "Y");
        Flight f2 = new Flight(2, "DL202", "Delta", "JFK", "ATL", null, "11:00", "13:00", 0, "Cancelled", "Commercial", "N", "", null, "1PC", "Coach", "N");
        Flight f3 = new Flight(3, "UA303", "United", "ORD", "SFO", null, "12:00", "14:00", 60, "Delayed", "Commercial", "N", "", null, "1PC", "First", "Y");
        
        mockFlights = Arrays.asList(f1, f2, f3);
    }


    @Test
    @DisplayName("Verify Average Delay Calculation")
    void testAverageDelay() {
        assertEquals(30.0, service.getAverageDelay(mockFlights), "Average should be (30+0+60)/3 = 30.0");
    }


    @Test
    @DisplayName("Verify Cancelled Flight Filtering")
    void testCancelledCount() {
        assertEquals(1, service.getCancelledFlights(mockFlights), "Should find 1 cancelled flight.");
    }


    @Test
    @DisplayName("Verify Top Delays Sorting (New Phase II Feature)")
    void testTopDelays() {
        List<Flight> top = service.getTopDelays(mockFlights, 2);
        assertEquals(2, top.size());
        assertEquals("UA303", top.get(0).getFlightNumber(), "The 60min delay should be first.");
    }


    @Test
    @DisplayName("Verify CSV Export Formatting (New Phase II Feature)")
    void testCSVFormatting() {
        String csv = service.convertToCSV(mockFlights);
        assertTrue(csv.contains("AA101"), "CSV should contain flight data.");
        assertTrue(csv.startsWith("FlightNumber"), "CSV should have correct headers.");
    }
    
    @Test
    @DisplayName("CSV Export - Content Validation")
    void testCSVContent() {
        String csv = service.convertToCSV(mockFlights);
        String[] lines = csv.split("\n");
        
        // Check header
        assertEquals("FlightNumber,Airline,Status,DelayMinutes", lines[0]);
        // Check data row 1
        assertTrue(lines[1].contains("AA101"), "First data row should contain flight AA101");
    }

    @Test
    @DisplayName("Phase II: System Latency Metric")
    void testSystemLatency() throws Exception { 
        // Setup - Connect to the real DB
        java.sql.Connection conn = airline.db.DatabaseConnection.getConnection();
        airline.dao.FlightDAO dao = new airline.dao.FlightDAO(conn);
        
        // Timer Start
        long startTime = System.currentTimeMillis();
        
        // Action - Fetch real data
        java.util.List<airline.model.Flight> flights = dao.getAllFlights();
        service.buildStatsSummary(flights); 
        
        // Timer Stop
        long duration = System.currentTimeMillis() - startTime;
        
        // Output
        System.out.println(">>> Measured Latency: " + duration + "ms");
        
        // Verification
        assertNotNull(flights, "Database returned null flights.");
        assertTrue(duration < 500, "Latency is too high!");
    }


}


