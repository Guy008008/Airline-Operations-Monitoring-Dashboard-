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
 * Last Revised: 2026-03-27
 *
 * Description:
 * Entry point for the Airline Operations Monitoring Dashboard
 * application. This class initializes the database connection,
 * creates backend service objects, and launches the dashboard UI.
 *
 * ================================================================
 */

package airline;

import javafx.application.Application;
import airline.ui.DashboardUI;
public class Main {
   public static void main(String[] args) {
       Application.launch(DashboardUI.class, args);
   }
}
