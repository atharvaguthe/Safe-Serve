package com.thinkspark.view;

import com.thinkspark.Controller.LoginController;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class LoginUI extends Application {

    // --- Style Definitions ---
    private static final String BACKGROUND_COLOR = "#F4F7FC";
    private static final String CARD_BACKGROUND_COLOR = "#FFFFFF";
    private static final String PRIMARY_BLUE = "#005A9C";
    private static final String FONT_COLOR_DARK = "#2D3748";
    private static final String FONT_COLOR_LIGHT = "#FFFFFF";
    private static final String BORDER_COLOR = "#E2E8F0";

    private static Font getFont(double size, FontWeight weight) {
        return Font.font("System", weight, size);
    }

    private final LoginController loginController = new LoginController();
    private TextField emailField;
    private PasswordField passwordField;
    private ComboBox<String> roleComboBox;

    @Override
    public void start(Stage stage) {
        HBox mainLayout = new HBox();
        mainLayout.setAlignment(Pos.CENTER);
        mainLayout.setMaxSize(1200, 800); // Increased max size
        mainLayout.setStyle("-fx-background-color: " + CARD_BACKGROUND_COLOR + "; " +
                           "-fx-background-radius: 20; " + // Increased radius
                           "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 30, 0, 0, 10);"); // Increased shadow
        mainLayout.getChildren().addAll(createBrandingPane(), createLoginForm(stage));

        StackPane root = new StackPane(mainLayout);
        root.setStyle("-fx-background-color: " + BACKGROUND_COLOR + ";");
        root.setPadding(new Insets(60)); // Increased padding

        Scene scene = new Scene(root,1980,1080); 
        stage.setTitle("Safe Serve");
        stage.setResizable(true);
        stage.setScene(scene);
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/logo.png")));
        stage.show();
    }

    private Node createBrandingPane() {
    VBox brandingPane = new VBox(30); // Increased spacing
    brandingPane.setPrefWidth(600); // Increased preferred width
    brandingPane.setAlignment(Pos.CENTER_LEFT);
    brandingPane.setPadding(new Insets(60)); // Increased padding
    brandingPane.setStyle("-fx-background-color: " + PRIMARY_BLUE + "; -fx-background-radius: 20 0 0 20;"); // Increased radius

    // Add ImageView for the logo
    ImageView logoView = new ImageView(new Image(getClass().getResourceAsStream("/logo.png")));
    logoView.setFitHeight(300); // Increased size
    logoView.setPreserveRatio(true);

    Label logo = new Label("Safe Serve");
    logo.setFont(getFont(50, FontWeight.BOLD)); // Increased font size
    logo.setTextFill(Color.web(FONT_COLOR_LIGHT));

    Text welcomeText = new Text("A unified platform for citizens, volunteers, NGOs, and government agencies to collaborate during crises.");
    welcomeText.setFont(getFont(20, FontWeight.NORMAL)); // Increased font size
    welcomeText.setFill(Color.web(FONT_COLOR_LIGHT));
    welcomeText.setOpacity(0.9);
    welcomeText.setWrappingWidth(450); // Increased wrapping width

    // Add logoView before the "Safe Serve" label (which is assigned to 'logo')
    brandingPane.getChildren().addAll(logoView, logo, welcomeText);
    return brandingPane;
    }

    private Node createLoginForm(Stage stage) {
        VBox formPane = new VBox(30); // Increased spacing
        formPane.setPrefWidth(600); // Increased preferred width
        formPane.setAlignment(Pos.CENTER_LEFT);
        formPane.setPadding(new Insets(50)); // Increased padding

        Label title = new Label("Log In to Your Account");
        title.setFont(getFont(34, FontWeight.BOLD)); // Increased font size
        title.setTextFill(Color.web(FONT_COLOR_DARK));
        VBox.setMargin(title, new Insets(0, 0, 15, 0)); // Increased margin

        emailField = createStyledTextField("Enter your email");
        passwordField = createStyledPasswordField("Enter your password");
        roleComboBox = createStyledComboBox();

        Label emailLabel = new Label("Email"); // Added label for email
        emailLabel.setFont(getFont(18, FontWeight.MEDIUM)); // Set font for label
        emailLabel.setTextFill(Color.web(FONT_COLOR_DARK)); // Set color for label

        Label passwordLabel = new Label("Password"); // Added label for password
        passwordLabel.setFont(getFont(18, FontWeight.MEDIUM)); // Set font for label
        passwordLabel.setTextFill(Color.web(FONT_COLOR_DARK)); // Set color for label

        Label roleLabel = new Label("Log in as"); // Added label for role
        roleLabel.setFont(getFont(18, FontWeight.MEDIUM)); // Set font for label
        roleLabel.setTextFill(Color.web(FONT_COLOR_DARK)); // Set color for label


        Button loginBtn = new Button("Log In");
        loginBtn.setFont(getFont(20, FontWeight.BOLD)); // Increased font size
        loginBtn.setTextFill(Color.web(FONT_COLOR_LIGHT));
        loginBtn.setStyle("-fx-background-color: " + PRIMARY_BLUE + "; -fx-background-radius: 10; -fx-cursor: hand;"); // Increased radius
        loginBtn.setPrefHeight(60); // Increased height
        loginBtn.setMaxWidth(Double.MAX_VALUE);

        Hyperlink forgotPasswordLink = new Hyperlink("Forgot Password?");
        forgotPasswordLink.setFont(getFont(16, FontWeight.BOLD)); // Increased font size and made bold
        forgotPasswordLink.setTextFill(Color.web(PRIMARY_BLUE)); // Made text blue
        forgotPasswordLink.setStyle("-fx-underline: true; -fx-cursor: hand;"); // Ensure underline and hand cursor
        forgotPasswordLink.setOnAction(e -> handleForgotPassword(stage));

        Hyperlink signUpLink = new Hyperlink("Don't have an account? Sign Up");
        signUpLink.setFont(getFont(16, FontWeight.BOLD)); // Increased font size and made bold
        signUpLink.setTextFill(Color.web(PRIMARY_BLUE)); // Made text blue
        signUpLink.setStyle("-fx-underline: true; -fx-cursor: hand;"); // Ensure underline and hand cursor

        HBox bottomLinks = new HBox(40); // Increased spacing
        HBox.setHgrow(forgotPasswordLink, Priority.ALWAYS);
        bottomLinks.setAlignment(Pos.CENTER);
        bottomLinks.getChildren().addAll(forgotPasswordLink, signUpLink); // Ensure both links are added

        // Event handlers for Enter key
        emailField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                passwordField.requestFocus();
            }
        });

        passwordField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                roleComboBox.requestFocus();
            }
        });
        
        roleComboBox.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                loginBtn.fire();
            }
        });


        loginBtn.setOnAction(e -> handleLogin(stage, emailField.getText(), passwordField.getText(), roleComboBox.getValue()));
        signUpLink.setOnAction(e -> handleSignUp(stage));

        formPane.getChildren().addAll(
            title,
            emailLabel, emailField, // Added labels
            passwordLabel, passwordField, // Added labels
            roleLabel, roleComboBox, // Added labels
            loginBtn,
            bottomLinks
        );

        return formPane;
    }

    private TextField createStyledTextField(String prompt) {
        TextField textField = new TextField();
        textField.setPromptText(prompt);
        textField.setFont(getFont(18, FontWeight.NORMAL)); // Increased font size
        textField.setPrefHeight(60); // Increased height
        textField.setStyle("-fx-background-color: " + BACKGROUND_COLOR + "; -fx-background-radius: 10; -fx-border-color: " + BORDER_COLOR + "; -fx-border-radius: 10; -fx-border-width: 2;"); // Increased radius and border width
        return textField;
    }

    private PasswordField createStyledPasswordField(String prompt) {
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText(prompt);
        passwordField.setFont(getFont(18, FontWeight.NORMAL)); // Increased font size
        passwordField.setPrefHeight(60); // Increased height
        passwordField.setStyle("-fx-background-color: " + BACKGROUND_COLOR + "; -fx-background-radius: 10; -fx-border-color: " + BORDER_COLOR + "; -fx-border-radius: 10; -fx-border-width: 2;"); // Increased radius and border width
        return passwordField;
    }

    private ComboBox<String> createStyledComboBox() {
        ComboBox<String> comboBox = new ComboBox<>();
        comboBox.getItems().addAll("Citizen", "Volunteer", "NGO", "Government");
        comboBox.setPromptText("Select your role");
        comboBox.setPrefHeight(60); // Increased height
        comboBox.setMaxWidth(Double.MAX_VALUE);
        comboBox.setStyle("-fx-font-size: 18px; -fx-background-color: " + BACKGROUND_COLOR + "; -fx-border-color: " + BORDER_COLOR + "; -fx-border-radius: 10; -fx-border-width: 2;"); // Increased font size, radius and border width
        return comboBox;
    }

    private void handleLogin(Stage currentStage, String email, String password, String selectedRole) {
        if (selectedRole == null || selectedRole.isEmpty()) {
            showAlert("Login Error", "Please select a role to continue.");
            return;
        }
        if (email == null || email.trim().isEmpty() || password == null || password.isEmpty()) {
            showAlert("Login Error", "Email and password fields cannot be empty.");
            return;
        }

        LoginController.Result result = loginController.login(email, password, selectedRole);

        if (result.success) {
            // No longer closing the current stage, just reusing it
            Application dashboard = null;

            switch (selectedRole) {
                case "Citizen":
                    dashboard = new CitizenDashboard(email);
                    break;
                case "Volunteer":
                    dashboard = new VolunteerDashboard(email);
                    break;
                case "NGO":
                    dashboard = new NgoDashboard(email);
                    break;
                case "Government":
                    dashboard = new GovDashboard(email);
                    break;
            }

            if (dashboard != null) {
                try {
                    dashboard.start(currentStage); // Use the existing stage
                } catch (Exception ex) {
                    ex.printStackTrace();
                    showAlert("Application Error", "Could not open the dashboard.");
                }
            } else {
                showAlert("Login Successful", "Welcome! The dashboard for '" + selectedRole + "' is not available.");
            }
        } else {
            showAlert("Login Failed", result.message);
        }
    }

    private void handleSignUp(Stage currentStage) {
        // No longer closing the current stage, just reusing it
        new SignUpForm().start(currentStage); // Use the existing stage
    }
    
    private void handleForgotPassword(Stage currentStage) {
        // No longer closing the current stage, just reusing it
        new ForgotPasswordUI().start(currentStage); // Use the existing stage
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        if (title.toLowerCase().contains("error") || title.toLowerCase().contains("failed")) {
            alert.setAlertType(Alert.AlertType.ERROR);
        }
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}