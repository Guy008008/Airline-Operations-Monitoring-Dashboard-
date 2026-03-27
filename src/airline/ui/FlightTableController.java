/*
 * ================================================================
 * University: University of Maryland Global Campus (UMGC)
 * Course: CMSC 495 – Computer Science Capstone
 * Project: Airline Operations Monitoring Dashboard
 *
 * File: FlightTableController.java
 *
 * Team Members:
 *   Ruth Mathewos
 *   Will Vernigor
 *   Afsana Abdul
 *   Robby Allen
 *   Casey Leonard
 *
 * Author: Ruth Mathewos
 *
 * Created: 2026-03-17
 * Last Revised: 2026-03-27
 *
 * Description: The FlightTableController class handles data loading, filtering,
 *              and updates for the dashboard. It manages the flight table, statistics, 
 *              chart, and user interactions such as search, filters, and row selection.
 *
 * ================================================================
 */

package airline.ui;

import airline.dao.FlightDAO;
import airline.db.DatabaseConnection;
import airline.model.Flight;

import javafx.collections.*;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.geometry.Insets;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;
import javafx.scene.control.TextArea;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import java.util.Map;
import javafx.scene.shape.Line;
import javafx.scene.paint.Color;


public class FlightTableController {

    private TableView<Flight> table;
    private TextField searchField;
    private ComboBox<String> statusFilter;
    private ComboBox<String> airportFilter;
    private ComboBox<String> routeFilter;
    private TextArea statsLabel;
    private Label lastUpdatedLabel; 
    private PieChart chart;
    private BarChart<String, Number> barChart;
    private ImageView cityImage; 
    private Label destinationLabel; 

    private ObservableList<Flight> masterData = FXCollections.observableArrayList();

    public FlightTableController(TableView<Flight> table,
                                 TextField searchField,
                                 ComboBox<String> statusFilter,
                                 ComboBox<String> airportFilter,
                                 ComboBox<String> routeFilter,
                                 TextArea statsLabel,
                                 Label lastUpdatedLabel,
                                 PieChart chart,
                                 BarChart<String, Number> barChart,
                                 ImageView cityImage,
                                 Label destinationLabel) {

        this.table = table;
        this.searchField = searchField;
        this.statusFilter = statusFilter;
        this.airportFilter = airportFilter;
        this.routeFilter = routeFilter;
        this.statsLabel = statsLabel;
        this.lastUpdatedLabel = lastUpdatedLabel;
        this.chart = chart;
        this.barChart = barChart;
        this.cityImage = cityImage;
        this.destinationLabel = destinationLabel;
        
        statsLabel.setMaxWidth(Double.MAX_VALUE);
        statsLabel.setWrapText(true);
        statsLabel.setPrefHeight(220);
        
        setupColumns();
        loadData();
        setupListeners();
        setupRowClick();
        updateAll(masterData);
    }

