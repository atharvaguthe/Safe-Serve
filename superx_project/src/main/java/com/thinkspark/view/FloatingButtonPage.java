package com.thinkspark.view;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class FloatingButtonPage extends Application {

    private static final String BACKGROUND_COLOR = "#F4F7FC";
    private static final String PRIMARY_BLUE = "#005A9C";
    private static final String FONT_COLOR_DARK = "#2D3748";
    private static final String CARD_BACKGROUND_COLOR = "#FFFFFF";

    private final Scene previousScene;
    private final Stage primaryStage;

    private static Font getFont(double size, FontWeight weight) {
        return Font.font("System", weight, size);
    }

    /**
     * Constructor to receive the primary stage and the scene of the previous page (DisasterHelperUI).
     * @param primaryStage The main application stage.
     * @param previousScene The scene of the DisasterHelperUI.
     */
    public FloatingButtonPage(Stage primaryStage, Scene previousScene) {
        this.primaryStage = primaryStage;
        this.previousScene = previousScene;
    }

    @Override
    public void start(Stage stage) {
        VBox rootLayout = new VBox(30);
        rootLayout.setPadding(new Insets(40));
        rootLayout.setStyle("-fx-background-color: " + BACKGROUND_COLOR + ";");
        rootLayout.setAlignment(Pos.TOP_CENTER);

        // Header with a back button
        HBox header = new HBox();
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(0, 0, 20, 0));

        Label title = new Label("🚀 New Feature Unlocked!");
        title.setFont(getFont(32, FontWeight.BOLD));
        title.setTextFill(Color.web(FONT_COLOR_DARK));

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button backButton = new Button("⬅️ Back to Home");
        backButton.setFont(getFont(16, FontWeight.MEDIUM));
        backButton.setTextFill(Color.web(PRIMARY_BLUE));
        backButton.setStyle("-fx-background-color: transparent; -fx-cursor: hand; -fx-border-color: " + PRIMARY_BLUE + "; -fx-border-radius: 8;");
        backButton.setPadding(new Insets(8, 16, 8, 16));
        backButton.setOnAction(e -> {
            primaryStage.setScene(previousScene);
            primaryStage.setTitle("Safe Serve"); // Restore previous title
        });

        header.getChildren().addAll(title, spacer, backButton);

        // Main content of the new page
        VBox contentCard = new VBox(20);
        contentCard.setPadding(new Insets(40));
        contentCard.setStyle("-fx-background-color: " + CARD_BACKGROUND_COLOR + "; " +
                             "-fx-background-radius: 12; " +
                             "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.05), 10, 0, 0, 4);");
        contentCard.setMaxWidth(800);
        contentCard.setAlignment(Pos.CENTER);

        Label welcomeLabel = new Label("Welcome to the Floating Button Page!");
        welcomeLabel.setFont(getFont(28, FontWeight.BOLD));
        welcomeLabel.setTextFill(Color.web(PRIMARY_BLUE));

        Label infoLabel = new Label("This page was opened by clicking the floating circular button. You can add any new feature or information here.");
        infoLabel.setFont(getFont(16, FontWeight.NORMAL));
        infoLabel.setTextFill(Color.web(FONT_COLOR_DARK));
        infoLabel.setWrapText(true);
        infoLabel.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);

        contentCard.getChildren().addAll(welcomeLabel, infoLabel);

        rootLayout.getChildren().addAll(header, contentCard);

        Scene scene = new Scene(rootLayout, 1920, 1080);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Safe Serve | Floating Feature");
    }
}
