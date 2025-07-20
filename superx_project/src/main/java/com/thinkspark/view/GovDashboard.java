package com.thinkspark.view;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class GovDashboard extends Application {

    // --- Style Definitions (Light Theme) ---
    private static final String BACKGROUND_COLOR = "#F4F7FC";
    private static final String CARD_BACKGROUND_COLOR = "#FFFFFF";
    private static final String PRIMARY_BLUE = "#005A9C";
    private static final String SECONDARY_ACCENT_COLOR = "#4299E1"; // A bright, professional blue
    private static final String FONT_COLOR_DARK = "#2D3748";
    private static final String FONT_COLOR_MEDIUM = "#4A5568";
    private static final String FONT_COLOR_LIGHT = "#FFFFFF";
    private static final String BORDER_COLOR = "#E2E8F0";
    private static final String PRIMARY_ACCENT_ORANGE = "#FCA311";

    // --- Fields to hold stage and scene reference ---
    private Stage primaryStage;
    private Scene dashboardScene;

    // --- Font Definitions ---
    private static Font getFont(double size, FontWeight weight) {
        return Font.font("System", weight, size);
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;

        VBox rootLayout = new VBox(40);
        rootLayout.setPadding(new Insets(20, 50, 50, 50));
        rootLayout.setStyle("-fx-background-color: " + BACKGROUND_COLOR + ";");
        rootLayout.setAlignment(Pos.TOP_CENTER);

        Node header = createHeader();
        Node overviewSection = createOverviewSection();
        GridPane mainGrid = createMainGrid();
        rootLayout.getChildren().addAll(header, overviewSection, mainGrid);

        ScrollPane scrollPane = new ScrollPane(rootLayout);
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setStyle("-fx-background-color: " + BACKGROUND_COLOR + "; -fx-background: " + BACKGROUND_COLOR + ";");
        scrollPane.getStyleClass().add("edge-to-edge");

        dashboardScene = new Scene(scrollPane, 1366, 900);
        primaryStage.setTitle("Government Dashboard | Disaster Command Center");
        primaryStage.setScene(dashboardScene);
        primaryStage.show();
    }

    private GridPane createMainGrid() {
        GridPane mainGrid = new GridPane();
        mainGrid.setHgap(40);
        mainGrid.setVgap(40);
        mainGrid.setAlignment(Pos.CENTER);

        mainGrid.add(createQuickAccessModule(), 0, 0);
        mainGrid.add(createLiveAlertsModule(), 1, 0);
        mainGrid.add(createStatisticsModule(), 0, 1, 2, 1);
        return mainGrid;
    }

    private Node createHeader() {
        HBox header = new HBox();
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(10, 0, 20, 0));

        Label logo = new Label("🏛️ Helper Command");
        logo.setFont(getFont(32, FontWeight.BOLD));
        logo.setTextFill(Color.web(PRIMARY_BLUE));

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox navLinks = new HBox(40);
        navLinks.setAlignment(Pos.CENTER);
        String[] navItems = { "Dashboard", "Analytics", "Reports" };
        for (String item : navItems) {
            Label link = new Label(item);
            link.setFont(getFont(16, FontWeight.MEDIUM));
            link.setTextFill(Color.web(FONT_COLOR_DARK));
            link.setStyle("-fx-cursor: hand;");
            link.setOnMouseEntered(e -> link.setUnderline(true));
            link.setOnMouseExited(e -> link.setUnderline(false));
            
            if ("Dashboard".equals(item)) {
                link.setOnMouseClicked(e -> primaryStage.setScene(dashboardScene));
            } else if ("Analytics".equals(item)) {
                link.setOnMouseClicked(e -> new AnalyticsView(primaryStage, dashboardScene).start(primaryStage));
            } else if ("Reports".equals(item)) {
                link.setOnMouseClicked(e -> new AllRequestsView(primaryStage, dashboardScene).start(primaryStage));
            }
            
            navLinks.getChildren().add(link);
        }
        
        MenuButton profileButton = new MenuButton("Profile");
        profileButton.setFont(getFont(16, FontWeight.MEDIUM));
        MenuItem settingsItem = new MenuItem("Settings");
        MenuItem logoutItem = new MenuItem("Log Out");
        profileButton.getItems().addAll(settingsItem, logoutItem);

        settingsItem.setOnAction(e -> new SettingsView(primaryStage, dashboardScene).start(primaryStage));
        logoutItem.setOnAction(e -> {
            primaryStage.close();
            new DisasterHelperUI().start(new Stage());
        });
        
        navLinks.getChildren().add(profileButton);

        header.getChildren().addAll(logo, spacer, navLinks);
        return header;
    }

    private Node createOverviewSection() {
        VBox overviewBox = new VBox(15);
        overviewBox.setAlignment(Pos.CENTER_LEFT);
        overviewBox.setPadding(new Insets(30));
        overviewBox.setStyle("-fx-background-color: " + CARD_BACKGROUND_COLOR + "; -fx-background-radius: 16;");

        Label title = new Label("Command Center Overview");
        title.setFont(getFont(36, FontWeight.BOLD));
        title.setTextFill(Color.web(FONT_COLOR_DARK));

        Label subtitle = new Label("Monitor real-time alerts, manage resources, and analyze disaster response data.");
        subtitle.setFont(getFont(18, FontWeight.NORMAL));
        subtitle.setTextFill(Color.web(FONT_COLOR_MEDIUM));
        subtitle.setWrapText(true);

        overviewBox.getChildren().addAll(title, subtitle);
        return overviewBox;
    }

    private Node createQuickAccessModule() {
        VBox card = createDashboardCard("Quick Access");
        GridPane buttonGrid = new GridPane();
        buttonGrid.setHgap(20);
        buttonGrid.setVgap(20);
        buttonGrid.setPadding(new Insets(15, 0, 0, 0));
        
        Button broadcastButton = createQuickAccessButton("📢 Broadcast Alerts", true);
        broadcastButton.setOnAction(e -> new BroadcastAlertsView(primaryStage, dashboardScene).start(primaryStage));
        
        Button manageNgosButton = createQuickAccessButton("🌐 Manage NGOs", false);
        manageNgosButton.setOnAction(e -> new ManageNgosView(primaryStage, dashboardScene).start(primaryStage));
        
        Button liveMapButton = createQuickAccessButton("🗺️ Live Map", false);
        // Add map functionality later
        
        Button analyticsButton = createQuickAccessButton("📊 Analytics", false);
        analyticsButton.setOnAction(e -> new AnalyticsView(primaryStage, dashboardScene).start(primaryStage));

        buttonGrid.add(broadcastButton, 0, 0);
        buttonGrid.add(manageNgosButton, 1, 0);
        buttonGrid.add(liveMapButton, 0, 1);
        buttonGrid.add(analyticsButton, 1, 1);

        card.getChildren().add(buttonGrid);
        return card;
    }
    
    private Button createQuickAccessButton(String text, boolean isPrimary) {
        Button button = new Button(text);
        button.setFont(getFont(16, FontWeight.BOLD));
        button.setPrefSize(250, 80);
        GridPane.setHgrow(button, Priority.ALWAYS);
        GridPane.setVgrow(button, Priority.ALWAYS);

        String style;
        if (isPrimary) {
            button.setTextFill(Color.web(FONT_COLOR_LIGHT));
            style = "-fx-background-color: " + PRIMARY_BLUE + ";";
        } else {
            button.setTextFill(Color.web(FONT_COLOR_DARK));
            style = "-fx-background-color: " + BACKGROUND_COLOR + "; -fx-border-color: " + BORDER_COLOR + ";";
        }
        
        button.setStyle(style + " -fx-background-radius: 10; -fx-border-radius: 10; -fx-cursor: hand;");
        return button;
    }

    private Node createLiveAlertsModule() {
        VBox card = createDashboardCard("Live Alerts");
        VBox alertsContainer = new VBox(15);
        alertsContainer.setPadding(new Insets(15, 0, 0, 0));
        
        alertsContainer.getChildren().add(createAlertItem("New Request: Medical Aid in Sector 7", "High Priority"));
        alertsContainer.getChildren().add(createAlertItem("Resource Depletion Warning: Water Supply", "Medium Priority"));
        alertsContainer.getChildren().add(createAlertItem("Volunteer Check-in: Team Bravo", "Low Priority"));
        
        card.getChildren().add(alertsContainer);
        return card;
    }
    
    private Node createAlertItem(String text, String priority) {
        VBox item = new VBox(3);
        Label alertLabel = new Label(text);
        alertLabel.setFont(getFont(15, FontWeight.MEDIUM));
        alertLabel.setTextFill(Color.web(FONT_COLOR_DARK));
        alertLabel.setWrapText(true);
        
        Label priorityLabel = new Label(priority);
        priorityLabel.setFont(getFont(12, FontWeight.BOLD));
        
        String priorityColor = PRIMARY_ACCENT_ORANGE;
        if ("Medium Priority".equals(priority)) {
            priorityColor = SECONDARY_ACCENT_COLOR;
        } else if ("Low Priority".equals(priority)) {
            priorityColor = FONT_COLOR_MEDIUM;
        }
        priorityLabel.setTextFill(Color.web(priorityColor));
        
        item.getChildren().addAll(alertLabel, priorityLabel);
        return item;
    }

    private Node createStatisticsModule() {
        VBox card = createDashboardCard("Key Performance Indicators");
        HBox statsBox = new HBox();
        statsBox.setAlignment(Pos.CENTER);
        statsBox.setSpacing(40);
        statsBox.setPadding(new Insets(15, 0, 0, 0));
        HBox.setHgrow(statsBox, Priority.ALWAYS);

        String[][] statsData = {
            { "12K", "People Rescued" },
            { "56K", "Requests Handled" },
            { "343", "Active Volunteers" },
            { "39K", "Resources Delivered" }
        };

        for (String[] stat : statsData) {
            Node statItem = createStatItem(stat[0], stat[1]);
            HBox.setHgrow(statItem, Priority.ALWAYS);
            statsBox.getChildren().add(statItem);
        }

        card.getChildren().add(statsBox);
        return card;
    }

    private Node createStatItem(String number, String label) {
        VBox item = new VBox(-5);
        item.setAlignment(Pos.CENTER);
        
        Label numberLabel = new Label(number);
        numberLabel.setFont(getFont(48, FontWeight.BOLD));
        numberLabel.setTextFill(Color.web(PRIMARY_BLUE));
        
        Label textLabel = new Label(label);
        textLabel.setFont(getFont(16, FontWeight.NORMAL));
        textLabel.setTextFill(Color.web(FONT_COLOR_MEDIUM));
        
        item.getChildren().addAll(numberLabel, textLabel);
        return item;
    }
    
    private VBox createDashboardCard(String title) {
        VBox card = new VBox(10);
        card.setPadding(new Insets(25));
        card.setStyle("-fx-background-color: " + CARD_BACKGROUND_COLOR + "; " +
                      "-fx-background-radius: 12; " +
                      "-fx-border-color: " + BORDER_COLOR + "; " +
                      "-fx-border-width: 1;");
        GridPane.setHgrow(card, Priority.ALWAYS);
        GridPane.setVgrow(card, Priority.ALWAYS);
        
        Label titleLabel = new Label(title);
        titleLabel.setFont(getFont(22, FontWeight.BOLD));
        titleLabel.setTextFill(Color.web(FONT_COLOR_DARK));
        
        card.getChildren().add(titleLabel);
        return card;
    }

}
