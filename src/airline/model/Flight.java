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

    public Flight(int flightId, String flightNumber, String airline,
                  String originAirport, String destinationAirport,
                  String connectionAirport, String departureTime,
                  String arrivalTime, int delayMinutes, String status,
                  String flightType, String technicalStopFlag,
                  String technicalStopNote, Integer connectionTime) {

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
}
