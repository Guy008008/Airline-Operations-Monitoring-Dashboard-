/*
 * ================================================================
 * University: University of Maryland Global Campus (UMGC)
 * Course: CMSC 495 – Computer Science Capstone
 * Project: Airline Operations Monitoring Dashboard
 *
 * File: FilterService.java
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
 * Provides filtering functionality for flight data used by the
 * Airline Operations Monitoring Dashboard. This service allows
 * filtering of flights based on origin airport.
 *
 * ================================================================
 */

package airline.service;

import airline.model.Flight;
import java.util.List;
import java.util.stream.Collectors;

public class FilterService {

    /**
     * Filters flights by origin airport code.
     *
     * @param flights List of flights retrieved from the database
     * @param origin Airport code used to filter flights
     * @return List of flights departing from the specified origin
     */
    public List<Flight> filterByOrigin(List<Flight> flights, String origin) {
        return flights.stream()
                .filter(f -> f.getOriginAirport().equalsIgnoreCase(origin))
                .collect(Collectors.toList());
    }

}