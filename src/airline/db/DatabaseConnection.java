/*
 * ================================================================
 * University: University of Maryland Global Campus (UMGC)
 * Course: CMSC 495 – Computer Science Capstone
 * Project: Airline Operations Monitoring Dashboard
 *
 * File: DatabaseConnection.java
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
 * Handles the connection between the Java application and the
 * SQLite database used for storing flight information.
 *
 * ================================================================
 */

package airline.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    // location of SQLite database file
    private static final String DB_URL = "jdbc:sqlite:data/airline.db";

    /**
     * Creates and returns a database connection.
     *
     * @return Connection object for SQLite database
     * @throws SQLException if connection fails
     */
    public static Connection getConnection() throws SQLException {

        // simple connection using JDBC driver
        return DriverManager.getConnection(DB_URL);

    }

}
