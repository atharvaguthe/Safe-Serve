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
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class MyRequestsView extends Application {

    // --- Style Definitions ---
    private static final String BACKGROUND_COLOR = "#F4F7FC";
    private static final String PRIMARY_BLUE = "#005A9C";
    private static final String FONT_COLOR_DARK = "#2D3748";
    private static final String CARD_BACKGROUND_COLOR = "#FFFFFF";

    private final Scene previousScene;
    private final Stage primaryStage;
    private final String userName;
    private DisasterDao disasterDao;
    private ObservableList<Disaster> myRequestsList;

    private static Font getFont(double size, FontWeight weight) {
        return Font.font("System", weight, size);
    }

    public MyRequestsView(Stage primaryStage, Scene previousScene, String userName) {
        this.primaryStage = primaryStage;
        this.previousScene = previousScene;
        this.userName = userName;
    }

    @Override
    public void start(Stage stage) {
        // Initialize DAO and list, then start listening for requests by the current user
        disasterDao = new DisasterDao();
        myRequestsList = FXCollections.observableArrayList();
        disasterDao.listenForDisastersByCitizen(myRequestsList, this.userName);

        VBox rootLayout = new VBox(40); // Increased spacing
        rootLayout.setPadding(new Insets(60)); // Increased padding
        rootLayout.setStyle("-fx-background-color: " + BACKGROUND_COLOR + ";");
        rootLayout.setAlignment(Pos.TOP_CENTER);

        rootLayout.getChildren().addAll(createHeader(), createMyRequestsList());

        ScrollPane scrollPane = new ScrollPane(rootLayout);
        scrollPane.setFitToWidth(true);

        Scene scene = new Scene(scrollPane,1920,1080);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Safe Serve");
    }

    private Node createHeader() {
        HBox header = new HBox();
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(0, 0, 30, 0)); // Increased padding

        Label title = new Label("📋 My Submitted Requests");
        title.setFont(getFont(40, FontWeight.BOLD)); // Increased font size
        title.setTextFill(Color.web(FONT_COLOR_DARK));

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button backButton = new Button("⬅️ Back to Dashboard");
        backButton.setFont(getFont(20, FontWeight.MEDIUM)); // Increased font size
        backButton.setTextFill(Color.web(PRIMARY_BLUE));
        backButton.setStyle("-fx-background-color: transparent; -fx-cursor: hand; -fx-border-color: " + PRIMARY_BLUE + "; -fx-border-radius: 10;"); // Increased radius
        backButton.setPadding(new Insets(10, 20, 10, 20)); // Increased padding
        backButton.setOnAction(e -> primaryStage.setScene(previousScene));

        header.getChildren().addAll(title, spacer, backButton);
        return header;
    }

    private Node createMyRequestsList() {
        VBox card = new VBox(15); // Added spacing for potential future elements within the card
        card.setPadding(new Insets(40)); // Increased padding significantly
        card.setStyle("-fx-background-color: " + CARD_BACKGROUND_COLOR + "; " +
                      "-fx-background-radius: 20; " + // Increased radius
                      "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.06), 20, 0, 0, 8);"); // Increased shadow

        ListView<Disaster> listView = new ListView<>(myRequestsList);
        listView.setPlaceholder(new Label("You have not submitted any requests."));
        listView.setPrefHeight(600); // Set a preferred height to make the list box big
        listView.setCellFactory(param -> new ListCell<Disaster>() {
            @Override
            protected void updateItem(Disaster item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    HBox content = new HBox(30); // Increased spacing
                    content.setAlignment(Pos.CENTER_LEFT);
                    content.setPadding(new Insets(25)); // Increased padding for each item
                    
                    VBox details = new VBox(10); // Increased spacing
                    Label typeLabel = new Label(item.getType());
                    typeLabel.setFont(MyRequestsView.getFont(22, FontWeight.BOLD)); // Increased font size
                    Label locationLabel = new Label(item.getLocation());
                    locationLabel.setFont(MyRequestsView.getFont(20, FontWeight.NORMAL)); // Increased font size
                    details.getChildren().addAll(typeLabel, locationLabel);

                    // For now, we just show the details. Status tracking could be added later.
                    
                    content.getChildren().add(details);
                    setGraphic(content);
                }
            }
        });
        listView.setStyle("-fx-background-color: transparent;");

        card.getChildren().add(listView);
        
        VBox container = new VBox(card);
        container.setAlignment(Pos.CENTER);
        
        return container;
    }
}