# Airline Operations Monitoring Dashboard 

**Course:** CMSC 495 – Computer Science Capstone  
**University:** University of Maryland Global Campus (UMGC)  
**Section:** 7382  

## Project Overview

The Airline Operations Monitoring Dashboard is a Java-based application designed to monitor airline flight operations. The system allows users to view and analyze flight information such as routes, delays, cancellations, and operational statistics. The application uses Java for the application logic and SQLite for persistent data storage.

## Team Members

- Ruth Mathewos  
- Will Vernigor  
- Afsana Abdul  
- Robby Allen  
- Casey Leonard  

## Class Responsibilities

| Class | Responsibility | Owner |
|------|------|------|
| Main.java | Application entry point | Will & Casey |
| DashboardUI.java | Dashboard interface | Ruth |
| DatabaseConnection.java | SQLite connection management | Will & Casey |
| FlightDAO.java | Data access layer for flights | Will & Casey |
| Flight.java | Flight data model | Afsana |
| StatsService.java | Flight statistics calculations | Will & Casey |
| FilterService.java | Flight filtering logic | Will & Casey |
| FlightTableController.java | UI table controller | Ruth |

## Baseline Database Schema

```sql
CREATE TABLE flights (
    flight_id INTEGER PRIMARY KEY,
    flight_number TEXT,
    airline TEXT,
    origin_airport TEXT,
    destination_airport TEXT,
    connection_airport TEXT,
    departure_time TEXT,
    arrival_time TEXT,
    delay_minutes INTEGER,
    status TEXT,
    flight_type TEXT,
    technical_stop_flag TEXT,
    technical_stp_note TEXT,
    connection_time INTEGER
);
```

## Reporting Issues and Tracking Tasks

Team members should use **GitHub Issues** to report bugs, suggest improvements, or track development tasks during the project.

### How to Create an Issue

1. Go to the project repository:

https://github.com/Guy008008/Airline-Operations-Monitoring-Dashboard-

2. Click the **Issues** tab at the top of the repository page.

3. Click the green **New Issue** button.

4. Enter a clear and descriptive **title** for the issue.

Example:

Verify Flight.java model matches database schema

5. Write a short **description** explaining the issue, bug, or task.

Example:

The current backend DAO expects the following fields from the Flight model:

- flight_id  
- flight_number  
- airline  
- origin_airport  
- destination_airport  
- connection_airport  
- departure_time  
- arrival_time  
- delay_minutes  
- status  
- flight_type  
- technical_stop  
- connection_time  

We should confirm that `Flight.java` matches this schema so DAO mapping works correctly.

6. Assign the issue to the appropriate team member if needed.

7. Click **Submit new issue**.

---

## Issue Usage Guidelines

- Use Issues to report bugs, missing functionality, or implementation questions.
- Provide enough detail so other team members understand the problem.
- Reference relevant files or code sections when possible.
- Close issues once the problem has been resolved.

---

## Project Structure

The repository is organized into the following directories:

src/
- airline/
  - Main.java – Application entry point
  - dao/ – Data access layer responsible for retrieving flight data from the database
  - db/ – Database connection management
  - model/ – Data model classes representing flight information
  - service/ – Business logic such as filtering and statistics calculations
  - ui/ – Dashboard user interface components

data/
- Contains the SQLite database file used by the application

docs/
- Project documentation, diagrams, and supporting materials
2. How to Run the Application

This makes it easier for teammates to run the project.

## Running the Application

To run the Airline Operations Monitoring Dashboard locally:

1. Ensure Java is installed on your system.
2. Clone the repository:

git clone https://github.com/Guy008008/Airline-Operations-Monitoring-Dashboard-

3. Navigate to the project directory.

4. Ensure the SQLite database file exists in:

data/airline.db

5. Compile the project using your preferred Java IDE or command line tools.

6. Run the application starting from:

Main.java
3. Development Workflow

This explains how the team should work with GitHub.

## Development Workflow

The project uses GitHub for version control and collaboration.

General workflow:

1. Pull the latest changes from the repository.
2. Make code updates or add new features.
3. Commit changes with a clear commit message describing the update.
4. Push updates to the repository.
5. Use GitHub Issues to report bugs, track tasks, or discuss implementation details.

Team members should coordinate changes that affect shared components such as:

- Database schema
- Data models
- Service layer interfaces
- UI integration points
