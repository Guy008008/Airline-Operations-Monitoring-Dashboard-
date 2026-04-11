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
 * Revised By: Will Vernigor
 *
 * Created: 2026-03-16
 * Last Revised: 2026-04-11
 *
 * Description:
 * Provides filtering functionality for flight data used by the
 * Airline Operations Monitoring Dashboard. This service allows
 * filtering of flights based on search text, status, airport,
 * and route type.
 *
 * ================================================================
 */

package airline.service;

import java.util.List;
import java.util.stream.Collectors;

import airline.model.Flight;

public class FilterService {

    /**
     * Filters flights using search text, status, airport, and route type.
     *
     * @param flights List of flights
     * @param search Search text for flight number, airline, origin, or destination
     * @param status Selected status filter
     * @param airport Selected airport filter
     * @param route Selected route filter
     * @return Filtered list of flights
     */
    public List<Flight> filterFlights(List<Flight> flights, String search, String status,
                                      String airport, String route) {

        String safeSearch = search == null ? "" : search.toLowerCase();
        String safeStatus = status == null ? "All" : status;
        String safeAirport = airport == null ? "All" : airport;
        String safeRoute = route == null ? "All" : route;

        return flights.stream()
                .filter(f ->
                        f.getFlightNumber().toLowerCase().contains(safeSearch) ||
                        f.getAirline().toLowerCase().contains(safeSearch) ||
                        f.getOriginAirport().toLowerCase().contains(safeSearch) ||
                        f.getDestinationAirport().toLowerCase().contains(safeSearch)
                )
                .filter(f -> safeStatus.equals("All") || f.getStatus().equalsIgnoreCase(safeStatus))
                .filter(f -> safeAirport.equals("All")
                        || f.getOriginAirport().equalsIgnoreCase(safeAirport)
                        || f.getDestinationAirport().equalsIgnoreCase(safeAirport))
                .filter(f -> safeRoute.equals("All") || f.getFlightType().equalsIgnoreCase(safeRoute))
                .collect(Collectors.toList());
    }

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