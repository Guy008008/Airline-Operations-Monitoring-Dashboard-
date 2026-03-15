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
    technical_stop_note TEXT,
    connection_time INTEGER
);
```
