package com.thinkspark.view;

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
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class AllRequestsView extends Application {

    // --- Style Definitions ---
    private static final String BACKGROUND_COLOR = "#F4F7FC";
    private static final String PRIMARY_BLUE = "#005A9C";
    private static final String FONT_COLOR_DARK = "#2D3748";
    private static final String CARD_BACKGROUND_COLOR = "#FFFFFF";
    private static final String BORDER_COLOR = "#E2E8F0";
    private static final String STATUS_PENDING_COLOR = "#DD6B20"; // Orange
    private static final String STATUS_IN_PROGRESS_COLOR = "#3182CE"; // Blue
    private static final String STATUS_RESOLVED_COLOR = "#38A169"; // Green

    private final Scene previousScene;
    private final Stage primaryStage;

    // --- Font Helper ---
    private static Font getFont(double size, FontWeight weight) {
        return Font.font("System", weight, size);
    }

    /**
     * Represents a single request item in the list.
     */
    private static class RequestItem {
        private final String type;
        private final String location;
        private final String status;
        private final String timestamp;

        public RequestItem(String type, String location, String status, String timestamp) {
            this.type = type;
            this.location = location;
            this.status = status;
            this.timestamp = timestamp;
        }

        public String getType() { return type; }
        public String getLocation() { return location; }
        public String getStatus() { return status; }
        public String getTimestamp() { return timestamp; }
    }

    /**
     * Custom ListCell to display RequestItem data.
     */
    private static class RequestCell extends ListCell<RequestItem> {
        private final HBox content = new HBox();
        private final VBox details = new VBox(5);
        private final Label typeLabel = new Label();
        private final Label locationLabel = new Label();
        private final Label timestampLabel = new Label();
        private final Label statusLabel = new Label();
        private final Region spacer = new Region();

        public RequestCell() {
            super();
            typeLabel.setFont(AllRequestsView.getFont(16, FontWeight.BOLD));
            locationLabel.setFont(AllRequestsView.getFont(14, FontWeight.NORMAL));
            timestampLabel.setFont(AllRequestsView.getFont(12, FontWeight.NORMAL));
            timestampLabel.setTextFill(Color.web(FONT_COLOR_DARK, 0.7));

            statusLabel.setFont(AllRequestsView.getFont(14, FontWeight.BOLD));
            statusLabel.setPadding(new Insets(4, 8, 4, 8));
            statusLabel.setStyle("-fx-background-radius: 6;");

            details.getChildren().addAll(typeLabel, locationLabel, timestampLabel);
            HBox.setHgrow(spacer, Priority.ALWAYS);
            content.setAlignment(Pos.CENTER);
            content.setSpacing(20);
            content.getChildren().addAll(details, spacer, statusLabel);
            content.setPadding(new Insets(15));
            content.setStyle("-fx-border-color: " + BORDER_COLOR + " transparent transparent transparent; -fx-border-width: 1;");
        }

        @Override
        protected void updateItem(RequestItem item, boolean empty) {
            super.updateItem(item, empty);
            if (empty || item == null) {
                setGraphic(null);
            } else {
                typeLabel.setText(item.getType());
                locationLabel.setText(item.getLocation());
                timestampLabel.setText("Reported: " + item.getTimestamp());
                statusLabel.setText(item.getStatus());

                String statusColor;
                switch (item.getStatus().toLowerCase()) {
                    case "in progress":
                        statusColor = STATUS_IN_PROGRESS_COLOR;
                        break;
                    case "resolved":
                        statusColor = STATUS_RESOLVED_COLOR;
                        break;
                    default: // Pending
                        statusColor = STATUS_PENDING_COLOR;
                        break;
                }
                statusLabel.setStyle("-fx-background-color: " + statusColor + "; -fx-text-fill: white; -fx-background-radius: 6;");
                setGraphic(content);
            }
        }
    }

    public AllRequestsView(Stage primaryStage, Scene previousScene) {
        this.primaryStage = primaryStage;
        this.previousScene = previousScene;
    }

    @Override
    public void start(Stage stage) {
        VBox rootLayout = new VBox(30);
        rootLayout.setPadding(new Insets(40));
        rootLayout.setStyle("-fx-background-color: " + BACKGROUND_COLOR + ";");
        rootLayout.setAlignment(Pos.TOP_CENTER);

        Node header = createHeader();
        Node requestsList = createRequestsList();

        rootLayout.getChildren().addAll(header, requestsList);

        ScrollPane scrollPane = new ScrollPane(rootLayout);
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        Scene scene = new Scene(scrollPane, 1280, 900);
        primaryStage.setScene(scene);
        primaryStage.setTitle("All Requests | Disaster Relief Helper");
    }

    private Node createHeader() {
        HBox header = new HBox();
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(0, 0, 20, 0));

        Label title = new Label("📋 All Active Requests");
        title.setFont(getFont(32, FontWeight.BOLD));
        title.setTextFill(Color.web(FONT_COLOR_DARK));

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button backButton = new Button("⬅️ Back to Dashboard");
        backButton.setFont(getFont(16, FontWeight.MEDIUM));
        backButton.setTextFill(Color.web(PRIMARY_BLUE));
        backButton.setStyle("-fx-background-color: transparent; -fx-cursor: hand; -fx-border-color: " + PRIMARY_BLUE + "; -fx-border-radius: 8;");
        backButton.setPadding(new Insets(8, 16, 8, 16));
        backButton.setOnAction(e -> {
            primaryStage.setScene(previousScene);
            primaryStage.setTitle("Citizen Dashboard | Disaster Relief Helper");
        });

        header.getChildren().addAll(title, spacer, backButton);
        return header;
    }

    private Node createRequestsList() {
        VBox card = new VBox(0);
        card.setPadding(new Insets(20));
        card.setStyle("-fx-background-color: " + CARD_BACKGROUND_COLOR + "; " +
                      "-fx-background-radius: 12; " +
                      "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.05), 10, 0, 0, 4);");

        // Mock data for demonstration
        ObservableList<RequestItem> requests = FXCollections.observableArrayList(
            new RequestItem("Medical Aid", "Central City Hospital", "In Progress", "2 hours ago"),
            new RequestItem("Food & Water", "North District Shelter", "Pending", "5 hours ago"),
            new RequestItem("Rescue", "Riverside Park Area", "Resolved", "1 day ago"),
            new RequestItem("Power Outage Report", "West Suburbs, Block C", "In Progress", "1 day ago"),
            new RequestItem("Shelter", "Community Hall", "Resolved", "2 days ago")
        );

        ListView<RequestItem> listView = new ListView<>(requests);
        listView.setCellFactory(param -> new RequestCell());
        listView.setStyle("-fx-background-color: transparent;");

        card.getChildren().add(listView);
        
        VBox container = new VBox(card);
        container.setAlignment(Pos.CENTER);
        
        return container;
    }

}
