/*
 * ================================================================
 * University: University of Maryland Global Campus (UMGC)
 * Course: CMSC 495 – Computer Science Capstone
 * Project: Airline Operations Monitoring Dashboard
 *
 * File: FlightDAO.java
 *
 * Team Members:
 *   Ruth Mathewos
 *   Will Vernigor
 *   Afsana Abdul
 *   Robby Allen
 *   Casey Leonard
 *
 * Author: Casey Leonard
 * Revised: Ruth Mathewos
 *
 * Created: 2026-03-16
 * Last Revised: 2026-03-23
 *
 * Description:
 * Provides data access methods for retrieving flight information
 * from the SQLite database. This class handles SQL queries and
 * converts database rows into Flight objects.
 *
 * ================================================================
 */

package airline.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import airline.model.Flight;

public class FlightDAO {

    private final Connection connection;

    /**
     * Constructs a FlightDAO with an active database connection.
     *
     * @param connection Database connection used for SQL operations
     */
    public FlightDAO(Connection connection) {
        this.connection = connection;
    }

    /**
     * Retrieves all flights from the database.
     *
     * @return List of all flights
     */
    public List<Flight> getAllFlights() {
        List<Flight> flights = new ArrayList<>();
        String sql = "SELECT * FROM flights";

        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                flights.add(mapRowToFlight(resultSet));
            }

        } catch (SQLException e) {
            System.out.println("Error retrieving flights.");
            e.printStackTrace();
        }

        return flights;
    }

    /**
     * Searches for flights by flight number.
     *
     * @param flightNumber Flight number or partial flight number
     * @return List of matching flights
     */
    public List<Flight> searchByFlightNumber(String flightNumber) {
        List<Flight> flights = new ArrayList<>();
        String sql = "SELECT * FROM flights WHERE flight_number LIKE ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, "%" + flightNumber + "%");

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    flights.add(mapRowToFlight(resultSet));
                }
            }

        } catch (SQLException e) {
            System.out.println("Error searching flights by flight number.");
            e.printStackTrace();
        }

        return flights;
    }

    /**
     * Converts a database row into a Flight object.
     *
     * @param resultSet Current row from SQL query results
     * @return Flight object built from row data
     * @throws SQLException if a column cannot be read
     */
    private Flight mapRowToFlight(ResultSet resultSet) throws SQLException {
        return new Flight(
                resultSet.getInt("flight_id"),
                resultSet.getString("flight_number"),
                resultSet.getString("airline"),
                resultSet.getString("origin_airport"),
                resultSet.getString("destination_airport"),
                resultSet.getString("connection_airport"),
                resultSet.getString("departure_time"),
                resultSet.getString("arrival_time"),
                resultSet.getInt("delay_minutes"),
                resultSet.getString("status"),
                resultSet.getString("flight_type"),
                resultSet.getString("technical_stop_flag"),
                resultSet.getString("technical_stop_note"),
                resultSet.getObject("connection_time") != null
                    ? resultSet.getInt("connection_time")
                    : null,
                resultSet.getString("baggage_allowance"),
                resultSet.getString("seat_type"),
                resultSet.getString("meal_included")
        );
    }
}