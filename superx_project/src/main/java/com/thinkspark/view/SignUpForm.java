package com.thinkspark.view;

import com.thinkspark.Controller.SignUpController;
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

public class SignUpForm extends Application {

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

    @Override
    public void start(Stage primaryStage) {
        HBox mainLayout = new HBox();
        mainLayout.setAlignment(Pos.CENTER);
        mainLayout.setMaxSize(1200, 800); // Increased max size to match LoginUI
        mainLayout.setStyle("-fx-background-color: " + CARD_BACKGROUND_COLOR + "; " +
                           "-fx-background-radius: 20; " + // Increased radius to match LoginUI
                           "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 30, 0, 0, 10);"); // Increased shadow to match LoginUI
        mainLayout.getChildren().addAll(createBrandingPane(), createSignUpForm(primaryStage));

        StackPane root = new StackPane(mainLayout);
        root.setStyle("-fx-background-color: " + BACKGROUND_COLOR + ";");
        root.setPadding(new Insets(60)); // Increased padding to match LoginUI

        Scene scene = new Scene(root,1920,1080);
        primaryStage.setTitle("Safe Serve");
        primaryStage.setScene(scene);
        primaryStage.setMaximized(true);
        primaryStage.setResizable(true);
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/logo.png"))); // Fixed image path to .png
        primaryStage.show();
    }

    private Node createBrandingPane() {
        VBox brandingPane = new VBox(30); // Increased spacing
        brandingPane.setPrefWidth(600); // Increased preferred width to match LoginUI
        brandingPane.setAlignment(Pos.CENTER_LEFT);
        brandingPane.setPadding(new Insets(60)); // Increased padding to match LoginUI
        brandingPane.setStyle("-fx-background-color: " + PRIMARY_BLUE + "; -fx-background-radius: 20 0 0 20;"); // Increased radius to match LoginUI

        // Add ImageView for the logo, similar to LoginUI
        ImageView logoView = new ImageView(new Image(getClass().getResourceAsStream("/logo.png")));
        logoView.setFitHeight(300); // Increased size to match LoginUI
        logoView.setPreserveRatio(true);

        Label logo = new Label("🚀 Join Us");
        logo.setFont(getFont(50, FontWeight.BOLD)); // Increased font size
        logo.setTextFill(Color.web(FONT_COLOR_LIGHT));

        Text welcomeText = new Text("Become a part of a community dedicated to providing and receiving help effectively during crises. Your first step to making a difference starts here.");
        welcomeText.setFont(getFont(20, FontWeight.NORMAL)); // Increased font size
        welcomeText.setFill(Color.web(FONT_COLOR_LIGHT));
        welcomeText.setOpacity(0.9);
        welcomeText.setWrappingWidth(450); // Increased wrapping width to match LoginUI

        brandingPane.getChildren().addAll(logoView, logo, welcomeText); // Added logoView here
        return brandingPane;
    }

