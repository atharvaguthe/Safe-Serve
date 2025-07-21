package com.thinkspark.view;

import com.thinkspark.dao.AlertDao;
import com.thinkspark.dao.TaskDao;
import com.thinkspark.model.Alert;
import com.thinkspark.model.DataManager;
import com.thinkspark.model.Task;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
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

public class VolunteerDashboard extends Application {

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
    private ListView<Task> tasksListView;
    private final String currentVolunteerName = "Aarav Sharma"; // This would be dynamic in a real app

    private AlertDao alertDao;
    private TaskDao taskDao;
    private ObservableList<Alert> liveAlertsList;


    private static Font getFont(double size, FontWeight weight) {
        return Font.font("System", weight, size);
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;

        // --- Initialize DAOs and start listening for data ---
        alertDao = new AlertDao();
        taskDao = new TaskDao();
        liveAlertsList = FXCollections.observableArrayList();
        alertDao.listenForAlerts(liveAlertsList);


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

        dashboardScene = new Scene(scrollPane, 1366, 900);
        primaryStage.setTitle("Volunteer Dashboard | " + currentVolunteerName);
        primaryStage.setScene(dashboardScene);

        // Listen for changes in assigned tasks from Firebase
        taskDao.listenForTasks(DataManager.getAssignedTasks());
        DataManager.getAssignedTasks().addListener((ListChangeListener<Task>) c -> updateAssignedTasks());
        updateAssignedTasks();

        primaryStage.show();
    }

    private GridPane createMainGrid(){
        GridPane mainGrid = new GridPane();
        mainGrid.setHgap(40);
        mainGrid.setVgap(40);
        mainGrid.setAlignment(Pos.CENTER);

        mainGrid.add(createQuickAccessModule(), 0, 0);
        mainGrid.add(createAssignedTasksModule(), 1, 0);
        mainGrid.add(createTeamStatsModule(), 0, 1);
        mainGrid.add(createLiveAlertsModule(), 1, 1);


        return mainGrid;
    }

    private Node createHeader() {
        HBox header = new HBox();
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(10, 0, 20, 0));

        Label logo = new Label("🤝 Volunteer Helper");
        logo.setFont(getFont(32, FontWeight.BOLD));
        logo.setTextFill(Color.web(PRIMARY_BLUE));

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox navLinks = new HBox(40);
        navLinks.setAlignment(Pos.CENTER);

        Label dashboardLink = new Label("Dashboard");
        dashboardLink.setFont(getFont(16, FontWeight.MEDIUM));
        dashboardLink.setOnMouseClicked(e -> primaryStage.setScene(dashboardScene));

        // "My Tasks" link has been removed from here
        navLinks.getChildren().add(dashboardLink);

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
        overviewBox.setStyle("-fx-background-color: " + CARD_BACKGROUND_COLOR + "; -fx-background-radius: 16; -fx-border-color: " + BORDER_COLOR + "; -fx-border-width: 1;");

        Label title = new Label("Welcome, " + currentVolunteerName + "!");
        title.setFont(getFont(36, FontWeight.BOLD));
        title.setTextFill(Color.web(FONT_COLOR_DARK));

        Label subtitle = new Label("Thank you for your dedication. Here are your tasks and team progress.");
        subtitle.setFont(getFont(18, FontWeight.NORMAL));
        subtitle.setTextFill(Color.web(FONT_COLOR_MEDIUM));
        subtitle.setWrapText(true);

        overviewBox.getChildren().addAll(title, subtitle);
        return overviewBox;
    }

    private Node createQuickAccessModule() {
        VBox card = createDashboardCard("Quick Actions");
        GridPane buttonGrid = new GridPane();
        buttonGrid.setHgap(20);
        buttonGrid.setVgap(20);
        buttonGrid.setPadding(new Insets(15, 0, 0, 0));

        Button reportButton = createQuickAccessButton("🚨 Report Incident", true);
        reportButton.setOnAction(e -> new ReportDisasterView(primaryStage, dashboardScene, currentVolunteerName).start(primaryStage));

        Button requestHelpButton = createQuickAccessButton("🤝 Request Help", false);
        requestHelpButton.setOnAction(e -> new AssistanceView(primaryStage, dashboardScene, currentVolunteerName).start(primaryStage));

        Button logHoursButton = createQuickAccessButton("📋 Log Hours", false);
        logHoursButton.setOnAction(e -> new LogHoursView(primaryStage, dashboardScene).start(primaryStage));

        buttonGrid.add(reportButton, 0, 0);
        buttonGrid.add(requestHelpButton, 1, 0);
        buttonGrid.add(createQuickAccessButton("🗺️ View Map", false), 0, 1);
        buttonGrid.add(logHoursButton, 1, 1);

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

    private Node createAssignedTasksModule() {
        VBox card = createDashboardCard("Your Assigned Tasks");
        tasksListView = new ListView<>();
        tasksListView.setPlaceholder(new Label("You have no assigned tasks."));
        tasksListView.setCellFactory(param -> new ListCell<Task>() {
            @Override
            protected void updateItem(Task item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getDescription());
                    setFont(VolunteerDashboard.getFont(15, FontWeight.MEDIUM));
                    setTextFill(Color.web(FONT_COLOR_DARK));
                }
            }
        });

        VBox.setVgrow(tasksListView, Priority.ALWAYS);
        card.getChildren().add(tasksListView);
        return card;
    }

    private void updateAssignedTasks() {
        tasksListView.getItems().clear();
        for (Task task : DataManager.getAssignedTasks()) {
            if (currentVolunteerName.equals(task.getAssignedTo())) {
                tasksListView.getItems().add(task);
            }
        }
    }

    private Node createTeamStatsModule() {
        VBox card = createDashboardCard("Your Team's Contribution");
        HBox statsBox = new HBox();
        statsBox.setAlignment(Pos.CENTER);
        statsBox.setSpacing(40);
        statsBox.setPadding(new Insets(15, 0, 0, 0));
        HBox.setHgrow(statsBox, Priority.ALWAYS);

        String[][] statsData = {
            { "48", "Hours Logged (You)" },
            { "212", "Hours Logged (Team)" },
            { "15", "Tasks Completed" },
            { "120", "People Assisted" }
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
                    title.setFont(VolunteerDashboard.getFont(14, FontWeight.BOLD));

                    Text message = new Text(item.getMessage());
                    message.setFont(VolunteerDashboard.getFont(14, FontWeight.NORMAL));
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