/*
 * ================================================================
 * University: University of Maryland Global Campus (UMGC)
 * Course: CMSC 495 – Computer Science Capstone
 * Project: Airline Operations Monitoring Dashboard
 *
 * File: Main.java
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
 * Entry point for the Airline Operations Monitoring Dashboard
 * application. This class initializes the database connection,
 * creates backend service objects, and launches the dashboard UI.
 *
 * ================================================================
 */

package airline;

import airline.dao.FlightDAO;
import airline.db.DatabaseConnection;
import airline.service.FilterService;
import airline.service.StatsService;
import airline.ui.DashboardUI;

import java.sql.Connection;

public class Main {

    public static void main(String[] args) {

        // just wiring up the app startup here
        try {
            Connection connection = DatabaseConnection.getConnection();

            FlightDAO flightDAO = new FlightDAO(connection);
            FilterService filterService = new FilterService();
            StatsService statsService = new StatsService();

            // initialize dashboard UI
            // NOTE: assumes DashboardUI provides a constructor that accepts
            // FlightDAO, FilterService, and StatsService objects. This allows
            // the UI layer to access flight data, filtering logic, and statistics.
            DashboardUI dashboardUI = new DashboardUI(flightDAO, filterService, statsService);

            // launch the user interface
            dashboardUI.launch();

            System.out.println("Airline Operations Monitoring Dashboard started successfully.");

        } catch (Exception e) {
            System.out.println("Application failed to start.");
            e.printStackTrace();
        }
    }
}