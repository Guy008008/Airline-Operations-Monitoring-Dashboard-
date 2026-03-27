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
 * Last Revised: 2026-03-27
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
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class DashboardUI extends Application {

    @Override
    public void start(Stage stage) {

        // ===== HEADER =====
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

        ComboBox<String> routeFilter = new ComboBox<>();
        routeFilter.getItems().addAll("All", "Non-stop", "Layover");
        routeFilter.setValue("All");

        VBox searchBox = new VBox(5, new Label("Search"), searchField);
        VBox statusBox = new VBox(5, new Label("Status"), statusFilter);
        VBox airportBox = new VBox(5, new Label("Airport"), airportFilter);
        VBox routeBox = new VBox(5, new Label("Route"), routeFilter);

        Button refreshBtn = new Button("Refresh");
        refreshBtn.setStyle("-fx-background-color: #2c7be5; -fx-text-fill: white;");

        HBox filters = new HBox(20, searchBox, statusBox, airportBox, routeBox, refreshBtn);
        filters.setPadding(new Insets(10));

        // ===== TABLE =====
        Label tableTitle = new Label("Flights");
        tableTitle.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        TableView<Flight> table = new TableView<>();
        table.setPrefHeight(260);
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
        TextArea stats = new TextArea();
        stats.setWrapText(true);
        stats.setEditable(false);
        stats.setFocusTraversable(false);
        stats.setPrefWidth(250);
        stats.setPrefHeight(220);
        stats.setPrefRowCount(10);
        stats.setStyle(
            "-fx-font-size: 14px;" +
            "-fx-font-weight: bold;" +
            "-fx-control-inner-background: white;" +
            "-fx-background-color: transparent;" +
            "-fx-border-color: transparent;"
        );

        Label lastUpdated = new Label("Last Updated: --");
        lastUpdated.setStyle("-fx-font-size: 11px; -fx-text-fill: #777;");

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
        statsCard.setPrefWidth(260);
        statsCard.setPrefHeight(260);

        // ===== PIE CHART =====
        PieChart pieChart = new PieChart();
        pieChart.setPrefSize(320, 250);

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
        chartCard.setPrefWidth(360);
        chartCard.setPrefHeight(260);

        // ===== BAR CHART =====
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();

        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
        barChart.setLegendVisible(false);
        barChart.setCategoryGap(20);
        barChart.setPrefSize(300, 250);
        xAxis.setTickLabelRotation(45);

        Label barTitle = new Label("Total Delay (Minutes) by Airline");
        barTitle.setStyle("-fx-font-weight: bold;");

        VBox barCard = new VBox(10, barTitle, barChart);
        barCard.setPadding(new Insets(15));
        barCard.setStyle("""
            -fx-background-color: white;
            -fx-border-color: #e0e0e0;
            -fx-background-radius: 10;
            -fx-border-radius: 10;
        """);
        barCard.setPrefWidth(320);
        barCard.setPrefHeight(260);

        // ===== DESTINATION IMAGE =====
        ImageView cityImage = new ImageView();
        cityImage.setFitWidth(350);
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
        imageCard.setPrefWidth(300);
        imageCard.setPrefHeight(260);


        // ===== ROWS =====
        HBox row1 = new HBox(15, statsCard, chartCard, barCard, imageCard);
        row1.setAlignment(Pos.CENTER);


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
            barChart,
            cityImage,
            destinationLabel
        );

        refreshBtn.setOnAction(e -> controller.refreshData());

        // ===== MAIN CONTENT =====
        VBox content = new VBox(20, filters, tableCard, row1);
        content.setPadding(new Insets(20));

        // ===== ROOT =====
        VBox root = new VBox(header, content);
        root.setStyle("-fx-background-color: #f4f6f8;");

        // ===== SCENE =====
        Scene scene = new Scene(root, 1400, 850);
        stage.setScene(scene);
        stage.setTitle("Airline Dashboard");
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}