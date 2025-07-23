package com.thinkspark.view;

import com.thinkspark.dao.DisasterDao;
import com.thinkspark.model.Disaster;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class GovDashboard extends Application {

    // --- Style Definitions ---
    private static final String BACKGROUND_COLOR = "#F4F7FC";
    private static final String CARD_BACKGROUND_COLOR = "#FFFFFF";
    private static final String PRIMARY_BLUE = "#005A9C";
    private static final String FONT_COLOR_DARK = "#2D3748";
    private static final String FONT_COLOR_MEDIUM = "#4A5568";
    private static final String FONT_COLOR_LIGHT = "#FFFFFF";
    private static final String BORDER_COLOR = "#E2E8F0";
    private static final String ALERT_COLOR = "#E53E3E";

    private Stage primaryStage;
    private Scene dashboardScene;
    private DisasterDao disasterDao;
    private ObservableList<Disaster> liveDisasterReportsList;
    private String userName;

    public GovDashboard() {
        this.userName = "Admin"; 
    }

    private static Font getFont(double size, FontWeight weight) {
        return Font.font("System", weight, size);
    }

    public GovDashboard(String userName) {
        this.userName = (userName == null || userName.isEmpty()) ? "Admin" : userName;
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;

        // --- Initialize DAO and start listening for data ---
        disasterDao = new DisasterDao();
        liveDisasterReportsList = FXCollections.observableArrayList();
        disasterDao.listenForDisasterReports(liveDisasterReportsList);

        VBox rootLayout = new VBox(40);
        rootLayout.setPadding(new Insets(20, 50, 50, 50));
        rootLayout.setStyle("-fx-background-color: " + BACKGROUND_COLOR + ";");
        rootLayout.setAlignment(Pos.TOP_CENTER);

        rootLayout.getChildren().addAll(createHeader(), createOverviewSection(), createMainGrid());

        ScrollPane scrollPane = new ScrollPane(rootLayout);
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        dashboardScene = new Scene(scrollPane);
        primaryStage.setTitle("Safe Serve");
        primaryStage.setScene(dashboardScene);
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/logo.jpg")));
        primaryStage.show();
    }

    private GridPane createMainGrid() {
        GridPane mainGrid = new GridPane();
        mainGrid.setHgap(40);
        mainGrid.setVgap(40);
        mainGrid.setAlignment(Pos.CENTER);

        mainGrid.add(createKeyActionsModule(), 0, 0);
        mainGrid.add(createDisasterReportsModule(), 1, 0);
        mainGrid.add(createStatisticsModule(), 0, 1, 2, 1);
        return mainGrid;
    }

    private Node createHeader() {
        HBox header = new HBox();
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(10, 0, 20, 0));

        // Create an ImageView for the logo
        ImageView logoView = new ImageView(new Image(getClass().getResourceAsStream("/logo.jpg")));
        logoView.setFitHeight(40); // Adjust size as needed
        logoView.setPreserveRatio(true);

        // Create the text label
        Label logoLabel = new Label("Safe Serve");
        logoLabel.setFont(getFont(32, FontWeight.BOLD));
        logoLabel.setTextFill(Color.web(PRIMARY_BLUE));

        // Combine image and text into an HBox
        HBox logoBox = new HBox(10, logoView, logoLabel);
        logoBox.setAlignment(Pos.CENTER_LEFT);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        MenuButton profileButton = new MenuButton("Admin");
        profileButton.setFont(getFont(16, FontWeight.MEDIUM));
        MenuItem settingsItem = new MenuItem("Settings");
        MenuItem logoutItem = new MenuItem("Log Out");
        profileButton.getItems().addAll(settingsItem, logoutItem);
        
        
        settingsItem.setOnAction(e -> new SettingsView(primaryStage, dashboardScene, this.userName).start(primaryStage));
        logoutItem.setOnAction(e -> {
            // Reuse the existing primary stage to go back to DisasterHelperUI
            new DisasterHelperUI().start(primaryStage);
        });

        header.getChildren().addAll(logoBox, spacer, profileButton);
        return header;
    }

    private Node createOverviewSection() {
        VBox overviewBox = new VBox(15);
        overviewBox.setPadding(new Insets(30));
        overviewBox.setStyle("-fx-background-color: " + CARD_BACKGROUND_COLOR + "; -fx-background-radius: 16;");
        Label title = new Label("Command Center Overview");
        title.setFont(getFont(36, FontWeight.BOLD));
        title.setTextFill(Color.web(FONT_COLOR_DARK));
        Label subtitle = new Label("Monitor real-time reports, manage partner NGOs, and broadcast critical alerts.");
        subtitle.setFont(getFont(18, FontWeight.NORMAL));
        subtitle.setTextFill(Color.web(FONT_COLOR_MEDIUM));
        overviewBox.getChildren().addAll(title, subtitle);
        return overviewBox;
    }

    private Node createKeyActionsModule() {
        VBox card = createDashboardCard("Key Actions");
        GridPane buttonGrid = new GridPane();
        buttonGrid.setHgap(20);
        buttonGrid.setVgap(20);
        buttonGrid.setPadding(new Insets(15, 0, 0, 0));

        Button broadcastButton = createQuickAccessButton("Broadcast Alert", true);
        broadcastButton.setOnAction(e -> new BroadcastAlertsView(primaryStage, dashboardScene).start(primaryStage));

        Button manageNgosButton = createQuickAccessButton("Manage NGOs", false);
        manageNgosButton.setOnAction(e -> new ManageNgosView(primaryStage, dashboardScene).start(primaryStage));

        buttonGrid.add(broadcastButton, 0, 0);
        buttonGrid.add(manageNgosButton, 1, 0);

        card.getChildren().add(buttonGrid);
        return card;
    }

    private Button createQuickAccessButton(String text, boolean isPrimary) {
        Button button = new Button(text);
        button.setFont(getFont(16, FontWeight.BOLD));
        button.setPrefSize(250, 80);
        GridPane.setHgrow(button, Priority.ALWAYS);
        if (isPrimary) {
            button.setTextFill(Color.web(FONT_COLOR_LIGHT));
            button.setStyle("-fx-background-color: " + PRIMARY_BLUE + "; -fx-background-radius: 10; -fx-cursor: hand;");
        } else {
            button.setTextFill(Color.web(FONT_COLOR_DARK));
            button.setStyle("-fx-background-color: " + BACKGROUND_COLOR + "; -fx-border-color: " + BORDER_COLOR + "; -fx-border-radius: 10; -fx-cursor: hand;");
        }
        return button;
    }
    
    private Node createDisasterReportsModule() {
        VBox card = createDashboardCard("Incoming Disaster Reports");
        ListView<Disaster> reportsList = new ListView<>(liveDisasterReportsList);
        reportsList.setPlaceholder(new Label("No new disaster reports from citizens."));
        reportsList.setCellFactory(param -> new ListCell<Disaster>() {
            @Override
            protected void updateItem(Disaster item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    Text type = new Text(item.getType() + " at " + item.getLocation());
                    type.setFont(GovDashboard.getFont(14, FontWeight.BOLD));
                    type.setFill(Color.web(ALERT_COLOR));
                    
                    Text description = new Text(item.getDescription());
                    description.setWrappingWidth(param.getWidth() - 30);
                    
                    VBox content = new VBox(5, type, description);
                    setGraphic(content);
                }
            }
        });
        VBox.setVgrow(reportsList, Priority.ALWAYS);
        card.getChildren().add(reportsList);
        return card;
    }

    private Node createStatisticsModule() {
        VBox card = createDashboardCard("Key Performance Indicators");
        HBox statsBox = new HBox(40);
        statsBox.setAlignment(Pos.CENTER);
        statsBox.setPadding(new Insets(15, 0, 0, 0));
        HBox.setHgrow(statsBox, Priority.ALWAYS);

        statsBox.getChildren().addAll(
            createStatItem("12K", "People Rescued"),
            createStatItem("56K", "Requests Handled"),
            createStatItem("3", "Verified NGOs"),
            createStatItem("343", "Total Volunteers")
        );

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