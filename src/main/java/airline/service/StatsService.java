/*
 * ================================================================
 * University: University of Maryland Global Campus (UMGC)
 * Course: CMSC 495 – Computer Science Capstone
 * Project: Airline Operations Monitoring Dashboard
 *
 * File: StatsService.java
 *
 * Team Members:
 *   Ruth Mathewos
 *   Will Vernigor
 *   Afsana Abdul
 *   Robby Allen
 *   Casey Leonard
 *
 * Author: Casey Leonard
 * Revised By: Will Vernigor
 * Revised By: Ruth Mathewos
 *
 * Created: 2026-03-16
 * Last Revised: 2026-04-12
 *
 * Description:
 * Provides statistical calculations for the dashboard including
 * total flights, delayed flights, cancelled flights, and average delay.
 * Also supports chart data and summary generation.
 *
 * ================================================================
 */

package airline.service;


import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.Collections;


import airline.model.Flight;


public class StatsService {


    /**
     * Returns the total number of flights.
     */
    public int getTotalFlights(List<Flight> flights) {
        return flights == null ? 0 : flights.size();
    }


    /**
     * Returns the number of delayed flights (delay > 0).
     */
    public long getDelayedFlights(List<Flight> flights) {
        if (flights == null || flights.isEmpty()) return 0;
        return flights.stream()
                .filter(f -> f.getDelayMinutes() > 0)
                .count();
    }


    /**
     * Returns the number of cancelled flights.
     */
    public long getCancelledFlights(List<Flight> flights) {
        if (flights == null || flights.isEmpty()) return 0;
        return flights.stream()
                .filter(f -> "Cancelled".equalsIgnoreCase(f.getStatus()))
                .count();
    }


    /**
     * Returns the number of on-time flights.
     */
    public long getOnTimeFlights(List<Flight> flights) {
        if (flights == null || flights.isEmpty()) return 0;
        return flights.stream()
                .filter(f -> "On Time".equalsIgnoreCase(f.getStatus()))
                .count();
    }


    /**
     * Returns the number of non-stop flights.
     */
    public long getNonStopFlights(List<Flight> flights) {
        if (flights == null || flights.isEmpty()) return 0;
        return flights.stream()
                .filter(f -> "Non-stop".equalsIgnoreCase(f.getFlightType()))
                .count();
    }


    /**
     * Returns the number of layover flights.
     */
    public long getLayoverFlights(List<Flight> flights) {
        if (flights == null || flights.isEmpty()) return 0;
        return flights.stream()
                .filter(f -> "Layover".equalsIgnoreCase(f.getFlightType()))
                .count();
    }


    /**
     * Calculates the average delay time across flights.
     */
    public double getAverageDelay(List<Flight> flights) {
        if (flights == null || flights.isEmpty()) return 0;
        return flights.stream()
                .mapToInt(Flight::getDelayMinutes)
                .average()
                .orElse(0);
    }


    /**
     * Calculates the percentage of on-time flights.
     */
    public double getOnTimePercentage(List<Flight> flights) {
        if (flights == null || flights.isEmpty()) return 0;
        long onTime = getOnTimeFlights(flights);
        return (onTime * 100.0) / flights.size();
    }


    /**
     * NEW PHASE II FEATURE: Returns the top N flights with longest delays.
     */
    public List<Flight> getTopDelays(List<Flight> flights, int limit) {
        if (flights == null || flights.isEmpty()) return Collections.emptyList();
        return flights.stream()
                .sorted((f1, f2) -> Integer.compare(f2.getDelayMinutes(), f1.getDelayMinutes()))
                .limit(limit)
                .collect(Collectors.toList());
    }


    /**
     * NEW PHASE II FEATURE: Formats flight data into a CSV string.
     */
    public String convertToCSV(List<Flight> flights) {
        if (flights == null || flights.isEmpty()) {
            return "FlightNumber,Airline,Status,DelayMinutes";
        }
        StringBuilder csv = new StringBuilder("FlightNumber,Airline,Status,DelayMinutes\n");
        for (Flight f : flights) {
            csv.append(f.getFlightNumber()).append(",")
               .append(f.getAirline()).append(",")
               .append(f.getStatus()).append(",")
               .append(f.getDelayMinutes()).append("\n");
        }
        return csv.toString();
    }


    /**
     * Returns status counts for charting.
     */
    public Map<String, Long> getStatusCounts(List<Flight> flights) {
        Map<String, Long> statusCounts = new LinkedHashMap<>();
        statusCounts.put("On Time", getOnTimeFlights(flights));
        statusCounts.put("Delayed", getDelayedFlights(flights));
        statusCounts.put("Cancelled", getCancelledFlights(flights));
        return statusCounts;
    }


    /**
     * Returns total delay minutes grouped by airline.
     */
    public Map<String, Integer> getDelayMinutesByAirline(List<Flight> flights) {
        if (flights == null || flights.isEmpty()) return new LinkedHashMap<>();
        return flights.stream()
                .collect(Collectors.groupingBy(
                        Flight::getAirline,
                        Collectors.summingInt(Flight::getDelayMinutes)
                ));
    }
    
    /**
     * NEW PHASE II FEATURE: Measures system latency for performance monitoring.
     * returns the duration in milliseconds.
     */
    public long measureSystemLatency(List<Flight> flights) {
        long startTime = System.currentTimeMillis();
        
        // Execute a summary build to simulate processing load
        buildStatsSummary(flights);
        
        long endTime = System.currentTimeMillis();
        return endTime - startTime;
    }


    /**
     * Builds formatted summary text for the stats panel.
     */
    public String buildStatsSummary(List<Flight> flights) {
        int total = getTotalFlights(flights);
        long onTime = getOnTimeFlights(flights);
        long delayed = getDelayedFlights(flights);
        long cancelled = getCancelledFlights(flights);
        long nonStop = getNonStopFlights(flights);
        long layover = getLayoverFlights(flights);
        double avgDelay = getAverageDelay(flights);
        double onTimePercent = getOnTimePercentage(flights);


        return "📊 FLIGHT SUMMARY\n\n" +
                "Flights: " + total + "\n" +
                "On-Time: " + Math.round(onTimePercent) + "%\n" +
                "Avg Delay: " + Math.round(avgDelay) + " min\n" +
                "On-Time Flights: " + onTime + "\n" +
                "Delayed: " + delayed + "\n" +
                "Cancelled: " + cancelled + "\n" +
                "Non-stop: " + nonStop + "\n" +
                "Layover: " + layover + "\n\n";
    }
}


