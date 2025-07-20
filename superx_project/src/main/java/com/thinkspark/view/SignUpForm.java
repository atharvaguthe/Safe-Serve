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

// --- Placeholder Classes for Compilation ---
// In a real application, these would be fully implemented.
class SignUpController {
    public void signUp(String email, String password) {
        System.out.println("Signing up with Email: " + email);
    }
}

// This placeholder uses the previously defined LoginUI for navigation.
// Ensure the LoginUI class is accessible in your project.

public class SignUpForm extends Application {

    SignUpController signUpController = new SignUpController();

    @Override
    public void start(Stage primaryStage) {
        // Main form layout with a clean white background
        VBox formBox = new VBox(20); // Increased spacing
        formBox.setPadding(new Insets(40));
        formBox.setAlignment(Pos.CENTER);
        formBox.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 15;");
        formBox.setMaxWidth(500); // Set a max width for the form
        formBox.setMaxHeight(650); // Set a max height

        // Center the formBox in the scene
        VBox root = new VBox(formBox);
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: #F0F0F0;"); // Light gray background for the scene

        // Header Label
        Label header = new Label("Create Your Account");
        header.setFont(Font.font("Arial", FontWeight.BOLD, 32));
        // Use the primary professional blue for the header
        header.setTextFill(Color.web("#005A9C"));

        // --- Input Fields ---
        // Styled to match the light theme of LoginUI
        String fieldStyle = "-fx-font-size: 16px; -fx-background-radius: 8; -fx-border-color: #DDDDDD; -fx-border-radius: 8; -fx-padding: 10;";
        
        TextField emailField = new TextField();
        emailField.setPromptText("Enter your email");
        emailField.setStyle(fieldStyle);
        emailField.setMaxWidth(350);
        
        TextField nameField = new TextField();
        nameField.setPromptText("Full name");
        nameField.setStyle(fieldStyle);
        nameField.setMaxWidth(350);

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Create a password");
        passwordField.setStyle(fieldStyle);
        passwordField.setMaxWidth(350);

        PasswordField confirmPasswordField = new PasswordField();
        confirmPasswordField.setPromptText("Confirm your password");
        confirmPasswordField.setStyle(fieldStyle);
        confirmPasswordField.setMaxWidth(350);

        // --- Role Selection ---
        Text roleLabel = new Text("Register as:");
        roleLabel.setFill(Color.BLACK);
        roleLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 16));

        ComboBox<String> roleComboBox = new ComboBox<>();
        roleComboBox.getItems().addAll("Citizen", "Volunteer", "NGO", "Government");
        roleComboBox.setPromptText("Select your role");
        roleComboBox.setMaxWidth(350);
        // Style the combo box to match the text fields
        roleComboBox.setStyle("-fx-font-size: 16px; -fx-background-color: #FFFFFF; -fx-border-color: #DDDDDD; -fx-border-radius: 8;");

        HBox selectionRow = new HBox(10, roleLabel, roleComboBox);
        selectionRow.setAlignment(Pos.CENTER_LEFT);
        selectionRow.setMaxWidth(350);

        // --- Buttons ---
        Button signUpBtn = new Button("Sign Up");
        // Style the primary sign-up button
        signUpBtn.setStyle("-fx-background-color: #007BFF; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 20; -fx-padding: 12 25 12 25; -fx-font-size: 16px;");
        signUpBtn.setPrefWidth(350);

        Button loginBtn = new Button("Already have an account? Log In");
        // Use a flat, text-like style for the secondary login button
        loginBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: #007BFF; -fx-font-weight: bold; -fx-font-size: 14px;");

        // --- Event Handlers ---
        signUpBtn.setOnAction(e -> {
            String emailString = emailField.getText();
            String passwordString = passwordField.getText();
            // Basic validation: check if passwords match
            if (!passwordString.equals(confirmPasswordField.getText())) {
                System.out.println("Passwords do not match.");
                // In a real app, show an alert to the user
                return;
            }
            signUpController.signUp(emailString, passwordString);
            System.out.println("Sign Up button clicked and successful.");

            // Navigate to LoginUI after successful sign-up
            Stage currentStage = (Stage) signUpBtn.getScene().getWindow();
            currentStage.close();

            Stage loginStage = new Stage();
            LoginUI loginUI = new LoginUI();
            try {
                loginUI.start(loginStage);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        loginBtn.setOnAction(e -> {
            Stage currentStage = (Stage) loginBtn.getScene().getWindow();
            currentStage.close();

            Stage loginStage = new Stage();
            LoginUI loginUI = new LoginUI();
            try {
                loginUI.start(loginStage);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        // Add components to the form layout
        formBox.getChildren().addAll(header, nameField, emailField, passwordField, confirmPasswordField, selectionRow, signUpBtn, loginBtn);

        // --- Scene and Stage Setup ---
        Scene scene = new Scene(root, 1200, 800);
        primaryStage.setTitle("Sign Up | Disaster Relief Helper");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
