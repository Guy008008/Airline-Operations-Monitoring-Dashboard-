/**
 * Project: Airline Dashboard - Phase II
 * File: FilterServiceTest.java
 * Description: Validates the filtering logic for the Dashboard UI.
 * Requirement: Functional Integrity - Ensures search results accurately match user input.

 */

package airline.service;


import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import airline.model.Flight;
import java.util.Arrays;
import java.util.List;


@DisplayName("Phase II: FilterService Automation Tests")
public class FilterServiceTest {


    private FilterService filterService;
    private List<Flight> mockFlights;


    @BeforeEach
    void setUp() {
        filterService = new FilterService();
        
        // Mock data for testing different filter combinations
        Flight f1 = new Flight(1, "AA101", "American", "DFW", "LAX", null, "10:00", "12:00", 0, "On Time", "Non-stop", "N", "", null, "1PC", "First", "Y");
        Flight f2 = new Flight(2, "DL202", "Delta", "JFK", "ATL", null, "11:00", "13:00", 30, "Delayed", "Layover", "N", "", null, "1PC", "Coach", "N");
        Flight f3 = new Flight(3, "UA303", "United", "ORD", "SFO", null, "12:00", "14:00", 0, "On Time", "Non-stop", "N", "", null, "1PC", "First", "Y");
        
        mockFlights = Arrays.asList(f1, f2, f3);
    }


    @Test
    @DisplayName("Test Search Text (Airline Match)")
    void testSearchFilter() {
        // Search for "Delta"
        List<Flight> result = filterService.filterFlights(mockFlights, "Delta", "All", "All", "All");
        assertEquals(1, result.size());
        assertEquals("DL202", result.get(0).getFlightNumber());
    }


    @Test
    @DisplayName("Test Status Filter (On Time)")
    void testStatusFilter() {
        List<Flight> result = filterService.filterFlights(mockFlights, "", "On Time", "All", "All");
        assertEquals(2, result.size(), "Should find 2 On Time flights");
    }


    @Test
    @DisplayName("Test Multi-Filter (Status + Route Type)")
    void testMultiFilter() {
        // Search for On Time AND Non-stop
        List<Flight> result = filterService.filterFlights(mockFlights, "", "On Time", "All", "Non-stop");
        assertEquals(2, result.size());
        
        // Search for Delayed AND Non-stop 
        List<Flight> resultEmpty = filterService.filterFlights(mockFlights, "", "Delayed", "All", "Non-stop");
        assertEquals(0, resultEmpty.size());
    }


    @Test
    @DisplayName("Test Airport Filter (Origin or Destination)")
    void testAirportFilter() {
        // Should find f2 because it departs from JFK
        List<Flight> result = filterService.filterFlights(mockFlights, "", "All", "JFK", "All");
        assertEquals(1, result.size());
        assertEquals("DL202", result.get(0).getFlightNumber());
    }


    @Test
    @DisplayName("Test Null/Empty Handling")
    void testNullInputs() {
        // Testing that the safe logic inside your service prevents NullPointerExceptions
        List<Flight> result = filterService.filterFlights(mockFlights, null, null, null, null);
        assertEquals(3, result.size(), "Null filters should return all flights (All)");
    }
}