    private void setupColumns() {
        table.getColumns().clear();

        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.setPlaceholder(new Label("No flights found")); 

        table.getColumns().add(createCol("Flight", "flightNumber"));
        table.getColumns().add(createCol("Airline", "airline"));
        table.getColumns().add(createCol("Origin", "originAirport"));
        table.getColumns().add(createCol("Destination", "destinationAirport"));
        table.getColumns().add(createCol("Baggage", "baggageAllowance"));
        
        TableColumn<Flight, String> seatCol = new TableColumn<>("Seat");
        seatCol.setCellValueFactory(new PropertyValueFactory<>("seatType"));

        seatCol.setCellFactory(column -> new TableCell<Flight, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(item);

                    switch (item.toLowerCase()) {
                        case "business":
                            setStyle("-fx-text-fill: green; -fx-font-weight: bold;");
                            break;
                        case "economy":
                            setStyle("-fx-text-fill: blue;");
                            break;
                        case "first":
                            setStyle("-fx-text-fill: purple; -fx-font-weight: bold;");
                            break;
                        default:
                            setStyle("");
                    }
                }
            }
        });

        table.getColumns().add(seatCol);
        
        table.getColumns().add(createCol("Meal", "mealIncluded"));

       

        TableColumn<Flight, String> departureCol = createCol("🛫 Departure", "departureTime");
        TableColumn<Flight, String> arrivalCol = createCol("🛬 Arrival", "arrivalTime");
        

        table.getColumns().add(departureCol);
        table.getColumns().add(arrivalCol);

        
        TableColumn<Flight, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));

        statusCol.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(String status, boolean empty) {
                super.updateItem(status, empty);

                if (empty || status == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(status);

                    switch (status.toLowerCase()) {
                        case "on time" -> setStyle("-fx-text-fill: #2ecc71; -fx-font-weight: bold;");
                        case "delayed" -> setStyle("-fx-text-fill: #f39c12; -fx-font-weight: bold;");
                        case "cancelled" -> setStyle("-fx-text-fill: #e74c3c; -fx-font-weight: bold;");
                    }
                }
            }
        });

        table.getColumns().add(statusCol);

        table.getColumns().add(createCol("Delay", "delayMinutes"));
        table.getColumns().add(createCol("Type", "flightType"));
        table.getColumns().add(createCol("Tech Stop", "technicalStopFlag"));

        
        table.setRowFactory(tv -> {
            TableRow<Flight> row = new TableRow<>();

            row.itemProperty().addListener((obs, oldItem, newItem) -> {
                if (newItem != null && "Delayed".equalsIgnoreCase(newItem.getStatus())) {
                    row.setStyle("-fx-background-color: #fff3cd;");
                } else {
                    row.setStyle("");
                }
            });

            row.setOnMouseEntered(e -> {
                if (!row.isEmpty()) {
                    row.setStyle("-fx-background-color: #eef6ff;");
                }
            });

            row.setOnMouseExited(e -> {
                if (row.getItem() != null &&
                        "Delayed".equalsIgnoreCase(row.getItem().getStatus())) {
                    row.setStyle("-fx-background-color: #fff3cd;");
                } else {
                    row.setStyle("");
                }
            });

            return row;
        });
    }

    private TableColumn<Flight, String> createCol(String title, String field) {
        TableColumn<Flight, String> col = new TableColumn<>(title);
        col.setCellValueFactory(new PropertyValueFactory<>(field));
        return col;
    }

    public void refreshData() { 
        loadData();
        updateAll(masterData);
    }

    private void loadData() {
        try {
            Connection connection = DatabaseConnection.getConnection();
            FlightDAO dao = new FlightDAO(connection);

            List<Flight> flights = dao.getAllFlights();
            masterData = FXCollections.observableArrayList(flights);

            table.setItems(masterData);
            
            ObservableList<String> airports = FXCollections.observableArrayList();
            airports.add("All");

            flights.stream()
                .flatMap(f -> java.util.stream.Stream.of(
                    f.getOriginAirport(),
                    f.getDestinationAirport()
                ))
                .filter(a -> a != null && !a.isEmpty())
                .distinct()
                .sorted()
                .forEach(airports::add);

            airportFilter.setItems(airports);
            airportFilter.setValue("All");

        } catch (SQLException e) {
            e.printStackTrace();

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Database Error");
            alert.setHeaderText("Failed to load flight data");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    private void setupListeners() {
        searchField.textProperty().addListener((obs, o, n) -> filterData());
        statusFilter.valueProperty().addListener((obs, o, n) -> filterData());
        airportFilter.valueProperty().addListener((obs, o, n) -> filterData());
        routeFilter.valueProperty().addListener((obs, o, n) -> filterData());

    }

    private void filterData() {

      String search = searchField.getText() == null ? "" : searchField.getText().toLowerCase();
      String status = statusFilter.getValue();
      String airport = airportFilter.getValue();
      String route = routeFilter.getValue();   // ✅ NEW

      List<Flight> filtered = masterData.stream()
              .filter(f ->
                      f.getFlightNumber().toLowerCase().contains(search) ||
                      f.getAirline().toLowerCase().contains(search) ||
                      f.getOriginAirport().toLowerCase().contains(search) ||
                      f.getDestinationAirport().toLowerCase().contains(search)
              )
              .filter(f -> status.equals("All") || f.getStatus().equalsIgnoreCase(status))
              .filter(f -> airport.equals("All") ||
                      f.getOriginAirport().equalsIgnoreCase(airport) ||
                      f.getDestinationAirport().equalsIgnoreCase(airport))
              .filter(f -> route.equals("All") || f.getFlightType().equalsIgnoreCase(route)) // ✅ NEW
              .collect(Collectors.toList());

      ObservableList<Flight> list = FXCollections.observableArrayList(filtered);
      table.setItems(list);

      updateAll(list);
    }


    private void updateAll(ObservableList<Flight> data) {
        if (data == null) return;

        updateStats(data);
        updateChart(data);
        updateBarChart(data);
        lastUpdatedLabel.setText(
            "Updated at " + java.time.LocalTime.now().withNano(0)
        );    
    }

    private void updateStats(ObservableList<Flight> data) {

      int total = data.size();

      long onTime = data.stream()
              .filter(f -> f.getStatus().equalsIgnoreCase("On Time"))
              .count();

      long delayed = data.stream()
              .filter(f -> f.getStatus().equalsIgnoreCase("Delayed"))
              .count();

      long cancelled = data.stream()
              .filter(f -> f.getStatus().equalsIgnoreCase("Cancelled"))
              .count();

      long nonStop = data.stream()
              .filter(f -> f.getFlightType().equalsIgnoreCase("Non-stop"))
              .count();

      long layover = data.stream()
              .filter(f -> f.getFlightType().equalsIgnoreCase("Layover"))
              .count();
      
      double avgDelay = data.stream()
          .mapToInt(Flight::getDelayMinutes)
          .average()
          .orElse(0);
          
      double onTimePercent = (onTime * 100.0) / total;

          
      statsLabel.setText(
          "📊 FLIGHT SUMMARY\n\n" +
          "Flights: " + total + "\n" +
          "On-Time: " + (int)onTimePercent + "%\n" +
          "Avg Delay: " + (int)avgDelay + " min\n" +
          "On-Time: " + onTime + "\n" +
          "Delayed: " + delayed + "\n" +
          "Cancelled: " + cancelled + "\n" +
          "Non-stop: " + nonStop + "\n" +
          "Layover: " + layover + "\n\n"    
      );
    }

    private void updateChart(ObservableList<Flight> data) {
        long onTime = data.stream().filter(f -> f.getStatus().equalsIgnoreCase("On Time")).count();
        long delayed = data.stream().filter(f -> f.getStatus().equalsIgnoreCase("Delayed")).count();
        long cancelled = data.stream().filter(f -> f.getStatus().equalsIgnoreCase("Cancelled")).count();

        chart.getData().clear();

        long total = onTime + delayed + cancelled;
        PieChart.Data onTimeData = new PieChart.Data(
            "On Time (" + (onTime * 100 / total) + "%)", onTime);
        PieChart.Data delayedData = new PieChart.Data(
            "Delayed (" + (delayed * 100 / total) + "%)", delayed);
        PieChart.Data cancelledData = new PieChart.Data(
            "Cancelled (" + (cancelled * 100 / total) + "%)", cancelled);

        
        chart.getData().addAll(onTimeData, delayedData, cancelledData);

        chart.getData().forEach(d -> {
            switch (d.getName()) {
                case "On Time" -> d.getNode().setStyle("-fx-pie-color: #2ecc71;");
                case "Delayed" -> d.getNode().setStyle("-fx-pie-color: #f39c12;");
                case "Cancelled" -> d.getNode().setStyle("-fx-pie-color: #e74c3c;");
            }
        });
    }
    
    private void updateBarChart(ObservableList<Flight> data) {

      barChart.getData().clear();

      Map<String, Integer> delays = data.stream()
          .collect(Collectors.groupingBy(
              Flight::getAirline,
              Collectors.summingInt(Flight::getDelayMinutes)
          ));

      XYChart.Series<String, Number> series = new XYChart.Series<>();

      delays.entrySet().stream()
      .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
      .limit(6)
      .forEach(entry ->
          series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()))
      );


      barChart.getData().add(series);
    }

    
    private void updateCityImage(String airport) {

      String imagePath = switch (airport) {

          case "LHR" -> "file:images/london.jpg";
          case "JFK" -> "file:images/newyork.jpg";
          case "DXB" -> "file:images/dubai.jpg";
          case "ADD" -> "file:images/addis.jpg";
          case "NBO" -> "file:images/nairobi.jpg";
          case "LAX" -> "file:images/losangeles.jpg";
          case "ATL" -> "file:images/atlanta.jpg";
          case "ORD" -> "file:images/chicago.jpg";
          case "SFO" -> "file:images/sanfrancisco.jpg";
          case "SEA" -> "file:images/seattle.jpg";
          case "DOH" -> "file:images/doha.jpg";
          case "FRA" -> "file:images/frankfurt.jpg";
          case "CDG" -> "file:images/paris.jpg";
          case "JNB" -> "file:images/johannesburg.jpg";
          case "DUB" -> "file:images/dublin.jpg";

          default -> "file:images/default.jpg";
      };

      try {
          cityImage.setImage(new Image(imagePath));
      } catch (Exception e) {
          cityImage.setImage(new Image("file:images/default.jpg"));
      }
      
    }
    
    private String getCityName(String airport) {
      return switch (airport) {
          case "LHR" -> "London";
          case "JFK" -> "New York";
          case "DXB" -> "Dubai";
          case "ADD" -> "Addis Ababa";
          case "NBO" -> "Nairobi";
          case "LAX" -> "Los Angeles";
          case "ATL" -> "Atlanta";
          case "ORD" -> "Chicago";
          case "SFO" -> "San Francisco";
          case "SEA" -> "Seattle";
          case "DOH" -> "Doha";
          case "FRA" -> "Frankfurt";
          case "CDG" -> "Paris";
          case "JNB" -> "Johannesburg";
          case "DUB" -> "Dublin";
          default -> "Unknown";
      };
    }


    private void setupRowClick() {
      table.setRowFactory(tv -> {
          TableRow<Flight> row = new TableRow<>();
          Tooltip tooltip = new Tooltip();

          row.itemProperty().addListener((obs, oldItem, newItem) -> {
              if (newItem == null) {
                  row.setTooltip(null);
                  row.setStyle("");
              } else {
                  tooltip.setText(
                      "Flight: " + newItem.getFlightNumber() + "\n" +
                      "Airline: " + newItem.getAirline() + "\n" +
                      "Route: " + newItem.getOriginAirport() + " → " + newItem.getDestinationAirport() + "\n" +
                      "Baggage: " + newItem.getBaggageAllowance() + "\n" +
                      "Seat: " + newItem.getSeatType() + "\n" +
                      "Meal: " + (newItem.getMealIncluded().equalsIgnoreCase("Yes") ? "Meal Included" : "No Meal")
                  );
                  row.setTooltip(tooltip);

                  if ("Delayed".equalsIgnoreCase(newItem.getStatus())) {
                      row.setStyle("-fx-background-color: #fff3cd;");
                  } else {
                      row.setStyle("");
                  }
              }
          });

          row.setOnMouseEntered(e -> {
              if (!row.isEmpty()) {
                  row.setStyle("-fx-background-color: #eef6ff;");
              }
          });

          row.setOnMouseExited(e -> {
              if (row.getItem() != null &&
                  "Delayed".equalsIgnoreCase(row.getItem().getStatus())) {
                  row.setStyle("-fx-background-color: #fff3cd;");
              } else {
                  row.setStyle("");
              }
          });

          row.setOnMouseClicked(event -> {
              if (!row.isEmpty() && event.getClickCount() == 2) {
                  Flight f = row.getItem();

                  updateCityImage(f.getDestinationAirport());
                  destinationLabel.setText(
                      "Destination: " + f.getDestinationAirport() +
                      " (" + getCityName(f.getDestinationAirport()) + ")"
                  );

                  Alert alert = new Alert(Alert.AlertType.INFORMATION);
                  alert.setTitle("✈ Flight Details");
                  alert.setHeaderText("Flight " + f.getFlightNumber());
                  alert.getDialogPane().setPrefWidth(400);
                  
                  Label details = new Label(
                      "Airline: " + f.getAirline() + "\n" +
                      "Route: " + f.getOriginAirport() + " → " + f.getDestinationAirport() + "\n" +
                      "Departure: " + f.getDepartureTime() + "\n" +
                      "Arrival: " + f.getArrivalTime() + "\n" +
                      "Status: " + f.getStatus() + "\n" +
                      "Delay: " + f.getDelayMinutes() + " mins\n" +
                      "Type: " + f.getFlightType() + "\n" +
                      "Technical Stop: " + f.getTechnicalStopFlag() + "\n" +
                      "Baggage: " + f.getBaggageAllowance() + "\n" +
                      "Seat: " + f.getSeatType() + "\n" +
                      "Meal: " + (f.getMealIncluded().equalsIgnoreCase("Yes") ? "Meal Included" : "No Meal")
                  );

                  VBox content = new VBox(10, details);
                  content.setPadding(new Insets(10));

                  alert.getDialogPane().setContent(content);
                  alert.showAndWait();
              }
          });

          return row;
      });
  }

}
