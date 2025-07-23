package com.thinkspark.view;

import com.thinkspark.Controller.ForgotPasswordController;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class ForgotPasswordUI extends Application {

    private static final String BACKGROUND_COLOR = "#F4F7FC";
    private static final String CARD_BACKGROUND_COLOR = "#FFFFFF";
    private static final String PRIMARY_BLUE = "#005A9C";
    private static final String FONT_COLOR_DARK = "#2D3748";
    private static final String FONT_COLOR_LIGHT = "#FFFFFF";
    private static final String BORDER_COLOR = "#E2E8F0";

    private static Font getFont(double size, FontWeight weight) {
        return Font.font("System", weight, size);
    }

    private final ForgotPasswordController controller = new ForgotPasswordController();
    private TextField emailField;

    @Override
    public void start(Stage stage) {
        HBox mainLayout = new HBox();
        mainLayout.setAlignment(Pos.CENTER);
        mainLayout.setMaxSize(960, 640);
        mainLayout.setStyle("-fx-background-color: " + CARD_BACKGROUND_COLOR + "; " +
                           "-fx-background-radius: 16; " +
                           "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 20, 0, 0, 8);");
        mainLayout.getChildren().addAll(createBrandingPane(), createForgotPasswordForm(stage));

        StackPane root = new StackPane(mainLayout);
        root.setStyle("-fx-background-color: " + BACKGROUND_COLOR + ";");
        root.setPadding(new Insets(40));

        Scene scene = new Scene(root);
        stage.setTitle("Safe Serve");
        stage.setScene(scene);
        stage.setResizable(true);
        stage.show();
    }

    private Node createBrandingPane() {
        VBox brandingPane = new VBox(20);
        brandingPane.setPrefWidth(480);
        brandingPane.setAlignment(Pos.CENTER_LEFT);
        brandingPane.setPadding(new Insets(40));
        brandingPane.setStyle("-fx-background-color: " + PRIMARY_BLUE + "; -fx-background-radius: 16 0 0 16;");

        Label logo = new Label("🔑 Forgot Password");
        logo.setFont(getFont(40, FontWeight.BOLD));
        logo.setTextFill(Color.web(FONT_COLOR_LIGHT));

        brandingPane.getChildren().addAll(logo);
        return brandingPane;
    }

    private Node createForgotPasswordForm(Stage stage) {
        VBox formPane = new VBox(25);
        formPane.setPrefWidth(480);
        formPane.setAlignment(Pos.CENTER_LEFT);
        formPane.setPadding(new Insets(40));

        Label title = new Label("Reset Your Password");
        title.setFont(getFont(28, FontWeight.BOLD));
        title.setTextFill(Color.web(FONT_COLOR_DARK));
        VBox.setMargin(title, new Insets(0, 0, 10, 0));

        emailField = createStyledTextField("Enter your email");

        Button resetButton = new Button("Send Reset Email");
        resetButton.setFont(getFont(16, FontWeight.BOLD));
        resetButton.setTextFill(Color.web(FONT_COLOR_LIGHT));
        resetButton.setStyle("-fx-background-color: " + PRIMARY_BLUE + "; -fx-background-radius: 8; -fx-cursor: hand;");
        resetButton.setPrefHeight(50);
        resetButton.setMaxWidth(Double.MAX_VALUE);

        Hyperlink backToLoginLink = new Hyperlink("Back to Log In");
        backToLoginLink.setFont(getFont(14, FontWeight.MEDIUM));

        HBox bottomLinks = new HBox(backToLoginLink);
        HBox.setHgrow(backToLoginLink, Priority.ALWAYS);
        bottomLinks.setAlignment(Pos.CENTER);

        resetButton.setOnAction(e -> handlePasswordReset(stage, emailField.getText()));
        backToLoginLink.setOnAction(e -> handleBackToLogin(stage));

        formPane.getChildren().addAll(
            title,
            new Label("Email"), emailField,
            resetButton,
            bottomLinks
        );

        return formPane;
    }

    private TextField createStyledTextField(String prompt) {
        TextField textField = new TextField();
        textField.setPromptText(prompt);
        textField.setFont(getFont(15, FontWeight.NORMAL));
        textField.setPrefHeight(50);
        textField.setStyle("-fx-background-color: " + BACKGROUND_COLOR + "; -fx-background-radius: 8; -fx-border-color: " + BORDER_COLOR + "; -fx-border-radius: 8; -fx-border-width: 1.5;");
        return textField;
    }

    private void handlePasswordReset(Stage currentStage, String email) {
        if (email == null || email.trim().isEmpty()) {
            showAlert("Error", "Email field cannot be empty.");
            return;
        }

        boolean success = controller.sendPasswordResetEmail(email);

        if (success) {
            showAlert("Success", "Password reset email sent successfully! Please check your inbox.");
            // Reuse the existing stage
            new LoginUI().start(currentStage);
        } else {
            showAlert("Error", "Failed to send password reset email. Please try again.");
        }
    }

    private void handleBackToLogin(Stage currentStage) {
        // Reuse the existing stage
        new LoginUI().start(currentStage);
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        if (title.toLowerCase().contains("error")) {
            alert.setAlertType(Alert.AlertType.ERROR);
        }
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}