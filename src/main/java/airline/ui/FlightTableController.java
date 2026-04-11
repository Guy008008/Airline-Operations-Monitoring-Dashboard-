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
 * Revised By: Will Vernigor
 *
 * Created: 2026-03-17
 * Last Revised: 2026-04-11
 *
 * Description:
 * Handles data loading, delegates business logic to service classes,
 * and manages all UI updates including table, filters, statistics,
 * charts, and user interactions.
 *
 * ================================================================
 */

package airline.ui;

import airline.dao.FlightDAO;
import airline.db.DatabaseConnection;
import airline.model.Flight;
import airline.service.FilterService;
import airline.service.StatsService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class FlightTableController {

    // UI components
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

    // Master data list
    private ObservableList<Flight> masterData = FXCollections.observableArrayList();

    // Services (business logic handled here instead of controller)
    private final FilterService filterService = new FilterService();
    private final StatsService statsService = new StatsService();

    /**
     * Constructor initializes UI components and loads initial data.
     */
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

    /**
     * Configures table columns and styling.
     */
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

        seatCol.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(item);
                    switch (item.toLowerCase()) {
                        case "business" -> setStyle("-fx-text-fill: green; -fx-font-weight: bold;");
                        case "economy" -> setStyle("-fx-text-fill: blue;");
                        case "first" -> setStyle("-fx-text-fill: purple; -fx-font-weight: bold;");
                        default -> setStyle("");
                    }
                }
            }
        });

        table.getColumns().add(seatCol);
        table.getColumns().add(createCol("Meal", "mealIncluded"));
        table.getColumns().add(createCol("🛫 Departure", "departureTime"));
        table.getColumns().add(createCol("🛬 Arrival", "arrivalTime"));

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
                        default -> setStyle("");
                    }
                }
            }
        });

        table.getColumns().add(statusCol);
        table.getColumns().add(createCol("Delay", "delayMinutes"));
        table.getColumns().add(createCol("Type", "flightType"));
        table.getColumns().add(createCol("Tech Stop", "technicalStopFlag"));
    }

    /**
     * Helper method to create table columns.
     */
    private TableColumn<Flight, String> createCol(String title, String field) {
        TableColumn<Flight, String> col = new TableColumn<>(title);
        col.setCellValueFactory(new PropertyValueFactory<>(field));
        return col;
    }

    /**
     * Reloads data from database and reapplies current filters.
     */
    public void refreshData() {
        loadData();
        filterData();
    }

    /**
     * Loads flight data from database and populates filters.
     */
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
                            f.getDestinationAirport()))
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

    /**
     * Sets up listeners for filtering.
     */
    private void setupListeners() {
        searchField.textProperty().addListener((obs, o, n) -> filterData());
        statusFilter.valueProperty().addListener((obs, o, n) -> filterData());
        airportFilter.valueProperty().addListener((obs, o, n) -> filterData());
        routeFilter.valueProperty().addListener((obs, o, n) -> filterData());
    }

    /**
     * Applies filters using FilterService.
     */
    private void filterData() {
        List<Flight> filtered = filterService.filterFlights(
                masterData,
                searchField.getText(),
                statusFilter.getValue(),
                airportFilter.getValue(),
                routeFilter.getValue()
        );

        ObservableList<Flight> list = FXCollections.observableArrayList(filtered);
        table.setItems(list);
        updateAll(list);
    }

    /**
     * Updates all UI components.
     */
    private void updateAll(ObservableList<Flight> data) {
        if (data == null) {
            return;
        }

        statsLabel.setText(statsService.buildStatsSummary(data));
        updateChart(data);
        updateBarChart(data);

        lastUpdatedLabel.setText(
                "Updated at " + java.time.LocalTime.now().withNano(0)
        );
    }

    /**
     * Updates pie chart using StatsService data.
     */
    private void updateChart(ObservableList<Flight> data) {
        Map<String, Long> statusCounts = statsService.getStatusCounts(data);

        long onTime = statusCounts.getOrDefault("On Time", 0L);
        long delayed = statusCounts.getOrDefault("Delayed", 0L);
        long cancelled = statusCounts.getOrDefault("Cancelled", 0L);
        long total = onTime + delayed + cancelled;

        chart.getData().clear();

        if (total == 0) {
            return;
        }

        PieChart.Data onTimeData = new PieChart.Data(
                "On Time (" + Math.round(onTime * 100.0 / total) + "%)", onTime);
        PieChart.Data delayedData = new PieChart.Data(
                "Delayed (" + Math.round(delayed * 100.0 / total) + "%)", delayed);
        PieChart.Data cancelledData = new PieChart.Data(
                "Cancelled (" + Math.round(cancelled * 100.0 / total) + "%)", cancelled);

        chart.getData().addAll(onTimeData, delayedData, cancelledData);

        onTimeData.nodeProperty().addListener((obs, oldNode, newNode) -> {
            if (newNode != null) {
                newNode.setStyle("-fx-pie-color: #2ecc71;");
            }
        });

        delayedData.nodeProperty().addListener((obs, oldNode, newNode) -> {
            if (newNode != null) {
                newNode.setStyle("-fx-pie-color: #f39c12;");
            }
        });

        cancelledData.nodeProperty().addListener((obs, oldNode, newNode) -> {
            if (newNode != null) {
                newNode.setStyle("-fx-pie-color: #e74c3c;");
            }
        });
    }

    /**
     * Updates bar chart showing delays per airline.
     */
    private void updateBarChart(ObservableList<Flight> data) {
        barChart.getData().clear();

        Map<String, Integer> delays = statsService.getDelayMinutesByAirline(data);

        XYChart.Series<String, Number> series = new XYChart.Series<>();

        delays.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .limit(6)
                .forEach(entry ->
                        series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()))
                );

        barChart.getData().add(series);
    }

    /**
     * Handles row interactions (tooltips, double-click details, image updates).
     */
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

    /**
     * Updates destination image based on airport code.
     */
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

    /**
     * Returns a readable city name from an airport code.
     */
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
}