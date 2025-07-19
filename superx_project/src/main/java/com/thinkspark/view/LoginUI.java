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
// These classes are placeholders to allow the LoginUI to compile and run.
// In a real application, these would be fully implemented UI classes.
class SignUpForm extends Application { @Override public void start(Stage stage) { stage.show(); } }
class CitizenDashboard extends Application { @Override public void start(Stage stage) { stage.show(); } }
class VolunteerDashboard extends Application { @Override public void start(Stage stage) { stage.show(); } }
class NgoDashboard extends Application { @Override public void start(Stage stage) { stage.show(); } }
class GovDashboard extends Application { @Override public void start(Stage stage) { stage.show(); } }


public class LoginUI extends Application {

    @Override
    public void start(Stage stage) {
        // Login form layout with a clean white background
        VBox loginBox = new VBox(20); // Increased spacing for better visual separation
        loginBox.setPadding(new Insets(40)); // Increased padding
        loginBox.setAlignment(Pos.CENTER);
        loginBox.setStyle("-fx-background-color: #FFFFFF;");
        loginBox.setMaxWidth(500); // Set a max width for the login form
        loginBox.setMaxHeight(600); // Set a max height
        
        // Center the loginBox in the scene
        VBox root = new VBox(loginBox);
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: #F0F0F0;"); // A light gray background for the whole scene

        // Title
        Label title = new Label("Log In");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 32)); // Larger, bolder font for the title
        title.setTextFill(Color.web("#005A9C")); // Use the primary professional blue

        // Email
        TextField email = new TextField();
        email.setPromptText("Email");
        // Style text fields for the light theme
        email.setStyle("-fx-font-size: 16px; -fx-background-radius: 8; -fx-border-color: #DDDDDD; -fx-border-radius: 8; -fx-padding: 10;");
        email.setMaxWidth(350);

        // Password
        PasswordField password = new PasswordField();
        password.setPromptText("Password");
        // Apply the same style as the email field
        password.setStyle("-fx-font-size: 16px; -fx-background-radius: 8; -fx-border-color: #DDDDDD; -fx-border-radius: 8; -fx-padding: 10;");
        password.setMaxWidth(350);

        // Role Selection
        Text roleLabel = new Text("Log in as:");
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

        // Buttons
        Button loginBtn = new Button("Log In");
        // Style login button with the primary blue color
        loginBtn.setStyle("-fx-background-color: #007BFF; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 20; -fx-padding: 12 25 12 25; -fx-font-size: 16px;");
        loginBtn.setPrefWidth(350);

        Button signUpBtn = new Button("Don't have an account? Sign Up");
        // Use a flat, text-like style for the sign-up button to make it a secondary action
        signUpBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: #007BFF; -fx-font-weight: bold; -fx-font-size: 14px;");

        Label forgotPassword = new Label("Forgot Password?");
        forgotPassword.setTextFill(Color.web("#007BFF")); // Use the primary blue for links
        forgotPassword.setFont(Font.font("Arial", 12));
        forgotPassword.setStyle("-fx-font-weight: bold;");


        // --- Event Handlers ---

        loginBtn.setOnAction(e -> {
            String emailString = email.getText();
            String passwordString = password.getText();
            String selectedRole = roleComboBox.getValue();

            // For this example, we'll navigate based on the selected role
            if (selectedRole != null) {
                Stage currentStage = (Stage) loginBtn.getScene().getWindow();
                currentStage.close(); // Close the login window

                Stage dashboardStage = new Stage();
                Application dashboard = null;

                switch (selectedRole) {
                    case "Citizen":
                        dashboard = new CitizenDashboard();
                        break;
                    case "Volunteer":
                        dashboard = new VolunteerDashboard();
                        break;
                    case "NGO":
                        dashboard = new NgoDashboard();
                        break;
                    case "Government":
                        dashboard = new GovDashboard();
                        break;
                }

                if (dashboard != null) {
                    try {
                        dashboard.start(dashboardStage);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            } else {
                // Handle case where no role is selected
                System.out.println("Please select a role.");
                // You could show an alert here in a real app
            }
        });

        signUpBtn.setOnAction(e -> {
            Stage currentStage = (Stage) signUpBtn.getScene().getWindow();
            currentStage.close();

            Stage signUpStage = new Stage();
            SignUpForm signUpForm = new SignUpForm();
            try {
                signUpForm.start(signUpStage);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        // Add components to the login box
        loginBox.getChildren().addAll(title, email, password, selectionRow, loginBtn, forgotPassword, signUpBtn);

        // Create scene
        Scene scene = new Scene(root, 1200, 800);

        stage.setTitle("Login - Disaster Relief Helper");
        stage.setScene(scene);
        stage.setResizable(true);
        stage.show();
    }
}