    private Node createSignUpForm(Stage primaryStage) {
        VBox formPane = new VBox(30); // Increased spacing to match LoginUI
        formPane.setPrefWidth(600); // Increased preferred width to match LoginUI
        formPane.setAlignment(Pos.CENTER_LEFT);
        formPane.setPadding(new Insets(50)); // Increased padding to match LoginUI

        Label title = new Label("Create Your Account");
        title.setFont(getFont(34, FontWeight.BOLD)); // Increased font size to match LoginUI
        title.setTextFill(Color.web(FONT_COLOR_DARK));
        VBox.setMargin(title, new Insets(0, 0, 15, 0)); // Increased margin to match LoginUI

        TextField nameField = createStyledTextField("Full Name");
        TextField emailField = createStyledTextField("Email Address");
        PasswordField passwordField = createStyledPasswordField("Password");
        ComboBox<String> userType = createStyledComboBox(); 

        Button signUpButton = new Button("Sign Up");
        signUpButton.setFont(getFont(20, FontWeight.BOLD)); // Increased font size
        signUpButton.setTextFill(Color.web(FONT_COLOR_LIGHT));
        signUpButton.setStyle("-fx-background-color: " + PRIMARY_BLUE + "; -fx-background-radius: 10; -fx-cursor: hand;"); // Increased radius
        signUpButton.setPrefHeight(60); // Increased height to match LoginUI
        signUpButton.setMaxWidth(Double.MAX_VALUE);

        Hyperlink loginLink = new Hyperlink("Already have an account? Log In");
        loginLink.setFont(getFont(18, FontWeight.BOLD)); // Increased font size and made bold to match LoginUI
        loginLink.setTextFill(Color.web(PRIMARY_BLUE)); // Made text blue
        loginLink.setStyle("-fx-underline: true; -fx-cursor: hand;"); // Ensure underline and hand cursor

        HBox bottomLinkPane = new HBox(); // Removed spacing as it only contains one element
        bottomLinkPane.setAlignment(Pos.CENTER); // Center the link
        bottomLinkPane.getChildren().add(loginLink); // Add the link to the HBox
        VBox.setMargin(bottomLinkPane, new Insets(20, 0, 0, 0)); // Increased top margin to match LoginUI

        // Added labels above input fields for better UI/UX
        Label nameLabel = createLabel("Full Name");
        Label emailLabel = createLabel("Email Address");
        Label passwordLabel = createLabel("Password");
        Label roleLabel = createLabel("I am a...");

        nameField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                emailField.requestFocus();
            }
        });

        emailField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                passwordField.requestFocus();
            }
        });

        passwordField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                userType.requestFocus();
            }
        });

        userType.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                signUpButton.fire();
            }
        });

        signUpButton.setOnAction(e -> {
            String name = nameField.getText();
            String email = emailField.getText();
            String password = passwordField.getText();
            String role = userType.getValue();

            if (name.isEmpty() || email.isEmpty() || password.isEmpty() || role == null) {
                showAlert("Error", "Please fill in all fields.");
                return;
            }

            SignUpController signUpController = new SignUpController();
            SignUpController.Result result = signUpController.signUp(name, email, password, role);

            if (result.success) {
                showAlert("Success", "User registered successfully! Please log in.");
                // Reuse the existing primary stage
                 new LoginUI().start(primaryStage);
            } else {
                showAlert("Error", "Failed to register user. " + result.message);
            }
        });
        
        loginLink.setOnAction(e -> {
            // Reuse the existing primary stage
            new LoginUI().start(primaryStage);
        });

        formPane.getChildren().addAll(
            title,
            nameLabel, nameField,
            emailLabel, emailField,
            passwordLabel, passwordField,
            roleLabel, userType,
            signUpButton,
            bottomLinkPane
        );

        return formPane;
    }

    private Label createLabel(String text) {
        Label label = new Label(text);
        label.setFont(getFont(18, FontWeight.MEDIUM)); // Increased font size
        label.setTextFill(Color.web(FONT_COLOR_DARK));
        VBox.setMargin(label, new Insets(10, 0, 0, 0)); // Increased margin
        return label;
    }

    private TextField createStyledTextField(String prompt) {
        TextField textField = new TextField();
        textField.setPromptText(prompt);
        textField.setFont(getFont(18, FontWeight.NORMAL)); // Increased font size
        textField.setPrefHeight(60); // Set preferred height to match LoginUI text fields
        textField.setStyle("-fx-background-color: " + CARD_BACKGROUND_COLOR + "; -fx-background-radius: 10; -fx-border-color: " + BORDER_COLOR + "; -fx-border-radius: 10; -fx-border-width: 2;"); // Background color set to CARD_BACKGROUND_COLOR to match image
        return textField;
    }

    private PasswordField createStyledPasswordField(String prompt) {
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText(prompt);
        passwordField.setFont(getFont(18, FontWeight.NORMAL)); // Increased font size
        passwordField.setPrefHeight(60); // Set preferred height to match LoginUI password fields
        passwordField.setStyle("-fx-background-color: " + CARD_BACKGROUND_COLOR + "; -fx-background-radius: 10; -fx-border-color: " + BORDER_COLOR + "; -fx-border-radius: 10; -fx-border-width: 2;"); // Background color set to CARD_BACKGROUND_COLOR to match image
        return passwordField;
    }
    
    private ComboBox<String> createStyledComboBox() {
        ComboBox<String> comboBox = new ComboBox<>();
        comboBox.getItems().addAll("Citizen", "Volunteer", "NGO", "Government");
        comboBox.setPromptText("Select Role");
        comboBox.setPrefHeight(60); // Increased height
        comboBox.setMaxWidth(Double.MAX_VALUE);
        comboBox.setStyle("-fx-font-size: 18px; -fx-background-color: " + BACKGROUND_COLOR + "; -fx-border-color: " + BORDER_COLOR + "; -fx-border-radius: 10; -fx-border-width: 2;"); // Increased font size, radius and border width
        return comboBox;
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}