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
 *
 * Created: 2026-03-16
 * Last Revised: 2026-03-16
 *
 * Description:
 * Provides statistical calculations for the dashboard including
 * total flights, delayed flights, cancelled flights, and average delay.
 *
 * ================================================================
 */

package airline.service;

import airline.model.Flight;

import java.util.List;

public class StatsService {

    /**
     * Returns the total number of flights.
     */
    public int getTotalFlights(List<Flight> flights) {
        return flights.size();
    }

    /**
     * Returns the number of delayed flights.
     */
    public long getDelayedFlights(List<Flight> flights) {
        return flights.stream()
                .filter(f -> f.getDelayMinutes() > 0)
                .count();
    }

    /**
     * Returns the number of cancelled flights.
     */
    public long getCancelledFlights(List<Flight> flights) {
        return flights.stream()
                .filter(f -> "Cancelled".equalsIgnoreCase(f.getStatus()))
                .count();
    }

    /**
     * Calculates the average delay time across flights.
     */
    public double getAverageDelay(List<Flight> flights) {

        return flights.stream()
                .mapToInt(Flight::getDelayMinutes)
                .average()
                .orElse(0);

    }

}