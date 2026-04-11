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
 *
 * Created: 2026-03-16
 * Last Revised: 2026-04-11
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

import airline.model.Flight;

public class StatsService {

    /**
     * Returns the total number of flights.
     */
    public int getTotalFlights(List<Flight> flights) {
        return flights == null ? 0 : flights.size();
    }

    /**
     * Returns the number of delayed flights.
     * (Based on delayMinutes > 0 to match test expectations)
     */
    public long getDelayedFlights(List<Flight> flights) {
        if (flights == null || flights.isEmpty()) {
            return 0;
        }

        return flights.stream()
                .filter(f -> f.getDelayMinutes() > 0)
                .count();
    }

    /**
     * Returns the number of cancelled flights.
     */
    public long getCancelledFlights(List<Flight> flights) {
        if (flights == null || flights.isEmpty()) {
            return 0;
        }

        return flights.stream()
                .filter(f -> "Cancelled".equalsIgnoreCase(f.getStatus()))
                .count();
    }

    /**
     * Returns the number of on-time flights.
     */
    public long getOnTimeFlights(List<Flight> flights) {
        if (flights == null || flights.isEmpty()) {
            return 0;
        }

        return flights.stream()
                .filter(f -> "On Time".equalsIgnoreCase(f.getStatus()))
                .count();
    }

    /**
     * Returns the number of non-stop flights.
     */
    public long getNonStopFlights(List<Flight> flights) {
        if (flights == null || flights.isEmpty()) {
            return 0;
        }

        return flights.stream()
                .filter(f -> "Non-stop".equalsIgnoreCase(f.getFlightType()))
                .count();
    }

    /**
     * Returns the number of layover flights.
     */
    public long getLayoverFlights(List<Flight> flights) {
        if (flights == null || flights.isEmpty()) {
            return 0;
        }

        return flights.stream()
                .filter(f -> "Layover".equalsIgnoreCase(f.getFlightType()))
                .count();
    }

    /**
     * Calculates the average delay time across flights.
     */
    public double getAverageDelay(List<Flight> flights) {
        if (flights == null || flights.isEmpty()) {
            return 0;
        }

        return flights.stream()
                .mapToInt(Flight::getDelayMinutes)
                .average()
                .orElse(0);
    }

    /**
     * Calculates the percentage of on-time flights.
     */
    public double getOnTimePercentage(List<Flight> flights) {
        if (flights == null || flights.isEmpty()) {
            return 0;
        }

        long onTime = getOnTimeFlights(flights);
        return (onTime * 100.0) / flights.size();
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
        if (flights == null || flights.isEmpty()) {
            return new LinkedHashMap<>();
        }

        return flights.stream()
                .collect(Collectors.groupingBy(
                        Flight::getAirline,
                        Collectors.summingInt(Flight::getDelayMinutes)
                ));
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