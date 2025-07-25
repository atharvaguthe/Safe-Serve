package com.thinkspark.view;

import com.thinkspark.dao.TaskDao;
import com.thinkspark.dao.VolunteerDao;
import com.thinkspark.model.Task;
import com.thinkspark.model.Volunteer;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
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
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.util.Optional;

public class ManageVolunteersView extends Application {

    // --- Style Definitions ---
    private static final String BACKGROUND_COLOR = "#F4F7FC";
    private static final String PRIMARY_BLUE = "#005A9C";
    private static final String FONT_COLOR_DARK = "#2D3748";
    private static final String CARD_BACKGROUND_COLOR = "#FFFFFF";
    private static final String BORDER_COLOR = "#E2E8F0";
    private static final String STATUS_ACTIVE_COLOR = "#38A169"; // Green
    private static final String STATUS_INACTIVE_COLOR = "#A0AEC0"; // Gray

    private final Scene previousScene;
    private final Stage primaryStage;

    private ObservableList<VolunteerItem> volunteerItems;
    private final TaskDao taskDao;
    private final VolunteerDao volunteerDao;

    private static Font getFont(double size, FontWeight weight) {
        return Font.font("System", weight, size);
    }

    private static class VolunteerItem {
        private final String name;
        private final String status;
        private final String assignedTask;

        public VolunteerItem(String name, String status, String assignedTask) {
            this.name = name;
            this.status = status;
            this.assignedTask = assignedTask;
        }
    }

    private static class VolunteerCell extends ListCell<VolunteerItem> {
        private final HBox content = new HBox();
        private final VBox details = new VBox(10); // Increased spacing
        private final Label nameLabel = new Label();
        private final Label taskLabel = new Label();
        private final Label statusLabel = new Label();
        private final Region spacer = new Region();

        public VolunteerCell() {
            super();
            nameLabel.setFont(ManageVolunteersView.getFont(20, FontWeight.BOLD)); // Increased font size
            taskLabel.setFont(ManageVolunteersView.getFont(18, FontWeight.NORMAL)); // Increased font size
            taskLabel.setOpacity(0.8);
            statusLabel.setFont(ManageVolunteersView.getFont(16, FontWeight.BOLD)); // Increased font size
            statusLabel.setPadding(new Insets(8, 12, 8, 12)); // Increased padding
            statusLabel.setTextFill(Color.WHITE);

            details.getChildren().addAll(nameLabel, taskLabel);
            HBox.setHgrow(spacer, Priority.ALWAYS);
            content.setAlignment(Pos.CENTER);
            content.setSpacing(25); // Increased spacing
            content.getChildren().addAll(details, spacer, statusLabel);
            content.setPadding(new Insets(25)); // Increased padding
            content.setStyle("-fx-border-color: transparent transparent " + BORDER_COLOR + " transparent; -fx-border-width: 1;");
        }

        @Override
        protected void updateItem(VolunteerItem item, boolean empty) {
            super.updateItem(item, empty);
            if (empty || item == null) {
                setGraphic(null);
            } else {
                nameLabel.setText(item.name);
                taskLabel.setText("Task: " + item.assignedTask);
                statusLabel.setText(item.status.toUpperCase());

                if ("Active".equalsIgnoreCase(item.status)) {
                    statusLabel.setStyle("-fx-background-color: " + STATUS_ACTIVE_COLOR + "; -fx-background-radius: 10;"); // Increased radius
                } else {
                    statusLabel.setStyle("-fx-background-color: " + STATUS_INACTIVE_COLOR + "; -fx-background-radius: 10;"); // Increased radius
                }
                setGraphic(content);
            }
        }
    }

    public ManageVolunteersView(Stage primaryStage, Scene previousScene) {
        this.primaryStage = primaryStage;
        this.previousScene = previousScene;
        this.taskDao = new TaskDao();
        this.volunteerDao = new VolunteerDao();
    }

