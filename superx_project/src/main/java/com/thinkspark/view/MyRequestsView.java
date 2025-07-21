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

        VBox rootLayout = new VBox(30);
        rootLayout.setPadding(new Insets(40));
        rootLayout.setStyle("-fx-background-color: " + BACKGROUND_COLOR + ";");
        rootLayout.setAlignment(Pos.TOP_CENTER);

        rootLayout.getChildren().addAll(createHeader(), createMyRequestsList());

        ScrollPane scrollPane = new ScrollPane(rootLayout);
        scrollPane.setFitToWidth(true);

        Scene scene = new Scene(scrollPane, 1280, 900);
        primaryStage.setScene(scene);
        primaryStage.setTitle("My Requests | Citizen Dashboard");
    }

    private Node createHeader() {
        HBox header = new HBox();
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(0, 0, 20, 0));

        Label title = new Label("📋 My Submitted Requests");
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

    private Node createMyRequestsList() {
        VBox card = new VBox(0);
        card.setPadding(new Insets(20));
        card.setStyle("-fx-background-color: " + CARD_BACKGROUND_COLOR + "; " +
                      "-fx-background-radius: 12; " +
                      "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.05), 10, 0, 0, 4);");

        ListView<Disaster> listView = new ListView<>(myRequestsList);
        listView.setPlaceholder(new Label("You have not submitted any requests."));
        listView.setCellFactory(param -> new ListCell<Disaster>() {
            @Override
            protected void updateItem(Disaster item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    HBox content = new HBox(20);
                    content.setAlignment(Pos.CENTER_LEFT);
                    content.setPadding(new Insets(15));
                    
                    VBox details = new VBox(5);
                    Label typeLabel = new Label(item.getType());
                    typeLabel.setFont(MyRequestsView.getFont(16, FontWeight.BOLD));
                    Label locationLabel = new Label(item.getLocation());
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
