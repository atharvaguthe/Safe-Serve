package com.thinkspark.view;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class LoginUI extends Application {

    @Override
    public void start(Stage stage) {
        // Login form layout
        VBox loginBox = new VBox(15);
        loginBox.setPadding(new Insets(30));
        loginBox.setAlignment(Pos.CENTER);
        loginBox.setStyle("-fx-background-color: #1C263C;");

        // Title
        Label title = new Label("Log In");
        title.setFont(Font.font("Arial", 20));
        title.setTextFill(Color.WHITE);
        title.setStyle("-fx-background-color: #7189A6; -fx-background-radius: 20; -fx-padding: 10 30 10 30;");

        // Email
        TextField email = new TextField();
        email.setPromptText("Email:-");
        email.setStyle("-fx-background-color: #2D3B56; -fx-text-fill: white; -fx-prompt-text-fill: #cccccc;");
        email.setMaxWidth(300);

        // Password
        PasswordField password = new PasswordField();
        password.setPromptText("Password:-");
        password.setStyle("-fx-background-color: #2D3B56; -fx-text-fill: white; -fx-prompt-text-fill: #cccccc;");
        password.setMaxWidth(300);

        // Buttons
        Button loginBtn = new Button("log in");
        loginBtn.setStyle("-fx-background-color: #F76C6C; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 20; -fx-padding: 8 20 8 20;");

        Button signUpBtn = new Button("Sign Up");
        signUpBtn.setStyle("-fx-background-color: #8B3E47; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 20; -fx-padding: 8 20 8 20;");

        Label forgotPassword = new Label("Forget Password");
        forgotPassword.setTextFill(Color.WHITE);
        forgotPassword.setFont(Font.font("Arial", 10));

        Text roleLabel = new Text("Register as:");
        roleLabel.setFill(Color.WHITE);
        roleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));

        ComboBox<String> roleComboBox = new ComboBox<>();
        roleComboBox.getItems().addAll("Citizen", "Volunteer", "NGO", "Government");
        roleComboBox.setPromptText("Select your role");

        roleComboBox.setOnAction(e -> {
            String selected = roleComboBox.getValue();
            System.out.println("Selected role: " + selected);
        });

        HBox selectionRow = new HBox(10, roleLabel, roleComboBox);
        selectionRow.setAlignment(Pos.CENTER);

        VBox selectionBox = new VBox(selectionRow);
        selectionBox.setAlignment(Pos.CENTER);

        // Add components
        loginBox.getChildren().addAll(title, email, password,selectionBox, loginBtn, signUpBtn, forgotPassword);

        // Create scene with exact width and height
        Scene scene = new Scene(loginBox,1200,800);

        stage.setTitle("Login");
        stage.setScene(scene);
        stage.setResizable(true);
        stage.sizeToScene(); // Forces window to resize to content
        stage.centerOnScreen(); // Optional: center on screen
        stage.show();
    }

}
