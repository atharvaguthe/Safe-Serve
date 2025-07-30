package com.thinkspark.view;

import com.thinkspark.dao.AlertDao;
import com.thinkspark.dao.DisasterDao;
import com.thinkspark.model.Alert;
import com.thinkspark.model.Disaster;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class NgoDashboard extends Application {

    // --- Style Definitions ---
    private static final String BACKGROUND_COLOR = "#F4F7FC";
    private static final String CARD_BACKGROUND_COLOR = "#FFFFFF";
    private static final String PRIMARY_BLUE = "#005A9C";
    private static final String FONT_COLOR_DARK = "#2D3748";
    private static final String FONT_COLOR_MEDIUM = "#4A5568";
    private static final String FONT_COLOR_LIGHT = "#FFFFFF";
    private static final String BORDER_COLOR = "#E2E8F0";
    private static final String ALERT_COLOR = "#E53E3E";
    private static final String PRIMARY_ACCENT_ORANGE = "#FCA311";
    private static final String STATUS_COMPLETED_COLOR = "#38A169";
    // Added from DisasterHelperUI for the floating button
    private static final String SECONDARY_BLUE = "#007BFF";

    private Stage primaryStage;
    private Scene dashboardScene;

    private AlertDao alertDao;
    private DisasterDao disasterDao;
    private ObservableList<Alert> liveAlertsList;
    private ObservableList<Disaster> liveDisasterReportsList;
    private String userName;

    private NgoDashboard() {
        this.userName = "NGO";
    }


    private static Font getFont(double size, FontWeight weight) {
        return Font.font("System", weight, size);
    }

    public NgoDashboard(String userName) {
        this.userName = (userName == null || userName.isEmpty()) ? "NGO" : userName;
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;

        alertDao = new AlertDao();
        disasterDao = new DisasterDao();
        liveAlertsList = FXCollections.observableArrayList();
        liveDisasterReportsList = FXCollections.observableArrayList();
        alertDao.listenForAlerts(liveAlertsList);
        disasterDao.listenForDisasterReports(liveDisasterReportsList);


        VBox rootLayout = new VBox(60); // Increased spacing
        rootLayout.setPadding(new Insets(40, 80, 80, 80)); // Increased padding
        rootLayout.setStyle("-fx-background-color: " + BACKGROUND_COLOR + ";");
        rootLayout.setAlignment(Pos.TOP_CENTER);

        Node header = createHeader();
        Node overviewSection = createOverviewSection();
        GridPane mainGrid = createMainGrid();
        rootLayout.getChildren().addAll(header, overviewSection, mainGrid);

        ScrollPane scrollPane = new ScrollPane(rootLayout);
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        // --- Floating Circular Button (Copied from DisasterHelperUI) ---
        Button floatingButton = new Button("🤖");
        floatingButton.setStyle(
            "-fx-background-color: " + SECONDARY_BLUE + "; " + // Using SECONDARY_BLUE defined above
            "-fx-background-radius: 50%; " +
            "-fx-min-width: 90px; -fx-min-height: 90px; " +
            "-fx-max-width: 90px; -fx-max-height: 90px; " +
            "-fx-font-size: 36px; " +
            "-fx-text-fill: " + FONT_COLOR_LIGHT + "; " +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.4), 20, 0, 0, 10); " +
            "-fx-cursor: hand;"
        );
        StackPane.setAlignment(floatingButton, Pos.BOTTOM_RIGHT);
        StackPane.setMargin(floatingButton, new Insets(0, 60, 180, 0));

        // --- Create a new StackPane as the overall root for the Scene ---
        StackPane overallRoot = new StackPane();
        overallRoot.getChildren().addAll(scrollPane, floatingButton);

        dashboardScene = new Scene(overallRoot,1920,1080); // Set scene to overallRoot
        primaryStage.setTitle("Safe Serve");
        primaryStage.setScene(dashboardScene);
        primaryStage.setMaximized(true);
        primaryStage.setResizable(true);
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/logo.png")));
        primaryStage.show();

        // Set action for the floating button
        final Scene finalDashboardScene = dashboardScene; // Need a final variable for the lambda
        floatingButton.setOnAction(e -> {
            new FloatingButtonPage(primaryStage, finalDashboardScene).start(primaryStage);
        });
    }

    private GridPane createMainGrid() {
        GridPane mainGrid = new GridPane();
        mainGrid.setHgap(50); // Increased gap
        mainGrid.setVgap(50); // Increased gap
        mainGrid.setAlignment(Pos.CENTER);

        mainGrid.add(createQuickAccessModule(), 0, 0);
        mainGrid.add(createLiveFeedsModule(), 1, 0); // Combined feeds
        // FIX: Pass primaryStage and dashboardScene to createStatisticsModule
        mainGrid.add(createStatisticsModule(primaryStage, dashboardScene), 0, 1, 2, 1); // Row span for statistics module
        return mainGrid;
    }

    private Node createHeader() {
        HBox header = new HBox();
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(20, 0, 30, 0)); // Increased padding

        // Create an ImageView for the logo
        ImageView logoView = new ImageView(new Image(getClass().getResourceAsStream("/logo.png")));
        logoView.setFitHeight(60); // Increased size
        logoView.setPreserveRatio(true);

        // Create the text label
        Label logoLabel = new Label("Safe Serve");
        logoLabel.setFont(getFont(42, FontWeight.BOLD)); // Increased font size
        logoLabel.setTextFill(Color.web(PRIMARY_BLUE));

        // Combine image and text into an HBox
        HBox logoBox = new HBox(20, logoView, logoLabel); // Increased spacing
        logoBox.setAlignment(Pos.CENTER_LEFT);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox navLinks = new HBox(50); // Increased spacing
        navLinks.setAlignment(Pos.CENTER);
        String[] navItems = { "Dashboard", "Volunteers", "Reports" };
        for (String item : navItems) {
            Label link = new Label(item);
            link.setFont(getFont(20, FontWeight.MEDIUM)); // Increased font size
            link.setTextFill(Color.web(FONT_COLOR_DARK));
            link.setStyle("-fx-cursor: hand;");
            link.setOnMouseEntered(e -> link.setUnderline(true));
            link.setOnMouseExited(e -> link.setUnderline(false));

            if ("Dashboard".equals(item)) {
                link.setOnMouseClicked(e -> primaryStage.setScene(dashboardScene));
            } else if ("Volunteers".equals(item)) {
                link.setOnMouseClicked(e -> new ManageVolunteersView(primaryStage, dashboardScene).start(primaryStage));
            } else if ("Reports".equals(item)) {
                link.setOnMouseClicked(e -> new AllRequestsView(primaryStage, dashboardScene).start(primaryStage));
            }

            navLinks.getChildren().add(link);
        }

        MenuButton profileButton = new MenuButton("Profile");
        profileButton.setFont(getFont(20, FontWeight.MEDIUM)); // Increased font size
        MenuItem profileItem = new MenuItem("Profile");
        MenuItem logoutItem = new MenuItem("Log Out");
        profileButton.getItems().addAll(profileItem, logoutItem);

        profileItem.setOnAction(e -> new ProfileView(primaryStage, dashboardScene, this.userName).start(primaryStage));

        logoutItem.setOnAction(e -> {
            new DisasterHelperUI().start(primaryStage);
        });

        navLinks.getChildren().add(profileButton);

        header.getChildren().addAll(logoBox, spacer, navLinks);
        return header;
    }

    private Node createOverviewSection() {
        VBox overviewBox = new VBox(25); // Increased spacing
        overviewBox.setAlignment(Pos.CENTER_LEFT);
        overviewBox.setPadding(new Insets(50)); // Increased padding
        overviewBox.setStyle("-fx-background-color: " + CARD_BACKGROUND_COLOR + "; -fx-background-radius: 20; -fx-border-color: " + BORDER_COLOR + "; -fx-border-width: 1;"); // Increased radius

        Label title = new Label("Operations Dashboard");
        title.setFont(getFont(48, FontWeight.BOLD)); // Increased font size
        title.setTextFill(Color.web(FONT_COLOR_DARK));

        Label subtitle = new Label("Coordinate your team, manage requests, and track your impact in real-time.");
        subtitle.setFont(getFont(22, FontWeight.NORMAL)); // Increased font size
        subtitle.setTextFill(Color.web(FONT_COLOR_MEDIUM));
        subtitle.setWrapText(true);

        overviewBox.getChildren().addAll(title, subtitle);
        return overviewBox;
    }

    private Node createQuickAccessModule() {
        VBox card = createDashboardCard("Quick Actions");
        GridPane buttonGrid = new GridPane();
        buttonGrid.setHgap(30); // Increased gap
        buttonGrid.setVgap(30); // Increased gap
        buttonGrid.setPadding(new Insets(25, 0, 0, 0)); // Increased padding

        Button allRequestsButton = createQuickAccessButton("View & Assign Tasks", true);
        allRequestsButton.setOnAction(e -> new AllRequestsView(primaryStage, dashboardScene).start(primaryStage));

        Button manageVolunteersButton = createQuickAccessButton("Manage Volunteers", false);
        manageVolunteersButton.setOnAction(e -> new ManageVolunteersView(primaryStage, dashboardScene).start(primaryStage));

        Button viewMapButton = createQuickAccessButton("View Map", false);
        viewMapButton.setOnAction(e -> new MapView(primaryStage, dashboardScene).start(primaryStage));

        Button viewDonationsButton = createQuickAccessButton("View Donations", false);
        viewDonationsButton.setOnAction(e -> new ViewDonationsView(primaryStage, dashboardScene).start(primaryStage));

        buttonGrid.add(allRequestsButton, 0, 0);
        buttonGrid.add(manageVolunteersButton, 1, 0);
        buttonGrid.add(viewMapButton, 0, 1);
        buttonGrid.add(viewDonationsButton, 1, 1);

        card.getChildren().add(buttonGrid);
        return card;
    }

    private Button createQuickAccessButton(String text, boolean isPrimary) {
        Button button = new Button(text);
        button.setFont(getFont(20, FontWeight.BOLD)); // Increased font size
        button.setPrefSize(300, 100); // Increased size
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

        button.setStyle(style + " -fx-background-radius: 15; -fx-border-radius: 15; -fx-cursor: hand;"); // Increased radius
        return button;
    }

    private Node createLiveFeedsModule() {
        VBox card = createDashboardCard("Live Feeds");

        // Citizen Disaster Reports Section
        Label disasterReportsTitle = new Label("Citizen Disaster Reports");
        disasterReportsTitle.setFont(getFont(20, FontWeight.BOLD)); // Increased font size

        ListView<Disaster> disasterReportsList = new ListView<>(liveDisasterReportsList);
        disasterReportsList.setPlaceholder(new Label("No new disaster reports."));
        disasterReportsList.setPrefHeight(350); // Set a preferred height
        disasterReportsList.setCellFactory(param -> new ListCell<Disaster>() {
            @Override
            protected void updateItem(Disaster item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    Text typeAndLocation = new Text(item.getType() + " at " + item.getLocation());
                    typeAndLocation.setFont(NgoDashboard.getFont(18, FontWeight.BOLD)); // Increased font size

                    // Safely check item.getStatus() before using it
                    String status = item.getStatus();
                    String statusColor = ALERT_COLOR; // Default for pending/unassigned
                    if (status != null) { // Add null check
                        if (status.equalsIgnoreCase("assigned")) {
                            statusColor = PRIMARY_BLUE;
                        } else if (status.equalsIgnoreCase("completed")) {
                            statusColor = STATUS_COMPLETED_COLOR; // Use green for completed
                        }
                    }
                    typeAndLocation.setFill(Color.web(statusColor));

                    Text description = new Text(item.getDescription());
                    description.setWrappingWidth(param.getWidth() - 50); // Adjusted wrapping width
                    description.setFont(NgoDashboard.getFont(16, FontWeight.NORMAL)); // Set font size

                    // Display status explicitly
                    // Safely display status, defaulting to "Unknown" if null
                    Text statusText = new Text("Status: " + (status != null ? status : "Unknown"));
                    statusText.setFont(NgoDashboard.getFont(14, FontWeight.MEDIUM)); // Increased font size
                    statusText.setFill(Color.web(statusColor));

                    VBox content = new VBox(10, typeAndLocation, description, statusText); // Increased spacing
                    content.setPadding(new Insets(10)); // Added padding
                    setGraphic(content);
                }
            }
        });

        // Government Alerts Section
        Label govAlertsTitle = new Label("Government Alerts");
        govAlertsTitle.setFont(getFont(20, FontWeight.BOLD)); // Increased font size
        govAlertsTitle.setPadding(new Insets(20, 0, 0, 0)); // Increased padding

        ListView<Alert> govAlertsList = new ListView<>(liveAlertsList);
        govAlertsList.setPlaceholder(new Label("No new government alerts."));
        govAlertsList.setPrefHeight(350); // Set a preferred height
        govAlertsList.setCellFactory(param -> new ListCell<Alert>() {
            @Override
            protected void updateItem(Alert item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    Text title = new Text(item.getTitle());
                    title.setFont(NgoDashboard.getFont(18, FontWeight.BOLD)); // Increased font size

                    Text message = new Text(item.getMessage());
                    message.setWrappingWidth(param.getWidth() - 50); // Adjusted wrapping width
                    message.setFont(NgoDashboard.getFont(16, FontWeight.NORMAL)); // Set font size

                    String priorityColor = "High".equalsIgnoreCase(item.getPriority()) ? PRIMARY_ACCENT_ORANGE : FONT_COLOR_DARK;
                    title.setFill(Color.web(priorityColor));

                    VBox content = new VBox(10, title, message); // Increased spacing
                    content.setPadding(new Insets(10)); // Added padding
                    setGraphic(content);
                }
            }
        });

        VBox.setVgrow(disasterReportsList, Priority.ALWAYS);
        VBox.setVgrow(govAlertsList, Priority.ALWAYS);

        card.getChildren().addAll(disasterReportsTitle, disasterReportsList, govAlertsTitle, govAlertsList);
        return card;
    }

    private Node createStatisticsModule(Stage stage, Scene scene) { // Method now accepts stage and scene
        VBox card = createDashboardCard("Your Organization's Impact");
        HBox statsBox = new HBox();
        statsBox.setAlignment(Pos.CENTER);
        statsBox.setSpacing(60); // Increased spacing
        statsBox.setPadding(new Insets(25, 0, 0, 0)); // Increased padding
        HBox.setHgrow(statsBox, Priority.ALWAYS);

        String[][] statsData = {
            { "1.2K", "People Aided" },
            { "4.8K", "Requests Fulfilled" },
            { "343", "Active Volunteers" },
            { "8.2K", "Items Delivered" }
        };

        for (String[] stat : statsData) {
            Node statItem = createStatItem(stat[0], stat[1]);
            HBox.setHgrow(statItem, Priority.ALWAYS);
            statsBox.getChildren().add(statItem);
        }

        card.getChildren().add(statsBox);

        // New button to open AnalyticsView
        Button viewAnalyticsButton = new Button("View Analytics");
        viewAnalyticsButton.setFont(getFont(20, FontWeight.BOLD));
        viewAnalyticsButton.setTextFill(Color.web(FONT_COLOR_LIGHT));
        viewAnalyticsButton.setStyle("-fx-background-color: " + PRIMARY_BLUE + "; -fx-background-radius: 10; -fx-cursor: hand;");
        viewAnalyticsButton.setPrefHeight(60);
        viewAnalyticsButton.setMaxWidth(Double.MAX_VALUE);
        VBox.setMargin(viewAnalyticsButton, new Insets(30, 0, 0, 0)); // Add top margin

        // Action to open AnalyticsView
        // This will require passing primary stage and dashboard scene to createStatisticsModule()
        // I will update the call to createStatisticsModule in createMainGrid()
        viewAnalyticsButton.setOnAction(e -> new AnalyticsView(stage, scene).start(stage));

        card.getChildren().add(viewAnalyticsButton); // Add the new button to the card

        return card;
    }

    private Node createStatItem(String number, String label) {
        VBox item = new VBox(-8); // Adjusted spacing
        item.setAlignment(Pos.CENTER);

        Label numberLabel = new Label(number);
        numberLabel.setFont(getFont(60, FontWeight.BOLD)); // Increased font size
        numberLabel.setTextFill(Color.web(PRIMARY_BLUE));

        Label textLabel = new Label(label);
        textLabel.setFont(getFont(20, FontWeight.NORMAL)); // Increased font size
        textLabel.setTextFill(Color.web(FONT_COLOR_MEDIUM));

        item.getChildren().addAll(numberLabel, textLabel);
        return item;
    }

    private VBox createDashboardCard(String title) {
        VBox card = new VBox(25); // Increased spacing
        card.setPadding(new Insets(35)); // Increased padding
        card.setStyle("-fx-background-color: " + CARD_BACKGROUND_COLOR + "; " +
                      "-fx-background-radius: 20; " + // Increased radius
                      "-fx-border-color: " + BORDER_COLOR + "; " +
                      "-fx-border-width: 1;");
        GridPane.setHgrow(card, Priority.ALWAYS);
        GridPane.setVgrow(card, Priority.ALWAYS);

        Label titleLabel = new Label(title);
        titleLabel.setFont(getFont(32, FontWeight.BOLD)); // Increased font size
        titleLabel.setTextFill(Color.web(FONT_COLOR_DARK));

        card.getChildren().add(titleLabel);
        return card;
    }
}