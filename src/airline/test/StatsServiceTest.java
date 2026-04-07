
package airline.test;

import airline.model.Flight;
import airline.service.StatsService;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class StatsServiceTest {

    @Test
    public void testAnalyticsLogic() {
        System.out.println("Running Test 4: Analytics Logic Check...");
        
        Flight f1 = new Flight(1, "AA101", "American", "DFW", "LAX", null, "10:00", "12:00", 30, "On Time", "Commercial", "N", "", null, "1PC", "First", "Y");
        Flight f2 = new Flight(2, "DL202", "Delta", "JFK", "ATL", null, "11:00", "13:00", 0, "Cancelled", "Commercial", "N", "", null, "1PC", "Coach", "N");
        List<Flight> mockFlights = Arrays.asList(f1, f2);
        
        StatsService service = new StatsService();
        
        // 1. Verify average calculation: (30 + 0) / 2 = 15.0
        double average = service.getAverageDelay(mockFlights);
        assertEquals(15.0, average, "The average delay calculation is incorrect.");
        
        // 2. Verify delayed flight count: Only f1 has delay > 0
        long delayedCount = service.getDelayedFlights(mockFlights);
        assertEquals(1, delayedCount, "The delayed flight count is incorrect.");

        // 3. Verify cancelled flight count: Only f2 is "Cancelled"
        long cancelledCount = service.getCancelledFlights(mockFlights);
        assertEquals(1, cancelledCount, "The cancelled flight count is incorrect.");
        
        System.out.println("SUCCESS: Analytics logic and filtering verified.");
    }
}

