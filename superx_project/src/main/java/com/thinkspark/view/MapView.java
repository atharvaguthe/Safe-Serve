package com.thinkspark.view;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.scene.control.Label;


public class MapView extends Application {

    private final Stage primaryStage;
    private final Scene previousScene;

    public MapView(Stage primaryStage, Scene previousScene) {
        this.primaryStage = primaryStage;
        this.previousScene = previousScene;
    }

    @Override
    public void start(Stage stage) {
        VBox rootLayout = new VBox(20);
        rootLayout.setPadding(new Insets(20, 40, 40, 40));
        rootLayout.setStyle("-fx-background-color: #F4F7FC;");

        // Header
        HBox header = new HBox();
        header.setAlignment(Pos.CENTER_LEFT);
        Label title = new Label("🗺️ Disaster Map");
        title.setFont(Font.font("System", FontWeight.BOLD, 32));
        title.setTextFill(Color.web("#2D3748"));
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        Button backButton = new Button("⬅️ Back to Dashboard");
        backButton.setFont(Font.font("System", FontWeight.MEDIUM, 16));
        backButton.setTextFill(Color.web("#005A9C"));
        backButton.setStyle("-fx-background-color: transparent; -fx-cursor: hand; -fx-border-color: #005A9C; -fx-border-radius: 8;");
        backButton.setPadding(new Insets(8, 16, 8, 16));
        backButton.setOnAction(e -> primaryStage.setScene(previousScene));
        header.getChildren().addAll(title, spacer, backButton);

        // WebView for the map
        WebView webView = new WebView();
        VBox.setVgrow(webView, Priority.ALWAYS);

        // Load the local HTML file that contains the map
        try {
            webView.getEngine().load(getClass().getResource("/map.html").toExternalForm());
        } catch (Exception e) {
            e.printStackTrace();
            // Handle error, maybe show a message to the user
            Label errorLabel = new Label("Error loading map. Make sure 'map.html' is in the resources folder.");
            rootLayout.getChildren().add(errorLabel);
        }
        
        rootLayout.getChildren().addAll(header, webView);

        Scene scene = new Scene(rootLayout);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Safe Serve");
    }
}