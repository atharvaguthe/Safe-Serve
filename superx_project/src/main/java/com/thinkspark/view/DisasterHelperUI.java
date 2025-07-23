package com.thinkspark.view;

import com.thinkspark.dao.NewsDao;
import com.thinkspark.model.NewsArticle;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class DisasterHelperUI extends Application {

    // --- Style Definitions ---
    private static final String BACKGROUND_COLOR = "#FFFFFF";
    private static final String PRIMARY_BLUE = "#005A9C";
    private static final String SECONDARY_BLUE = "#007BFF";
    private static final String FONT_COLOR_DARK = "#2D3748";
    private static final String FONT_COLOR_LIGHT = "#FFFFFF";
    private static final String CARD_BACKGROUND_COLOR = "#F4F7FC";
    private static final String BORDER_COLOR = "#E2E8F0";
    private static final String ALERT_BACKGROUND_COLOR = "#FFFBEB";
    private static final String ALERT_TEXT_COLOR = "#B45309";

    // --- Font Definitions ---
    private static Font getFont(double size, FontWeight weight) {
        return Font.font("System", weight, size);
    }

    @Override
    public void start(Stage primaryStage) {
        // --- Root Layout ---
        VBox rootLayout = new VBox(40);
        rootLayout.setPadding(new Insets(20, 50, 50, 50));
        rootLayout.setStyle("-fx-background-color: " + BACKGROUND_COLOR + ";");
        rootLayout.setAlignment(Pos.TOP_CENTER);

        // --- Scene and Stage Setup ---
        Scene scene = new Scene(new ScrollPane(rootLayout),1920,1080);
        primaryStage.setTitle("Safe Serve");
        primaryStage.setMaximized(true);
        primaryStage.setResizable(true);
        primaryStage.setScene(scene);

        // --- Header / Navigation Bar ---
        Node header = createHeader(primaryStage, scene);

        // --- Hero Section ---
        Node heroSection = createHeroSection();

        // --- Main Content Grid ---
        GridPane mainGrid = new GridPane();
        mainGrid.setHgap(40);
        mainGrid.setVgap(40);
        mainGrid.setAlignment(Pos.CENTER);

        // Configure column constraints for a balanced layout
        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPercentWidth(65);
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setPercentWidth(35);
        mainGrid.getColumnConstraints().addAll(col1, col2);

        // --- Left Column Content ---
        VBox leftColumn = new VBox(40);
        leftColumn.getChildren().addAll(
            createQuickAccessModule(primaryStage, scene), // Pass stage and scene for navigation
            createNewsFeedModule()
        );

        // --- Right Column Content ---
        VBox rightColumn = new VBox(40);
        rightColumn.getChildren().addAll(
            createLiveAlertsModule(),
            createStatisticsModule()
        );

        mainGrid.add(leftColumn, 0, 0);
        mainGrid.add(rightColumn, 1, 0);

        // --- Assemble the Main Layout ---
        rootLayout.getChildren().addAll(header, heroSection, mainGrid);

        // --- ScrollPane for Responsiveness ---
        ScrollPane scrollPane = (ScrollPane) scene.getRoot();
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setStyle("-fx-background-color: " + BACKGROUND_COLOR + "; -fx-background: " + BACKGROUND_COLOR + ";");
        scrollPane.getStyleClass().add("edge-to-edge");

        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/logo.png")));
        primaryStage.show();
    }

    private Node createHeader(Stage stage, Scene scene) {
        HBox header = new HBox();
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(10, 0, 20, 0));

        // Create an ImageView for the logo
        ImageView logoView = new ImageView(new Image(getClass().getResourceAsStream("/logo.png")));
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

        // Navigation Links
        HBox navLinks = new HBox(40);
        navLinks.setAlignment(Pos.CENTER);
        
        Button homeLink = createNavLink("Home");
        homeLink.setOnAction(e -> this.start(stage));

        Button assistanceLink = createNavLink("Assistance");
        assistanceLink.setOnAction(e -> new AssistanceView(stage, scene, "Citizen").start(stage));

        Button resourcesLink = createNavLink("Resources");
        resourcesLink.setOnAction(e -> new ResourcesView(stage, scene).start(stage));

        Button aboutUsLink = createNavLink("About Us");
        aboutUsLink.setOnAction(e -> new AboutUsView(stage, scene).start(stage));

        navLinks.getChildren().addAll(homeLink, assistanceLink, resourcesLink, aboutUsLink);

        // Auth Buttons
        HBox authButtons = new HBox(15);
        authButtons.setAlignment(Pos.CENTER);

        Button loginBtn = new Button("Log In");
        loginBtn.setFont(getFont(15, FontWeight.BOLD));
        loginBtn.setTextFill(Color.web(PRIMARY_BLUE));
        loginBtn.setStyle("-fx-background-color: transparent; -fx-border-color: " + BORDER_COLOR + "; -fx-border-width: 2; -fx-background-radius: 20; -fx-border-radius: 20; -fx-padding: 8 20; -fx-cursor: hand;");

        Button signupBtn = new Button("Sign Up");
        signupBtn.setFont(getFont(15, FontWeight.BOLD));
        signupBtn.setTextFill(Color.web(FONT_COLOR_LIGHT));
        signupBtn.setStyle("-fx-background-color: " + SECONDARY_BLUE + "; -fx-background-radius: 20; -fx-padding: 8 20; -fx-cursor: hand;");
        
        loginBtn.setOnAction(e -> {
            // Reuse the existing stage
            new LoginUI().start(stage);
        });

        signupBtn.setOnAction(e -> {
            // Reuse the existing stage
            new SignUpForm().start(stage);
        });
        
        authButtons.getChildren().addAll(loginBtn, signupBtn);
        HBox.setMargin(navLinks, new Insets(0, 50, 0, 0));

        header.getChildren().addAll(logoBox, spacer, navLinks, authButtons);
        return header;
    }
    
    private Button createNavLink(String text) {
        Button link = new Button(text);
        link.setFont(getFont(16, FontWeight.MEDIUM));
        link.setTextFill(Color.web(FONT_COLOR_DARK));
        link.setStyle("-fx-background-color: transparent; -fx-padding: 0; -fx-cursor: hand;");
        link.setOnMouseEntered(e -> link.setUnderline(true));
        link.setOnMouseExited(e -> link.setUnderline(false));
        return link;
    }

    private Node createHeroSection() {
        VBox heroBox = new VBox(20);
        heroBox.setAlignment(Pos.CENTER);
        heroBox.setPadding(new Insets(60, 40, 60, 40));
        heroBox.setStyle("-fx-background-color: " + CARD_BACKGROUND_COLOR + "; -fx-background-radius: 16;");

        Label title = new Label("We Are Here To Help");
        title.setFont(getFont(48, FontWeight.BOLD));
        title.setTextFill(Color.web(FONT_COLOR_DARK));

        Label subtitle = new Label("Your trusted partner in navigating emergencies. Get access to critical information, resources, and support when you need it most.");
        subtitle.setFont(getFont(18, FontWeight.NORMAL));
        subtitle.setTextFill(Color.web(FONT_COLOR_DARK));
        subtitle.setOpacity(0.8);
        subtitle.setWrapText(true);
        subtitle.setMaxWidth(800);
        subtitle.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);

        heroBox.getChildren().addAll(title, subtitle);
        return heroBox;
    }

    private Node createQuickAccessModule(Stage stage, Scene scene) {
        VBox card = createDashboardCard("Quick Access");
        GridPane buttonGrid = new GridPane();
        buttonGrid.setHgap(20);
        buttonGrid.setVgap(20);
        buttonGrid.setPadding(new Insets(15, 0, 0, 0));
        
        Button disasterInfoBtn = createQuickAccessButton("Disaster Info Type");
        disasterInfoBtn.setOnAction(e -> new DisasterInfoTypeView(stage, scene).start(stage));

        Button safeZoneBtn = createQuickAccessButton("Nearest Safe Zone");
        safeZoneBtn.setOnAction(e -> new NearestSafeZoneView(stage, scene).start(stage));

        Button reliefResourcesBtn = createQuickAccessButton("Relief Resources");
        reliefResourcesBtn.setOnAction(e -> new ReliefResourcesView(stage, scene).start(stage));
        
        Button donateBtn = createQuickAccessButton("Donate Now");
        donateBtn.setOnAction(e -> new DonationsView(stage, scene).start(stage));

        buttonGrid.add(disasterInfoBtn, 0, 0);
        buttonGrid.add(safeZoneBtn, 1, 0);
        buttonGrid.add(reliefResourcesBtn, 0, 1);
        buttonGrid.add(donateBtn, 1, 1);

        card.getChildren().add(buttonGrid);
        return card;
    }
    
    private Button createQuickAccessButton(String text) {
        Button button = new Button(text);
        button.setFont(getFont(16, FontWeight.BOLD));
        button.setTextFill(Color.web(FONT_COLOR_DARK));
        button.setStyle("-fx-background-color: " + BACKGROUND_COLOR + "; -fx-background-radius: 10; -fx-border-color: " + BORDER_COLOR + "; -fx-border-radius: 10; -fx-cursor: hand;");
        button.setPrefSize(300, 80);
        GridPane.setHgrow(button, Priority.ALWAYS);
        GridPane.setVgrow(button, Priority.ALWAYS);
        return button;
    }

    private Node createLiveAlertsModule() {
        VBox card = createDashboardCard("Live Alerts");
        card.setStyle("-fx-background-color: " + ALERT_BACKGROUND_COLOR + "; -fx-background-radius: 12; -fx-padding: 20;");
        
        Label title = (Label) card.getChildren().get(0);
        title.setTextFill(Color.web(ALERT_TEXT_COLOR));
        
        VBox alertsContainer = new VBox(15);
        alertsContainer.setPadding(new Insets(10, 0, 0, 0));
        
        alertsContainer.getChildren().add(createAlertItem("High wind advisory in effect until 8 PM."));
        alertsContainer.getChildren().add(createAlertItem("Road closure on Main St due to flooding."));
        alertsContainer.getChildren().add(createAlertItem("Community shelter open at the civic center."));
        
        card.getChildren().add(alertsContainer);
        return card;
    }
    
    private Node createAlertItem(String text) {
        Label alertLabel = new Label("• " + text);
        alertLabel.setFont(getFont(15, FontWeight.MEDIUM));
        alertLabel.setTextFill(Color.web(ALERT_TEXT_COLOR));
        alertLabel.setWrapText(true);
        return alertLabel;
    }

    private Node createStatisticsModule() {
        VBox card = createDashboardCard("Statistics");
        GridPane grid = new GridPane();
        grid.setHgap(20);
        grid.setVgap(20);
        grid.setPadding(new Insets(15, 0, 0, 0));
        
        grid.add(createStatItem("12K", "People Rescued"), 0, 0);
        grid.add(createStatItem("56K", "Requests Handled"), 1, 0);
        grid.add(createStatItem("343", "Active Volunteers"), 0, 1);
        grid.add(createStatItem("39K", "Resources Delivered"), 1, 1);

        card.getChildren().add(grid);
        return card;
    }

    private Node createStatItem(String number, String label) {
        VBox item = new VBox(-2);
        item.setAlignment(Pos.CENTER_LEFT);
        
        Label numberLabel = new Label(number);
        numberLabel.setFont(getFont(34, FontWeight.BOLD));
        numberLabel.setTextFill(Color.web(PRIMARY_BLUE));
        
        Label textLabel = new Label(label);
        textLabel.setFont(getFont(14, FontWeight.NORMAL));
        textLabel.setTextFill(Color.web(FONT_COLOR_DARK));
        
        item.getChildren().addAll(numberLabel, textLabel);
        return item;
    }

    private Node createNewsFeedModule() {
        VBox card = createDashboardCard("Latest News");
        NewsDao newsDao = new NewsDao();
        ObservableList<NewsArticle> newsList = newsDao.getNews();

        ListView<NewsArticle> listView = new ListView<>(newsList);
        listView.setCellFactory(param -> new ListCell<NewsArticle>() {
            @Override
            protected void updateItem(NewsArticle item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    VBox vbox = new VBox(5);
                    Label title = new Label(item.getTitle());
                    title.setWrapText(true);
                    title.setStyle("-fx-font-weight: bold;");
                    Label description = new Label(item.getDescription());
                    description.setWrapText(true);
                    vbox.getChildren().addAll(title, description);
                    setGraphic(vbox);
                }
            }
        });

        ScrollPane scrollPane = new ScrollPane(listView);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefHeight(300); // Set a preferred height for independent scrolling

        card.getChildren().add(scrollPane);
        return card;
    }
    
    private VBox createDashboardCard(String title) {
        VBox card = new VBox(10);
        card.setPadding(new Insets(25));
        card.setStyle("-fx-background-color: " + CARD_BACKGROUND_COLOR + "; " +
                      "-fx-background-radius: 12;");
        VBox.setVgrow(card, Priority.ALWAYS);
        
        Label titleLabel = new Label(title);
        titleLabel.setFont(getFont(24, FontWeight.BOLD));
        titleLabel.setTextFill(Color.web(FONT_COLOR_DARK));
        
        card.getChildren().add(titleLabel);
        return card;
    }
}