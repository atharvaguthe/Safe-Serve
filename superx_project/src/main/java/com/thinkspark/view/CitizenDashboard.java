package com.thinkspark.view;

import com.thinkspark.dao.AlertDao;
import com.thinkspark.dao.NewsDao;
import com.thinkspark.model.Alert;
import com.thinkspark.model.NewsArticle;
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

        VBox rootLayout = new VBox(70); // Increased spacing even more
        rootLayout.setPadding(new Insets(50, 100, 100, 100)); // Increased padding even more
        rootLayout.setStyle("-fx-background-color: " + BACKGROUND_COLOR + ";");
        rootLayout.setAlignment(Pos.TOP_CENTER);

        rootLayout.getChildren().addAll(createHeader(), createHeroSection(), createMainGrid());

        ScrollPane scrollPane = new ScrollPane(rootLayout);
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        dashboardScene = new Scene(scrollPane,1920,1080);
        primaryStage.setTitle("Safe Serve");
        primaryStage.setScene(dashboardScene);
        primaryStage.setMaximized(true);
        primaryStage.setResizable(true);
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/logo.png")));
        primaryStage.show();
    }
    
    private GridPane createMainGrid() {
        GridPane mainGrid = new GridPane();
        mainGrid.setHgap(60); // Increased gap even more
        mainGrid.setVgap(60); // Increased gap even more
        mainGrid.setAlignment(Pos.CENTER);

        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPercentWidth(50);
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setPercentWidth(50);
        mainGrid.getColumnConstraints().addAll(col1, col2);

        mainGrid.add(createQuickAccessModule(), 0, 0);
        mainGrid.add(createLiveAlertsModule(), 1, 0);
        mainGrid.add(createStatisticsModule(), 0, 1);
        mainGrid.add(createNewsFeedModule(), 1, 1);
        
        return mainGrid;
    }

    private Node createHeader() {
    HBox header = new HBox();
    header.setAlignment(Pos.CENTER_LEFT);
    header.setPadding(new Insets(30, 0, 40, 0)); // Increased padding even more

    // Create an ImageView for the logo
    ImageView logoView = new ImageView(new Image(getClass().getResourceAsStream("/logo.png")));
    logoView.setFitHeight(70); // Increased size even more
    logoView.setPreserveRatio(true);

    // Create the text label
    Label logoLabel = new Label("Safe Serve");
    logoLabel.setFont(getFont(48, FontWeight.BOLD)); // Increased font size even more
    logoLabel.setTextFill(Color.web(PRIMARY_BLUE));

    // Combine image and text into an HBox
    HBox logoBox = new HBox(25, logoView, logoLabel); // Increased spacing even more
    logoBox.setAlignment(Pos.CENTER_LEFT);

    Region spacer = new Region();
    HBox.setHgrow(spacer, Priority.ALWAYS);

    HBox navLinks = new HBox(60); // Increased spacing even more
    navLinks.setAlignment(Pos.CENTER);
    String[] navItems = { "Home", "Assistance", "Resources", "About Us" };
    for (String item : navItems) {
        Label link = new Label(item);
        link.setFont(getFont(22, FontWeight.MEDIUM)); // Increased font size even more
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
    MenuItem profileItem = new MenuItem("Profile");
    MenuItem logoutItem = new MenuItem("Log Out");
    profileButton.getItems().addAll(profileItem, logoutItem);

    profileItem.setOnAction(e -> new ProfileView(primaryStage, dashboardScene, this.userName).start(primaryStage));
    logoutItem.setOnAction(e -> {
        // Reuse the existing primary stage to go back to DisasterHelperUI
        new DisasterHelperUI().start(primaryStage);
    });

    profileButton.setFont(getFont(22, FontWeight.BOLD)); // Increased font size even more
    profileButton.setStyle(
        "-fx-background-color: " + PRIMARY_BLUE + "; " +
        "-fx-text-fill: " + FONT_COLOR_LIGHT + "; " +
        "-fx-background-radius: 50em; -fx-padding: 0;"
    );
    profileButton.setMinSize(60, 60); // Increased size even more
    profileButton.setMaxSize(60, 60); // Increased size even more
    profileButton.setAlignment(Pos.CENTER);

    // Add a margin to create space between navLinks and profileButton
    HBox.setMargin(navLinks, new Insets(0, 50, 0, 0)); // Increased margin even more

    header.getChildren().addAll(logoBox, spacer, navLinks, profileButton);
    return header;
    }

    private Node createHeroSection() {
        StackPane heroPane = new StackPane();
        heroPane.setPadding(new Insets(70)); // Increased padding even more
        heroPane.setStyle("-fx-background-color: linear-gradient(to right, " + PRIMARY_BLUE + ", #007ACC); -fx-background-radius: 20;"); // Increased radius even more

        VBox content = new VBox(30); // Increased spacing even more
        content.setAlignment(Pos.CENTER_LEFT);

        Label welcome = new Label("Welcome back, " + this.userName + "!");
        welcome.setFont(getFont(40, FontWeight.BOLD)); // Increased font size even more
        welcome.setTextFill(Color.web(FONT_COLOR_LIGHT));

        Label subtitle = new Label("Your central hub for disaster assistance, resources, and real-time alerts.");
        subtitle.setFont(getFont(22, FontWeight.NORMAL)); // Increased font size even more
        subtitle.setTextFill(Color.web(FONT_COLOR_LIGHT));
        subtitle.setOpacity(0.9);
        subtitle.setWrapText(true);

        Button reportButton = new Button("🚨 Report a Disaster");
        reportButton.setFont(getFont(24, FontWeight.BOLD)); // Increased font size even more
        reportButton.setTextFill(Color.web(FONT_COLOR_LIGHT));
        reportButton.setStyle("-fx-background-color: " + ALERT_COLOR + "; -fx-background-radius: 15; -fx-cursor: hand;"); // Increased radius even more
        reportButton.setPadding(new Insets(20, 35, 20, 35)); // Increased padding even more
        VBox.setMargin(reportButton, new Insets(25, 0, 0, 0)); // Increased margin even more

        // UPDATED to pass username
        reportButton.setOnAction(e -> new ReportDisasterView(primaryStage, dashboardScene, this.userName).start(primaryStage));

        content.getChildren().addAll(welcome, subtitle, reportButton);
        heroPane.getChildren().add(content);
        return heroPane;
    }

    private Node createQuickAccessModule() {
        VBox card = createDashboardCard("Quick Access");
        HBox buttonBox = new HBox(25); // Increased spacing even more
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setPadding(new Insets(20, 0, 0, 0)); // Increased padding even more

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
        VBox buttonVBox = new VBox(12); // Increased spacing even more
        buttonVBox.setAlignment(Pos.CENTER);
        buttonVBox.setPadding(new Insets(35, 25, 35, 25)); // Increased padding even more
        buttonVBox.setStyle("-fx-background-color: " + BACKGROUND_COLOR + "; -fx-background-radius: 15; -fx-border-color: " + BORDER_COLOR + "; -fx-border-radius: 15; -fx-cursor: hand;"); // Increased radius even more
        HBox.setHgrow(buttonVBox, Priority.ALWAYS);

        buttonVBox.setOnMouseEntered(e -> buttonVBox.setStyle("-fx-background-color: #E2E8F0; -fx-background-radius: 15; -fx-border-color: " + BORDER_COLOR + "; -fx-border-radius: 15; -fx-cursor: hand;")); // Increased radius even more
        buttonVBox.setOnMouseExited(e -> buttonVBox.setStyle("-fx-background-color: " + BACKGROUND_COLOR + "; -fx-background-radius: 15; -fx-border-color: " + BORDER_COLOR + "; -fx-border-radius: 15; -fx-cursor: hand;")); // Increased radius even more
        
        Label textLabel = new Label(text);
        textLabel.setFont(getFont(22, FontWeight.BOLD)); // Increased font size even more
        textLabel.setTextFill(Color.web(FONT_COLOR_DARK));
        
        Label subtextLabel = new Label(subtext);
        subtextLabel.setFont(getFont(18, FontWeight.NORMAL)); // Increased font size even more
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
                    title.setFont(CitizenDashboard.getFont(20, FontWeight.BOLD)); // Increased font size even more

                    Text message = new Text(item.getMessage());
                    message.setFont(CitizenDashboard.getFont(20, FontWeight.NORMAL)); // Increased font size even more
                    message.setWrappingWidth(param.getWidth() - 60); // Adjusted wrapping width even more

                    if ("High".equalsIgnoreCase(item.getPriority())) {
                        title.setFill(Color.web(ALERT_COLOR));
                    } else {
                        title.setFill(Color.web(FONT_COLOR_DARK));
                    }
                    
                    VBox alertContent = new VBox(12, title, message); // Increased spacing even more
                    alertContent.setPadding(new Insets(12)); // Increased padding even more
                    setGraphic(alertContent);
                }
            }
        });
        alertsList.setPrefHeight(600); // Increased preferred height
        VBox.setVgrow(alertsList, Priority.ALWAYS);
        card.getChildren().add(alertsList);
        return card;
    }

    private Node createStatisticsModule() {
        VBox card = createDashboardCard("Key Statistics");
        GridPane grid = new GridPane();
        grid.setHgap(45); // Increased gap even more
        grid.setVgap(45); // Increased gap even more
        grid.setPadding(new Insets(25, 0, 0, 0)); // Increased padding even more
        
        grid.add(createStatItem("12K", "People Rescued", PRIMARY_BLUE), 0, 0);
        grid.add(createStatItem("56K", "Requests Handled", PRIMARY_BLUE), 1, 0);
        grid.add(createStatItem("343", "Active Volunteers", SECONDARY_ACCENT_COLOR), 0, 1);
        grid.add(createStatItem("39K", "Resources Delivered", SECONDARY_ACCENT_COLOR), 1, 1);

        card.getChildren().add(grid);
        return card;
    }

    private Node createStatItem(String number, String label, String color) {
        VBox item = new VBox(-5); // Adjusted spacing even more
        item.setAlignment(Pos.CENTER_LEFT);
        
        Label numberLabel = new Label(number);
        numberLabel.setFont(getFont(54, FontWeight.BOLD)); // Increased font size even more
        numberLabel.setTextFill(Color.web(color));
        
        Label textLabel = new Label(label);
        textLabel.setFont(getFont(20, FontWeight.NORMAL)); // Increased font size even more
        textLabel.setTextFill(Color.web(FONT_COLOR_DARK));
        
        item.getChildren().addAll(numberLabel, textLabel);
        return item;
    }

    private Node createNewsFeedModule() {
        VBox card = createDashboardCard("Latest News on Disasters");
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
                    VBox vbox = new VBox(15); // Increased spacing between title and description even more
                    vbox.setPadding(new Insets(15)); // Added padding to each news item even more
                    vbox.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 12; -fx-border-color: " + BORDER_COLOR + "; -fx-border-width: 1; -fx-padding: 15;"); // Attractive styling for each item, increased radius

                    Label title = new Label(item.getTitle());
                    title.setWrapText(true);
                    title.setFont(CitizenDashboard.getFont(24, FontWeight.BOLD)); // Increased title font size even more
                    title.setTextFill(Color.web(PRIMARY_BLUE)); // Changed title color to primary blue

                    Label description = new Label(item.getDescription());
                    description.setWrapText(true);
                    description.setFont(CitizenDashboard.getFont(20, FontWeight.NORMAL)); // Increased description font size even more
                    description.setTextFill(Color.web(FONT_COLOR_DARK)); // Consistent description color

                    vbox.getChildren().addAll(title, description);
                    setGraphic(vbox);
                }
            }
        });

        // Apply styles to ListView itself to remove default background and borders
        listView.setStyle("-fx-background-color: transparent; -fx-control-inner-background: transparent;");
        listView.setFixedCellSize(220); // Increased fixed cell size even more
        listView.setPrefHeight(660); // Increased preferred height even more

        ScrollPane scrollPane = new ScrollPane(listView);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefHeight(660); // Sync preferred height with ListView
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent;"); // Ensure scroll pane is transparent

        card.getChildren().add(scrollPane);
        return card;
    }
    
    private VBox createDashboardCard(String title) {
        VBox card = new VBox(25); // Increased spacing even more
        card.setPadding(new Insets(35)); // Increased padding even more
        card.setStyle("-fx-background-color: " + CARD_BACKGROUND_COLOR + "; " +
                      "-fx-background-radius: 20; " + // Increased radius even more
                      "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.05), 20, 0, 0, 8);"); // Increased shadow even more
        VBox.setVgrow(card, Priority.ALWAYS);
        
        Label titleLabel = new Label(title);
        titleLabel.setFont(getFont(32, FontWeight.BOLD)); // Increased font size even more
        titleLabel.setTextFill(Color.web(FONT_COLOR_DARK));
        
        card.getChildren().add(titleLabel);
        return card;
    }
}