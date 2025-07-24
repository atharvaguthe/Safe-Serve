package com.thinkspark.view;

import com.thinkspark.dao.DisasterDao;
import com.thinkspark.dao.TaskDao;
import com.thinkspark.dao.VolunteerDao;
import com.thinkspark.model.Disaster;
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
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.io.InputStream;
import java.util.Optional;
import java.util.stream.Collectors;

public class AllRequestsView extends Application {

    // --- Style Definitions ---
    private static final String BACKGROUND_COLOR = "#F4F7FC";
    private static final String PRIMARY_BLUE = "#005A9C";
    private static final String FONT_COLOR_DARK = "#2D3748";
    private static final String CARD_BACKGROUND_COLOR = "#FFFFFF";
    private static final String BORDER_COLOR = "#E2E8F0";
    private static final String STATUS_PENDING_COLOR = "#DD6B20"; // Orange
    private static final String STATUS_ASSIGNED_COLOR = "#3182CE"; // Blue
    private static final String STATUS_COMPLETED_COLOR = "#38A169"; // Green
    private static final String ACTION_BUTTON_COLOR = "#38A169"; // Green

    private final Scene previousScene;
    private final Stage primaryStage;
    private ListView<RequestItem> listView;
    private ObservableList<RequestItem> requestItems;
    private final DisasterDao disasterDao;
    private final VolunteerDao volunteerDao;
    private final TaskDao taskDao;
    private final ObservableList<Volunteer> availableVolunteers;
    private final ObservableList<Task> allTasks;

    private static Font getFont(double size, FontWeight weight) {
        return Font.font("System", weight, size);
    }

    /**
     * A wrapper class to hold a Disaster object and its current status for the view.
     */
    private static class RequestItem {
        private final Disaster disaster;
        private String status;

        public RequestItem(Disaster disaster, String status) {
            this.disaster = disaster;
            this.status = status;
        }

        public Disaster getDisaster() { return disaster; }
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
    }

    /**
     * Custom ListCell to display RequestItem data and provide an "Assign Task" button.
     */
     private class RequestCell extends ListCell<RequestItem> {
        @Override
        protected void updateItem(RequestItem item, boolean empty) {
            super.updateItem(item, empty);

            if (empty || item == null) {
                setGraphic(null);
            } else {
                HBox content = new HBox(20);
                content.setAlignment(Pos.CENTER);
                content.setPadding(new Insets(15));
                content.setStyle("-fx-border-color: transparent transparent " + BORDER_COLOR + " transparent; -fx-border-width: 1;");

                // Always initialize icon as empty ImageView
                ImageView icon = new ImageView();
                icon.setFitHeight(50);
                icon.setFitWidth(50);
                // Only try to load an image if possible
                if (item.getDisaster() != null) {
                    String imgUrl = item.getDisaster().getImageUrl();
                    if (imgUrl != null && !imgUrl.isEmpty()) {
                        try {
                            icon.setImage(new Image(imgUrl));
                        } catch (Exception e) {
                            System.err.println("Error loading image from URL: " + imgUrl + ". Falling back to default logo.");
                            e.printStackTrace();
                            InputStream defaultLogoStream = getClass().getResourceAsStream("/logo.png");
                            if (defaultLogoStream != null) {
                                icon.setImage(new Image(defaultLogoStream));
                            } else {
                                System.err.println("Default logo.png not found in resources!");
                            }
                        }
                    } else {
                        InputStream defaultLogoStream = getClass().getResourceAsStream("/logo.png");
                        if (defaultLogoStream != null) {
                            icon.setImage(new Image(defaultLogoStream));
                        } else {
                            System.err.println("Default logo.png not found in resources!");
                        }
                    }
                }

                // Add null checks for item.getDisaster()
                Label typeLabel = new Label(item.getDisaster() != null ? item.getDisaster().getType() : "N/A");
                typeLabel.setFont(AllRequestsView.getFont(16, FontWeight.BOLD));
                Label locationLabel = new Label(item.getDisaster() != null ? item.getDisaster().getLocation() : "N/A");
                VBox details = new VBox(5, typeLabel, locationLabel);

                Region spacer = new Region();
                HBox.setHgrow(spacer, Priority.ALWAYS);

                Label statusLabel = new Label(item.getStatus() != null ? item.getStatus().toUpperCase() : "N/A");
                statusLabel.setFont(AllRequestsView.getFont(12, FontWeight.BOLD));
                statusLabel.setTextFill(Color.WHITE);
                statusLabel.setPadding(new Insets(4, 8, 4, 8));

                String statusLower = item.getStatus() != null ? item.getStatus().toLowerCase() : "";
                String statusColor;
                switch (statusLower) {
                    case "pending":
                        statusColor = STATUS_PENDING_COLOR;
                        break;
                    case "assigned":
                        statusColor = STATUS_ASSIGNED_COLOR;
                        break;
                    case "completed":
                        statusColor = STATUS_COMPLETED_COLOR;
                        break;
                    default:
                        statusColor = FONT_COLOR_DARK;
                        break;
                }
                statusLabel.setStyle("-fx-background-color: " + statusColor + "; -fx-background-radius: 6;");

                content.getChildren().addAll(icon, details, spacer, statusLabel);

                // Only show the assign button if the task is pending
                if ("pending".equalsIgnoreCase(item.getStatus())) {
                    Button assignButton = new Button("Assign Task");
                    assignButton.setTextFill(Color.WHITE);
                    assignButton.setStyle("-fx-background-color: " + ACTION_BUTTON_COLOR + "; -fx-background-radius: 6; -fx-cursor: hand;");
                    assignButton.setOnAction(e -> assignTaskToVolunteer(item));
                    content.getChildren().add(assignButton);
                }
                setGraphic(content);
            }
        }
    }

