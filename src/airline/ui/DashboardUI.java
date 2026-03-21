/*
 * ================================================================
 * University: University of Maryland Global Campus (UMGC)
 * Course: CMSC 495 – Computer Science Capstone
 * Project: Airline Operations Monitoring Dashboard
 *
 * File: DashboardUI.java
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
 * Last Revised: 2026-03-21
 *
 * Description:The DashboardUI class builds and displays the main interface of 
 *             the Airline Operations Dashboard. It creates the layout, including
 *             the table, filters, charts, and image panel, and connects them to 
 *             the controller for functionality.
 *             
 * ================================================================
 */

package airline.ui;

import airline.model.Flight;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.scene.image.ImageView;



public class DashboardUI extends Application {

    @Override
    public void start(Stage stage) {

        // ===== HEADER (GRADIENT) =====
        Label headerTitle = new Label("✈ Airline Operations Dashboard");
        headerTitle.setStyle("-fx-text-fill: white; -fx-font-size: 20px; -fx-font-weight: bold;");

        HBox header = new HBox(headerTitle);
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(15));
        header.setStyle("-fx-background-color: linear-gradient(to right, #1e2a38, #2c3e50);");

        // ===== SEARCH + FILTERS =====
        TextField searchField = new TextField();
        searchField.setPromptText("Search Flight...");
        searchField.setPrefWidth(220);
        searchField.setStyle("-fx-padding: 8;");

        ComboBox<String> statusFilter = new ComboBox<>();
        statusFilter.getItems().addAll("All", "On Time", "Delayed", "Cancelled");
        statusFilter.setValue("All");

        ComboBox<String> airportFilter = new ComboBox<>();
        airportFilter.setValue("All");

        VBox searchBox = new VBox(5, new Label("Search"), searchField);
        VBox statusBox = new VBox(5, new Label("Status"), statusFilter);
        VBox airportBox = new VBox(5, new Label("Airport"), airportFilter);

        ComboBox<String> routeFilter = new ComboBox<>();
        routeFilter.getItems().addAll("All", "Non-stop", "Layover");
        routeFilter.setValue("All");
        VBox routeBox = new VBox(5, new Label("Route"), routeFilter);

        
        Button refreshBtn = new Button("Refresh");
        refreshBtn.setStyle("-fx-background-color: #2c7be5; -fx-text-fill: white;");
        
        HBox filters = new HBox(20, searchBox, statusBox, airportBox, routeBox, refreshBtn);
        filters.setPadding(new Insets(10));

        // ===== TABLE =====
        Label tableTitle = new Label("Flights");
        tableTitle.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        TableView<Flight> table = new TableView<>();
        table.setStyle("""
            -fx-background-color: white;
            -fx-border-color: #dcdcdc;
        """);

        VBox tableCard = new VBox(10, tableTitle, table);
        tableCard.setPadding(new Insets(15));
        tableCard.setStyle("""
            -fx-background-color: white;
            -fx-border-color: #e0e0e0;
            -fx-background-radius: 10;
            -fx-border-radius: 10;
        """);

        // ===== STATS =====
        Label stats = new Label();
        stats.setWrapText(true);
        stats.setMaxWidth(250);
        Label lastUpdated = new Label();
        lastUpdated.setText("Last updated: --");
        lastUpdated.setStyle("-fx-font-size: 11px; -fx-text-fill: #777;");
        stats.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");
        
        VBox statsBox = new VBox(5, stats, lastUpdated); 

        Label statsTitle = new Label("Statistics");
        statsTitle.setStyle("-fx-font-weight: bold;");

        VBox statsCard = new VBox(10, statsTitle, statsBox);
        statsCard.setPadding(new Insets(15));
        statsCard.setStyle("""
            -fx-background-color: white;
            -fx-border-color: #e0e0e0;
            -fx-background-radius: 10;
            -fx-border-radius: 10;
        """);
        statsCard.setPrefWidth(350);

        // ===== CHART =====
        PieChart pieChart = new PieChart();
        pieChart.setPrefSize(400, 300);

        Label chartTitle = new Label("Flight Status Distribution");
        chartTitle.setStyle("-fx-font-weight: bold;");

        VBox chartCard = new VBox(10, chartTitle, pieChart);
        chartCard.setPadding(new Insets(15));
        chartCard.setStyle("""
            -fx-background-color: white;
            -fx-border-color: #e0e0e0;
            -fx-background-radius: 10;
            -fx-border-radius: 10;
        """);
        
        // ==== Image view ====
        ImageView cityImage = new ImageView();
        cityImage.setFitWidth(250);
        cityImage.setFitHeight(150);
        cityImage.setPreserveRatio(true);
        
        Label destinationLabel = new Label("Destination: -");
        destinationLabel.setStyle("-fx-font-size: 13px; -fx-text-fill: #555;");

        Label imageTitle = new Label("Destination");
        imageTitle.setStyle("-fx-font-weight: bold;");

        VBox imageCard = new VBox(10, imageTitle, cityImage, destinationLabel);
        imageCard.setPadding(new Insets(15));
        imageCard.setStyle("""
            -fx-background-color: white;
            -fx-border-color: #e0e0e0;
            -fx-background-radius: 10;
            -fx-border-radius: 10;
        """);

        // ===== BOTTOM SECTION =====
        HBox bottomSection = new HBox(20, statsCard, chartCard, imageCard);

        // ===== CONTROLLER =====
        FlightTableController controller = new FlightTableController(
            table, 
            searchField, 
            statusFilter,
            airportFilter, 
            routeFilter, 
            stats, 
            lastUpdated,
            pieChart, 
            cityImage,
            destinationLabel
        );
        refreshBtn.setOnAction(e -> controller.refreshData());

        // ===== MAIN CONTENT =====
        VBox content = new VBox(20, filters, tableCard, bottomSection);
        content.setPadding(new Insets(20));

        // ===== ROOT =====
        VBox root = new VBox(header, content);
        root.setStyle("-fx-background-color: #f4f6f8;");

        // ===== SCENE =====
        Scene scene = new Scene(root, 1000, 720);
        stage.setScene(scene);
        stage.setTitle("Airline Dashboard");
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}