    @Override
    public void start(Stage stage) {
        VBox rootLayout = new VBox(40); // Increased spacing
        rootLayout.setPadding(new Insets(60, 80, 80, 80)); // Increased padding
        rootLayout.setStyle("-fx-background-color: " + BACKGROUND_COLOR + ";");
        rootLayout.setAlignment(Pos.TOP_CENTER);

        rootLayout.getChildren().addAll(createHeader(), createVolunteersList());

        ScrollPane scrollPane = new ScrollPane(rootLayout);
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        Scene scene = new Scene(scrollPane,1920,1080);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Safe Serve");

        // --- Firebase Listeners ---
        ObservableList<Task> allTasks = FXCollections.observableArrayList();
        ObservableList<Volunteer> allVolunteers = FXCollections.observableArrayList();

        // Listen for real-time updates from both collections
        taskDao.listenForTasks(allTasks);
        volunteerDao.listenForVolunteers(allVolunteers);

        // Add listeners to update the UI whenever the data changes
        allTasks.addListener((ListChangeListener<Task>) c -> updateVolunteerItems(allVolunteers, allTasks));
        allVolunteers.addListener((ListChangeListener<Volunteer>) c -> updateVolunteerItems(allVolunteers, allTasks));

        // Initial data load
        updateVolunteerItems(allVolunteers, allTasks);
    }

    private void updateVolunteerItems(ObservableList<Volunteer> allVolunteers, ObservableList<Task> allTasks) {
        volunteerItems.clear();
        for (Volunteer volunteer : allVolunteers) {
            Optional<Task> assignedTask = allTasks.stream()
                .filter(task -> task.getAssignedTo().equals(volunteer.getName()))
                .findFirst();

            if (assignedTask.isPresent()) {
                volunteerItems.add(new VolunteerItem(volunteer.getName(), "Active", assignedTask.get().getDescription()));
            } else {
                volunteerItems.add(new VolunteerItem(volunteer.getName(), "Inactive", "On Standby"));
            }
        }
    }

    private Node createHeader() {
        HBox header = new HBox();
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(20, 0, 30, 0)); // Increased padding

        Label title = new Label("Manage Volunteers");
        title.setFont(getFont(42, FontWeight.BOLD)); // Increased font size
        title.setTextFill(Color.web(FONT_COLOR_DARK));

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button backButton = new Button("⬅️ Back to Dashboard");
        backButton.setFont(getFont(20, FontWeight.MEDIUM)); // Increased font size
        backButton.setTextFill(Color.web(PRIMARY_BLUE));
        backButton.setStyle("-fx-background-color: transparent; -fx-cursor: hand; -fx-border-color: " + PRIMARY_BLUE + "; -fx-border-radius: 12;"); // Increased radius
        backButton.setPadding(new Insets(15, 28, 15, 28)); // Increased padding
        backButton.setOnAction(e -> primaryStage.setScene(previousScene));

        header.getChildren().addAll(title, spacer, backButton);
        return header;
    }

    private Node createVolunteersList() {
        VBox card = new VBox(15); // Increased spacing
        card.setPadding(new Insets(40)); // Increased padding
        card.setStyle("-fx-background-color: " + CARD_BACKGROUND_COLOR + "; " +
                      "-fx-background-radius: 20; " + // Increased radius
                      "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.06), 20, 0, 0, 8);"); // Increased shadow

        volunteerItems = FXCollections.observableArrayList();

        ListView<VolunteerItem> listView = new ListView<>(volunteerItems);
        listView.setCellFactory(param -> new VolunteerCell());
        listView.setStyle("-fx-background-color: transparent;");
        listView.setPlaceholder(new Label("No volunteers found in the database."));
        listView.setPrefHeight(600); // Increased preferred height

        card.getChildren().add(listView);
        
        VBox container = new VBox(card);
        container.setAlignment(Pos.CENTER);
        
        return container;
    }
}