    /**
     * Handles the logic for assigning a task to a volunteer.
     * @param request The request item to be assigned.
     */
    private void assignTaskToVolunteer(RequestItem request) {
        // Filter out volunteers who are already assigned to a task that is not completed
        ObservableList<Volunteer> currentlyAvailableVolunteers = availableVolunteers.stream()
            .filter(volunteer -> allTasks.stream()
                .noneMatch(task -> task.getAssignedTo().equals(volunteer.getName()) && !task.getStatus().equalsIgnoreCase("completed")))
            .collect(Collectors.toCollection(FXCollections::observableArrayList));


        if (currentlyAvailableVolunteers.isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "There are no available volunteers to assign. All are currently assigned tasks or none registered.", ButtonType.OK).show();
            return;
        }

        ChoiceDialog<Volunteer> dialog = new ChoiceDialog<>(currentlyAvailableVolunteers.get(0), currentlyAvailableVolunteers);
        dialog.setTitle("Assign Task to Volunteer");
        dialog.setHeaderText("Select a volunteer for this task:");
        dialog.setContentText("Task: " + request.getDisaster().getType() + " at " + request.getDisaster().getLocation());

        Optional<Volunteer> result = dialog.showAndWait();
        result.ifPresent(volunteer -> {
            String taskDescription = request.getDisaster().getType() + ": " + request.getDisaster().getDescription();
            
            // Create a new Task with the disasterId
            Task newTask = new Task(taskDescription, volunteer.getEmail(), request.getDisaster().getFirebaseDocId());
            taskDao.saveTask(newTask); // Save task to Firestore

            // Update the status of the original Disaster report in Firestore
            disasterDao.updateDisasterStatus(request.getDisaster().getFirebaseDocId(), "Assigned");
            
            // The ListChangeListener on disasterReports will update requestItems and refresh the view
            new Alert(Alert.AlertType.INFORMATION, "Task successfully assigned to " + volunteer.getName() + ".", ButtonType.OK).show();
        });
    }

    public AllRequestsView(Stage primaryStage, Scene previousScene) {
        this.primaryStage = primaryStage;
        this.previousScene = previousScene;
        this.disasterDao = new DisasterDao();
        this.volunteerDao = new VolunteerDao();
        this.taskDao = new TaskDao();
        this.availableVolunteers = FXCollections.observableArrayList();
        this.allTasks = FXCollections.observableArrayList();
    }

    @Override
    public void start(Stage stage) {
        VBox rootLayout = new VBox(30);
        rootLayout.setPadding(new Insets(40));
        rootLayout.setStyle("-fx-background-color: " + BACKGROUND_COLOR + ";");
        rootLayout.setAlignment(Pos.TOP_CENTER);

        rootLayout.getChildren().addAll(createHeader(), createRequestsList());

        ScrollPane scrollPane = new ScrollPane(rootLayout);
        scrollPane.setFitToWidth(true);

        Scene scene = new Scene(scrollPane,1920,1080);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Safe Serve");

        // Listen for disaster reports from Firebase
        ObservableList<Disaster> disasterReports = FXCollections.observableArrayList();
        disasterDao.listenForDisasterReports(disasterReports);
        disasterReports.addListener((ListChangeListener<Disaster>) c -> {
            requestItems.setAll(
                disasterReports.stream()
                    .map(disaster -> new RequestItem(disaster, disaster.getStatus()))
                    .collect(Collectors.toList())
            );
        });
        
        // Listen for all volunteers from Firebase
        volunteerDao.listenForVolunteers(availableVolunteers);

        // Listen for all tasks from Firebase (needed to check volunteer availability)
        taskDao.listenForTasks(allTasks);
    }

    private Node createHeader() {
        HBox header = new HBox();
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(0, 0, 20, 0));

        Label title = new Label("📋 All Disaster Reports");
        title.setFont(getFont(32, FontWeight.BOLD));
        title.setTextFill(Color.web(FONT_COLOR_DARK));

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button backButton = new Button("⬅️ Back to Dashboard");
        backButton.setFont(getFont(16, FontWeight.MEDIUM));
        backButton.setTextFill(Color.web(PRIMARY_BLUE));
        backButton.setStyle("-fx-background-color: transparent; -fx-cursor: hand; -fx-border-color: " + PRIMARY_BLUE + "; -fx-border-radius: 8;");
        backButton.setPadding(new Insets(8, 16, 8, 16));
        backButton.setOnAction(e -> primaryStage.setScene(previousScene));

        header.getChildren().addAll(title, spacer, backButton);
        return header;
    }

    private Node createRequestsList() {
        VBox card = new VBox(0);
        card.setPadding(new Insets(20));
        card.setStyle("-fx-background-color: " + CARD_BACKGROUND_COLOR + "; " +
                      "-fx-background-radius: 12; " +
                      "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.05), 10, 0, 0, 4);");

        requestItems = FXCollections.observableArrayList();

        listView = new ListView<>(requestItems);
        listView.setPlaceholder(new Label("No disaster reports to display."));
        listView.setCellFactory(param -> new RequestCell());
        listView.setStyle("-fx-background-color: transparent;");

        card.getChildren().add(listView);
        
        VBox container = new VBox(card);
        container.setAlignment(Pos.CENTER);
        
        return container;
    }
}