# Airline Operations Monitoring Dashboard

**Course:** CMSC 495 – Computer Science Capstone  
**University:** University of Maryland Global Campus (UMGC)  
**Section:** 7382  

---

## Project Overview

The Airline Operations Monitoring Dashboard is a Java-based desktop application designed to monitor airline flight operations. The system allows users to view and analyze flight information such as routes, delays, cancellations, baggage allowances, seat types, meal availability, and operational statistics.

 Tech Stack:
- Language: Java 17 LTS
- GUI Framework: JavaFX 17 (Hardware Accelerated)
- Build Tool: Maven (M2E Integration)
- Database: SQLite 3 with JDBC Driver
- Testing: JUnit 5

---

## Team Members

- Ruth Mathewos  
- Will Vernigor  
- Afsana Abdul  
- Robby Allen  
- Casey Leonard  

---

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

---

## Current Database Schema

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
    technical_stop_note TEXT,
    connection_time INTEGER,
    baggage_allowance TEXT,
    seat_type TEXT,
    meal_included TEXT
);
```
---

**Usage Examples**
---
To help users navigate the new dynamic features, follow these operational examples:
- **Multi-Criteria Filtering:** Enter "DL" in the search bar, select "Delayed" from the Status dropdown, and select "Non-stop" from the Route Type.
  The table and charts will update in real-time (<100ms) to show specific Delta Airlines operational bottlenecks.
- **Dynamic Visuals:** Click any row in the flight table. The dashboard will automatically load a city-specific image for the destination airport,
  providing immediate visual confirmation of the route.
- **Statistical Analysis:** View the Pie Chart for a breakdown of "On Time" vs "Delayed" percentages, or click "Top Delays" to see a specialized popup
  of the five flights requiring immediate dispatcher attention.
- **CSV Export:** Functionality for external data usage

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

**src/**

└─ airline/

├─ Main.java
    
├─ dao/ # Data access layer

├─ db/ # Database connection management

├─ model/ # Data model classes (Flight, etc.)

├─ service/ # Business logic (filtering, statistics)

└─ ui/ # Dashboard user interface components

├─ test/java/   # JUnit 5 Testing Suites

**data/**

└─ airline_dashboard.db # SQLite database file

**docs/**

└─ project documentation # Diagrams, planning documents, etc.

pom.xml # Maven configuration and dependencies

---

## Running the Application

To run the Airline Operations Monitoring Dashboard locally:

1. Ensure **Java 17** and **Maven** are installed on your system.

2. Clone the repository:

git clone https://github.com/Guy008008/Airline-Operations-Monitoring-Dashboard-

3. Navigate to the project directory.

4. Ensure the SQLite database file exists in:

data/airline_dashboard.db

5. Open the project in your preferred Java IDE (IntelliJ, Eclipse, NetBeans, etc.).

6. **Compile the project using Maven:** Run mvn clean install to download dependencies and build the JAR.

7. **Run the application:** From the command line, run mvn javafx:run or execute Main.java from your IDE.


---

**Setup & Verification Scripts**

To ensure your environment is correctly configured for Phase II, run the following Maven-based verification commands:

- **Verify Java Environment**: java -version

- **Verify dependencies and compile code**: mvn clean compile

- **Execute Automated Test Suite (Validation Script)**: mvn test

**Note:** The mvn test command executes all 5 test suites (Connectivity, Mapping, Filter, Stats, and Robustness) to ensure the system is stable before deployment.

## Development Workflow

The project uses **GitHub for version control and collaboration**.

General workflow for team members:

1. Pull the latest changes from the repository.
2. Make code updates or add new features.
3. Commit changes with a clear commit message describing the update.
4. Push updates to the repository.
5. Use **GitHub Issues** to report bugs, track tasks, or discuss implementation details.

Team members should coordinate changes that affect shared components such as:

- Database schema  
- Data models  
- Service layer interfaces  
- UI integration points

**Troubleshooting**
- **macOS Compatibility:** If the application crashes in an IDE, run it directly from the terminal using Maven.
- **Graphics Issues:** Using a software rendering flag can resolve specific JavaFX display errors.
