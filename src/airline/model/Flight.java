/*
 * ================================================================
 * University: University of Maryland Global Campus (UMGC)
 * Course: CMSC 495 – Computer Science Capstone
 * Project: Airline Operations Monitoring Dashboard
 *
 * File: Flight.java
 *
 * Team Members:
 *   Ruth Mathewos
 *   Will Vernigor
 *   Afsana Abdul
 *   Robby Allen
 *   Casey Leonard
 *
 * Author: Asfana Abdul
 *
 * Created: 2026-03-16
 * Last Revised: 2026-03-27
 *
 * Description: Represents a flight and its associated details in the airline system.
 *              Includes flight information, route, schedule, status, and passenger services
 *              such as baggage allowance, seat type, and meal availability.

 *             
 * ================================================================
 */

package airline.model;

public class Flight {

    private int flightId;
    private String flightNumber;
    private String airline;
    private String originAirport;
    private String destinationAirport;
    private String connectionAirport;
    private String departureTime;
    private String arrivalTime;
    private int delayMinutes;
    private String status;
    private String flightType;
    private String technicalStopFlag;
    private String technicalStopNote;
    private Integer connectionTime;
    private String baggageAllowance;
    private String seatType;
    private String mealIncluded;

    public Flight(int flightId, String flightNumber, String airline,
                  String originAirport, String destinationAirport,
                  String connectionAirport, String departureTime,
                  String arrivalTime, int delayMinutes, String status,
                  String flightType, String technicalStopFlag,
                  String technicalStopNote, Integer connectionTime,
                  String baggageAllowance, String seatType, String mealIncluded) {

        this.flightId = flightId;
        this.flightNumber = flightNumber;
        this.airline = airline;
        this.originAirport = originAirport;
        this.destinationAirport = destinationAirport;
        this.connectionAirport = connectionAirport;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.delayMinutes = delayMinutes;
        this.status = status;
        this.flightType = flightType;
        this.technicalStopFlag = technicalStopFlag;
        this.technicalStopNote = technicalStopNote;
        this.connectionTime = connectionTime;
        this.baggageAllowance = baggageAllowance;
        this.seatType = seatType;
        this.mealIncluded = mealIncluded;
    }

    public int getFlightId() { return flightId; }
    public String getFlightNumber() { return flightNumber; }
    public String getAirline() { return airline; }
    public String getOriginAirport() { return originAirport; }
    public String getDestinationAirport() { return destinationAirport; }
    public String getConnectionAirport() { return connectionAirport; }
    public String getDepartureTime() { return departureTime; }
    public String getArrivalTime() { return arrivalTime; }
    public int getDelayMinutes() { return delayMinutes; }
    public String getStatus() { return status; }
    public String getFlightType() { return flightType; }
    public String getTechnicalStopFlag() { return technicalStopFlag; }
    public String getTechnicalStopNote() { return technicalStopNote; }
    public Integer getConnectionTime() { return connectionTime; }
    public String getBaggageAllowance() { return baggageAllowance; }
    public String getSeatType() { return seatType; }
    public String getMealIncluded() {return mealIncluded; }
}
