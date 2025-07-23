package com.thinkspark.view;

import javafx.animation.FadeTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Duration;

public class StartupScreen extends Application {

    // --- Style Definitions ---
    private static final String BACKGROUND_COLOR = "#F4F7FC";
    private static final String PRIMARY_BLUE = "#005A9C";
    private static final String FONT_COLOR_DARK = "#2D3748";

    private static Font getFont(double size, FontWeight weight) {
        return Font.font("System", weight, size);
    }

    @Override
    public void start(Stage primaryStage) {
        VBox rootLayout = new VBox(30);
        rootLayout.setAlignment(Pos.CENTER);
        rootLayout.setPadding(new Insets(40));
        rootLayout.setStyle("-fx-background-color: " + BACKGROUND_COLOR + ";");

        // Logo
        ImageView logoView = new ImageView(new Image(getClass().getResourceAsStream("/logo.jpg")));
        logoView.setFitHeight(350);
        logoView.setPreserveRatio(true);
        logoView.setOpacity(0); // Start transparent for fade-in

        // Title
        Label title = new Label("Welcome to Safe Serve");
        title.setFont(getFont(32, FontWeight.BOLD));
        title.setTextFill(Color.web(FONT_COLOR_DARK));
        title.setOpacity(0); // Start transparent for fade-in

        // Start Button
        Button startButton = new Button("Start");
        startButton.setFont(getFont(18, FontWeight.BOLD));
        startButton.setTextFill(Color.WHITE);
        startButton.setStyle("-fx-background-color: " + PRIMARY_BLUE + "; -fx-background-radius: 8; -fx-cursor: hand;");
        startButton.setPadding(new Insets(15, 40, 15, 40));
        startButton.setOpacity(0); // Start transparent for fade-in

        // Button Hover Effect
        DropShadow shadow = new DropShadow();
        startButton.setOnMouseEntered(e -> startButton.setEffect(shadow));
        startButton.setOnMouseExited(e -> startButton.setEffect(null));


        // Button Action
        startButton.setOnAction(e -> {
            // No longer closing the primary stage, just setting a new scene
            new DisasterHelperUI().start(primaryStage);
        });

        rootLayout.getChildren().addAll(logoView, title, startButton);

        Scene scene = new Scene(rootLayout, 1920, 1080);
        primaryStage.setTitle("Welcome to Safe Serve");
        primaryStage.setScene(scene);
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/logo.jpg")));
        
        // Set to full screen
        primaryStage.setMaximized(true);

        primaryStage.show();
        
        // --- Animations ---
        // Logo fade-in
        FadeTransition logoFadeIn = new FadeTransition(Duration.seconds(1), logoView);
        logoFadeIn.setToValue(1);

        // Title slide-up and fade-in
        TranslateTransition titleSlideUp = new TranslateTransition(Duration.seconds(0.8), title);
        titleSlideUp.setFromY(50);
        titleSlideUp.setToY(0);
        FadeTransition titleFadeIn = new FadeTransition(Duration.seconds(0.8), title);
        titleFadeIn.setToValue(1);

        // Button fade-in
        FadeTransition buttonFadeIn = new FadeTransition(Duration.seconds(0.5), startButton);
        buttonFadeIn.setToValue(1);

        // Play animations in sequence
        SequentialTransition sequentialTransition = new SequentialTransition(logoFadeIn, titleSlideUp, titleFadeIn, buttonFadeIn);
        sequentialTransition.play();
    }
}