package com.thinkspark.view;

import com.thinkspark.dao.AlertDao;
import com.thinkspark.model.Alert;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class CitizenDashboard extends Application {

    private static final String BACKGROUND_COLOR = "#F4F7FC";
    private static final String PRIMARY_BLUE = "#005A9C";
    private static final String SECONDARY_ACCENT_COLOR = "#48BB78";
    private static final String ALERT_COLOR = "#E53E3E";
    private static final String FONT_COLOR_DARK = "#2D3748";
    private static final String FONT_COLOR_LIGHT = "#FFFFFF";
    private static final String CARD_BACKGROUND_COLOR = "#FFFFFF";
    private static final String BORDER_COLOR = "#E2E8F0";

    private Stage primaryStage;
    private Scene dashboardScene;
    private String userName;

    private AlertDao alertDao;
    private ObservableList<Alert> liveAlertsList;

    public CitizenDashboard() {
        this.userName = "Citizen";
    }

    public CitizenDashboard(String userName) {
        this.userName = (userName == null || userName.isEmpty()) ? "Citizen" : userName;
    }

    private static Font getFont(double size, FontWeight weight) {
        return Font.font("System", weight, size);
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;

        alertDao = new AlertDao();
        liveAlertsList = FXCollections.observableArrayList();
        alertDao.listenForAlerts(liveAlertsList);

        VBox rootLayout = new VBox(30);
        rootLayout.setPadding(new Insets(20, 40, 40, 40));
        rootLayout.setStyle("-fx-background-color: " + BACKGROUND_COLOR + ";");
        rootLayout.setAlignment(Pos.TOP_CENTER);

        rootLayout.getChildren().addAll(createHeader(), createHeroSection(), createMainGrid());

        ScrollPane scrollPane = new ScrollPane(rootLayout);
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        dashboardScene = new Scene(scrollPane, 1280, 900);
        primaryStage.setTitle("Citizen Dashboard | Disaster Relief Helper");
        primaryStage.setScene(dashboardScene);
        primaryStage.show();
    }
    
    private GridPane createMainGrid() {
        GridPane mainGrid = new GridPane();
        mainGrid.setHgap(30);
        mainGrid.setVgap(30);
        mainGrid.setAlignment(Pos.CENTER);

        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPercentWidth(50);
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setPercentWidth(50);
        mainGrid.getColumnConstraints().addAll(col1, col2);

        mainGrid.add(createQuickAccessModule(), 0, 0);
        mainGrid.add(createLiveAlertsModule(), 1, 0);
        mainGrid.add(createStatisticsModule(), 0, 1);
        mainGrid.add(createFeaturedNewsModule(), 1, 1);
        
        return mainGrid;
    }

    private Node createHeader() {
        HBox header = new HBox();
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(10, 0, 20, 0));

        Label logo = new Label("🏢 Helper");
        logo.setFont(getFont(32, FontWeight.BOLD));
        logo.setTextFill(Color.web(PRIMARY_BLUE));

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox navLinks = new HBox(30);
        navLinks.setAlignment(Pos.CENTER);
        String[] navItems = { "Home", "Assistance", "Resources", "About Us" };
        for (String item : navItems) {
            Label link = new Label(item);
            link.setFont(getFont(16, FontWeight.MEDIUM));
            link.setTextFill(Color.web(FONT_COLOR_DARK));
            link.setStyle("-fx-cursor: hand;");
            link.setOnMouseEntered(e -> link.setUnderline(true));
            link.setOnMouseExited(e -> link.setUnderline(false));
            
            if ("Home".equals(item)) {
                 link.setOnMouseClicked(e -> new CitizenDashboard(this.userName).start(primaryStage));
            } else if ("Assistance".equals(item)) {
                link.setOnMouseClicked(e -> new AssistanceView(primaryStage, dashboardScene, this.userName).start(primaryStage));
            } else if ("About Us".equals(item)) {
                link.setOnMouseClicked(e -> new AboutUsView(primaryStage, dashboardScene).start(primaryStage));
            } else if ("Resources".equals(item)) {
                link.setOnMouseClicked(e -> new ResourcesView(primaryStage, dashboardScene).start(primaryStage));
            }
            navLinks.getChildren().add(link);
        }

        MenuButton profileButton = new MenuButton(this.userName.substring(0, 1).toUpperCase());
        MenuItem settingsItem = new MenuItem("Settings");
        MenuItem logoutItem = new MenuItem("Log Out");
        profileButton.getItems().addAll(settingsItem, logoutItem);

        settingsItem.setOnAction(e -> new SettingsView(primaryStage, dashboardScene, this.userName).start(primaryStage));
        logoutItem.setOnAction(e -> {
            primaryStage.close();
            new DisasterHelperUI().start(new Stage());
        });

        profileButton.setFont(getFont(16, FontWeight.BOLD));
        profileButton.setStyle(
            "-fx-background-color: " + PRIMARY_BLUE + "; " +
            "-fx-text-fill: " + FONT_COLOR_LIGHT + "; " +
            "-fx-background-radius: 50em; -fx-padding: 0;"
        );
        profileButton.setMinSize(40, 40);
        profileButton.setMaxSize(40, 40);
        profileButton.setAlignment(Pos.CENTER);

        header.getChildren().addAll(logo, spacer, navLinks, profileButton);
        return header;
    }

    private Node createHeroSection() {
        StackPane heroPane = new StackPane();
        heroPane.setPadding(new Insets(40));
        heroPane.setStyle("-fx-background-color: linear-gradient(to right, " + PRIMARY_BLUE + ", #007ACC); -fx-background-radius: 12;");

        VBox content = new VBox(15);
        content.setAlignment(Pos.CENTER_LEFT);

        Label welcome = new Label("Welcome back, " + this.userName + "!");
        welcome.setFont(getFont(28, FontWeight.BOLD));
        welcome.setTextFill(Color.web(FONT_COLOR_LIGHT));

        Label subtitle = new Label("Your central hub for disaster assistance, resources, and real-time alerts.");
        subtitle.setFont(getFont(16, FontWeight.NORMAL));
        subtitle.setTextFill(Color.web(FONT_COLOR_LIGHT));
        subtitle.setOpacity(0.9);
        subtitle.setWrapText(true);

        Button reportButton = new Button("🚨 Report a Disaster");
        reportButton.setFont(getFont(18, FontWeight.BOLD));
        reportButton.setTextFill(Color.web(FONT_COLOR_LIGHT));
        reportButton.setStyle("-fx-background-color: " + ALERT_COLOR + "; -fx-background-radius: 8; -fx-cursor: hand;");
        reportButton.setPadding(new Insets(12, 20, 12, 20));
        VBox.setMargin(reportButton, new Insets(10, 0, 0, 0));

        // UPDATED to pass username
        reportButton.setOnAction(e -> new ReportDisasterView(primaryStage, dashboardScene, this.userName).start(primaryStage));

        content.getChildren().addAll(welcome, subtitle, reportButton);
        heroPane.getChildren().add(content);
        return heroPane;
    }

    private Node createQuickAccessModule() {
        VBox card = createDashboardCard("Quick Access");
        HBox buttonBox = new HBox(15);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setPadding(new Insets(10, 0, 0, 0));

        Node mapButton = createQuickAccessButton("🗺️ Map", "View disaster map");
        mapButton.setOnMouseClicked(e -> new MapView(primaryStage, dashboardScene).start(primaryStage));
        Node donateButton = createQuickAccessButton("💖 Donate", "Support the cause");
        Node myRequestsButton = createQuickAccessButton("📋 My Requests", "See your requests");

        myRequestsButton.setOnMouseClicked(e -> new MyRequestsView(primaryStage, dashboardScene, this.userName).start(primaryStage));
        donateButton.setOnMouseClicked(e -> new DonationsView(primaryStage, dashboardScene).start(primaryStage));

        buttonBox.getChildren().addAll(mapButton, donateButton, myRequestsButton);
        card.getChildren().add(buttonBox);
        return card;
    }

    private Node createQuickAccessButton(String text, String subtext) {
        VBox buttonVBox = new VBox(5);
        buttonVBox.setAlignment(Pos.CENTER);
        buttonVBox.setPadding(new Insets(20, 10, 20, 10));
        buttonVBox.setStyle("-fx-background-color: " + BACKGROUND_COLOR + "; -fx-background-radius: 8; -fx-border-color: " + BORDER_COLOR + "; -fx-border-radius: 8; -fx-cursor: hand;");
        HBox.setHgrow(buttonVBox, Priority.ALWAYS);

        buttonVBox.setOnMouseEntered(e -> buttonVBox.setStyle("-fx-background-color: #E2E8F0; -fx-background-radius: 8; -fx-border-color: " + BORDER_COLOR + "; -fx-border-radius: 8; -fx-cursor: hand;"));
        buttonVBox.setOnMouseExited(e -> buttonVBox.setStyle("-fx-background-color: " + BACKGROUND_COLOR + "; -fx-background-radius: 8; -fx-border-color: " + BORDER_COLOR + "; -fx-border-radius: 8; -fx-cursor: hand;"));
        
        Label textLabel = new Label(text);
        textLabel.setFont(getFont(16, FontWeight.BOLD));
        textLabel.setTextFill(Color.web(FONT_COLOR_DARK));
        
        Label subtextLabel = new Label(subtext);
        subtextLabel.setFont(getFont(12, FontWeight.NORMAL));
        subtextLabel.setTextFill(Color.web(FONT_COLOR_DARK));
        subtextLabel.setOpacity(0.7);

        buttonVBox.getChildren().addAll(textLabel, subtextLabel);
        return buttonVBox;
    }

    private Node createLiveAlertsModule() {
        VBox card = createDashboardCard("Live Government Alerts");
        ListView<Alert> alertsList = new ListView<>(liveAlertsList);
        alertsList.setPlaceholder(new Label("No active government alerts."));

        alertsList.setCellFactory(param -> new ListCell<Alert>() {
            @Override
            protected void updateItem(Alert item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    Text title = new Text(item.getTitle());
                    title.setFont(CitizenDashboard.getFont(14, FontWeight.BOLD));

                    Text message = new Text(item.getMessage());
                    message.setFont(CitizenDashboard.getFont(14, FontWeight.NORMAL));
                    message.setWrappingWidth(param.getWidth() - 30);

                    if ("High".equalsIgnoreCase(item.getPriority())) {
                        title.setFill(Color.web(ALERT_COLOR));
                    } else {
                        title.setFill(Color.web(FONT_COLOR_DARK));
                    }
                    
                    VBox alertContent = new VBox(5, title, message);
                    alertContent.setPadding(new Insets(5));
                    setGraphic(alertContent);
                }
            }
        });
        VBox.setVgrow(alertsList, Priority.ALWAYS);
        card.getChildren().add(alertsList);
        return card;
    }

    private Node createStatisticsModule() {
        VBox card = createDashboardCard("Key Statistics");
        GridPane grid = new GridPane();
        grid.setHgap(20);
        grid.setVgap(20);
        grid.setPadding(new Insets(10, 0, 0, 0));
        
        grid.add(createStatItem("12K", "People Rescued", PRIMARY_BLUE), 0, 0);
        grid.add(createStatItem("56K", "Requests Handled", PRIMARY_BLUE), 1, 0);
        grid.add(createStatItem("343", "Active Volunteers", SECONDARY_ACCENT_COLOR), 0, 1);
        grid.add(createStatItem("39K", "Resources Delivered", SECONDARY_ACCENT_COLOR), 1, 1);

        card.getChildren().add(grid);
        return card;
    }

    private Node createStatItem(String number, String label, String color) {
        VBox item = new VBox(-2);
        item.setAlignment(Pos.CENTER_LEFT);
        
        Label numberLabel = new Label(number);
        numberLabel.setFont(getFont(36, FontWeight.BOLD));
        numberLabel.setTextFill(Color.web(color));
        
        Label textLabel = new Label(label);
        textLabel.setFont(getFont(14, FontWeight.NORMAL));
        textLabel.setTextFill(Color.web(FONT_COLOR_DARK));
        
        item.getChildren().addAll(numberLabel, textLabel);
        return item;
    }

    private Node createFeaturedNewsModule() {
        VBox card = createDashboardCard("Featured News");
        VBox newsList = new VBox(15);
        newsList.setPadding(new Insets(10, 0, 0, 0));

        newsList.getChildren().add(createNewsItem("Community Rallies to Provide Aid", "Volunteers from across the city..."));
        newsList.getChildren().add(createNewsItem("New Early Warning System Deployed", "The new city-wide siren system was..."));
        newsList.getChildren().add(createNewsItem("How to Prepare an Emergency Kit", "Experts advise having a kit with essentials..."));

        card.getChildren().add(newsList);
        return card;
    }
    
    private Node createNewsItem(String title, String summary) {
        VBox item = new VBox(5);
        Label titleLabel = new Label(title);
        titleLabel.setFont(getFont(16, FontWeight.BOLD));
        titleLabel.setTextFill(Color.web(PRIMARY_BLUE));
        
        Label summaryLabel = new Label(summary);
        summaryLabel.setFont(getFont(14, FontWeight.NORMAL));
        summaryLabel.setTextFill(Color.web(FONT_COLOR_DARK));
        summaryLabel.setWrapText(true);
        
        item.getChildren().addAll(titleLabel, summaryLabel);
        item.setStyle("-fx-border-color: " + BORDER_COLOR + " transparent transparent transparent; -fx-border-width: 1; -fx-padding: 10 0 0 0;");
        
        if (item.getParent() instanceof VBox && ((VBox) item.getParent()).getChildren().isEmpty()) {
            item.setStyle("-fx-border-width: 0; -fx-padding: 0;");
        }

        return item;
    }
    
    private VBox createDashboardCard(String title) {
        VBox card = new VBox(10);
        card.setPadding(new Insets(20));
        card.setStyle("-fx-background-color: " + CARD_BACKGROUND_COLOR + "; " +
                      "-fx-background-radius: 12; " +
                      "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.05), 10, 0, 0, 4);");
        VBox.setVgrow(card, Priority.ALWAYS);
        
        Label titleLabel = new Label(title);
        titleLabel.setFont(getFont(20, FontWeight.BOLD));
        titleLabel.setTextFill(Color.web(FONT_COLOR_DARK));
        
        card.getChildren().add(titleLabel);
        return card;
    }
}
