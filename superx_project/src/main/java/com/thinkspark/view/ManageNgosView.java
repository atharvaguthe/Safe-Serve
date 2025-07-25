package com.thinkspark.view;

import com.thinkspark.dao.NgoDao;
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

public class ManageNgosView extends Application {
    // --- Style Definitions ---
    private static final String BACKGROUND_COLOR = "#F4F7FC";
    private static final String PRIMARY_BLUE = "#005A9C";
    private static final String CARD_BACKGROUND_COLOR = "#FFFFFF";
    private static final String BORDER_COLOR = "#E2E8F0";
    private static final String STATUS_VERIFIED_COLOR = "#38A169";
    private static final String STATUS_PENDING_COLOR = "#DD6B20";
    private static final String ACTION_BUTTON_COLOR = "#3182CE";

    private final Scene previousScene;
    private final Stage primaryStage;
    private final NgoDao ngoDao;
    private ObservableList<NgoItem> ngosList;

    private static Font getFont(double size, FontWeight weight) {
        return Font.font("System", weight, size);
    }

    /**
     * Public static inner class to represent an NGO.
     */
    public static class NgoItem {
        private String name;
        private String status;
        private String contact;

        public NgoItem(String name, String status, String contact) {
            this.name = name;
            this.status = status;
            this.contact = contact;
        }

        public String getName() { return name; }
        public String getStatus() { return status; }
        public String getContact() { return contact; }
        public void setStatus(String status) { this.status = status; }
    }

    /**
     * Custom ListCell to display NGO data and provide a verification button.
     */
    private class NgoCell extends ListCell<NgoItem> {
        @Override
        protected void updateItem(NgoItem item, boolean empty) {
            super.updateItem(item, empty);
            if (empty || item == null) {
                setGraphic(null);
            } else {
                HBox content = new HBox(25); // Increased spacing
                content.setAlignment(Pos.CENTER);
                content.setPadding(new Insets(25)); // Increased padding
                content.setStyle("-fx-border-color: transparent transparent " + BORDER_COLOR + " transparent; -fx-border-width: 1;");

                Label nameLabel = new Label(item.getName());
                nameLabel.setFont(ManageNgosView.getFont(20, FontWeight.BOLD)); // Increased font size
                Label contactLabel = new Label("Contact: " + item.getContact());
                contactLabel.setFont(ManageNgosView.getFont(18, FontWeight.NORMAL)); // Increased font size
                contactLabel.setOpacity(0.8);
                VBox details = new VBox(10, nameLabel, contactLabel); // Increased spacing

                Region spacer = new Region();
                HBox.setHgrow(spacer, Priority.ALWAYS);

                Label statusLabel = new Label(item.getStatus().toUpperCase());
                statusLabel.setFont(ManageNgosView.getFont(16, FontWeight.BOLD)); // Increased font size
                statusLabel.setTextFill(Color.WHITE);
                statusLabel.setPadding(new Insets(8, 12, 8, 12)); // Increased padding
                String statusColor = "Verified".equalsIgnoreCase(item.getStatus()) ? STATUS_VERIFIED_COLOR : STATUS_PENDING_COLOR;
                statusLabel.setStyle("-fx-background-color: " + statusColor + "; -fx-background-radius: 10;"); // Increased radius

                content.getChildren().addAll(details, spacer, statusLabel);

                if ("Pending Verification".equalsIgnoreCase(item.getStatus())) {
                    Button verifyButton = new Button("Verify NGO");
                    verifyButton.setFont(ManageNgosView.getFont(16, FontWeight.BOLD)); // Increased font size
                    verifyButton.setTextFill(Color.WHITE);
                    verifyButton.setStyle("-fx-background-color: " + ACTION_BUTTON_COLOR + "; -fx-background-radius: 10; -fx-cursor: hand;"); // Increased radius
                    verifyButton.setPadding(new Insets(10, 20, 10, 20)); // Increased padding
                    verifyButton.setOnAction(e -> {
                        item.setStatus("Verified");
                        getListView().refresh();
                    });
                    content.getChildren().add(verifyButton);
                }
                setGraphic(content);
            }
        }
    }

    public ManageNgosView(Stage primaryStage, Scene previousScene) {
        this.primaryStage = primaryStage;
        this.previousScene = previousScene;
        this.ngoDao = new NgoDao();
    }

    @Override
    public void start(Stage stage) {
        VBox rootLayout = new VBox(40); // Increased spacing
        rootLayout.setPadding(new Insets(60, 80, 80, 80)); // Increased padding
        rootLayout.setStyle("-fx-background-color: " + BACKGROUND_COLOR + ";");
        rootLayout.setAlignment(Pos.TOP_CENTER);

        rootLayout.getChildren().addAll(createHeader(), createNgosList());

        ScrollPane scrollPane = new ScrollPane(rootLayout);
        scrollPane.setFitToWidth(true);
        Scene scene = new Scene(scrollPane,1920,1080);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Safe Serve");

        // Start listening for NGOs from Firebase
        ngoDao.listenForNgos(ngosList);
    }

    private Node createHeader() {
        HBox header = new HBox();
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(20, 0, 30, 0)); // Increased padding

        Label title = new Label("Manage Partner NGOs");
        title.setFont(getFont(42, FontWeight.BOLD)); // Increased font size

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

    private Node createNgosList() {
        VBox card = new VBox(15); // Increased spacing
        card.setPadding(new Insets(40)); // Increased padding
        card.setStyle("-fx-background-color: " + CARD_BACKGROUND_COLOR + "; " +
                      "-fx-background-radius: 20; " + // Increased radius
                      "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.06), 20, 0, 0, 8);"); // Increased shadow

        ngosList = FXCollections.observableArrayList();

        ListView<NgoItem> listView = new ListView<>(ngosList);
        listView.setCellFactory(param -> new NgoCell());
        listView.setStyle("-fx-background-color: transparent;");
        listView.setPlaceholder(new Label("No NGOs to display."));
        listView.setPrefHeight(600); // Increased preferred height

        card.getChildren().add(listView);
        
        VBox container = new VBox(card);
        container.setAlignment(Pos.CENTER);
        
        return container;
    }
}