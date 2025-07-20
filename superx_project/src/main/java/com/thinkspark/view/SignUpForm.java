package com.thinkspark.view;

import com.thinkspark.Controller.SignUpController;
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
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class SignUpForm extends Application {

    // --- Style Definitions (Consistent with LoginUI) ---
    private static final String BACKGROUND_COLOR = "#F4F7FC";
    private static final String CARD_BACKGROUND_COLOR = "#FFFFFF";
    private static final String PRIMARY_BLUE = "#005A9C";
    private static final String FONT_COLOR_DARK = "#2D3748";
    private static final String FONT_COLOR_LIGHT = "#FFFFFF";
    private static final String BORDER_COLOR = "#E2E8F0";

    // --- Font Definitions ---
    private static Font getFont(double size, FontWeight weight) {
        return Font.font("System", weight, size);
    }

    @Override
    public void start(Stage primaryStage) {
        // --- Main Layout (Two-Pane) ---
        HBox mainLayout = new HBox();
        mainLayout.setAlignment(Pos.CENTER);
        mainLayout.setMaxSize(960, 640); // Consistent size with LoginUI
        mainLayout.setStyle("-fx-background-color: " + CARD_BACKGROUND_COLOR + "; " +
                           "-fx-background-radius: 16; " +
                           "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 20, 0, 0, 8);");

        // Add the two main panes
        mainLayout.getChildren().addAll(createBrandingPane(), createSignUpForm(primaryStage));

        // --- Root Pane ---
        StackPane root = new StackPane(mainLayout);
        root.setStyle("-fx-background-color: " + BACKGROUND_COLOR + ";");
        root.setPadding(new Insets(40));

        // --- Scene and Stage Setup ---
        Scene scene = new Scene(root, 1200, 800);
        primaryStage.setTitle("Sign Up - Disaster Relief Helper");
        primaryStage.setScene(scene);
        primaryStage.setResizable(true);
        primaryStage.show();
    }

    /**
     * Creates the left-side branding pane for the sign-up screen.
     * @return A styled VBox node for the branding pane.
     */
    private Node createBrandingPane() {
        VBox brandingPane = new VBox(20);
        brandingPane.setPrefWidth(480);
        brandingPane.setAlignment(Pos.CENTER_LEFT);
        brandingPane.setPadding(new Insets(40));
        brandingPane.setStyle("-fx-background-color: " + PRIMARY_BLUE + "; -fx-background-radius: 16 0 0 16;");

        Label logo = new Label("🚀 Join Us");
        logo.setFont(getFont(40, FontWeight.BOLD));
        logo.setTextFill(Color.web(FONT_COLOR_LIGHT));

        Text welcomeText = new Text("Become a part of a community dedicated to providing and receiving help effectively during crises. Your first step to making a difference starts here.");
        welcomeText.setFont(getFont(18, FontWeight.NORMAL));
        welcomeText.setFill(Color.web(FONT_COLOR_LIGHT));
        welcomeText.setOpacity(0.9);
        welcomeText.setWrappingWidth(380);

        brandingPane.getChildren().addAll(logo, welcomeText);
        return brandingPane;
    }

    /**
     * Creates the right-side sign-up form.
     * @param primaryStage The primary stage to handle window closing on success.
     * @return A styled VBox node for the sign-up form.
     */
    private Node createSignUpForm(Stage primaryStage) {
        VBox formPane = new VBox(15); // Adjusted spacing
        formPane.setPrefWidth(480);
        formPane.setAlignment(Pos.CENTER_LEFT);
        formPane.setPadding(new Insets(30, 40, 30, 40)); // Adjusted padding

        // --- Form Title ---
        Label title = new Label("Create Your Account");
        title.setFont(getFont(28, FontWeight.BOLD));
        title.setTextFill(Color.web(FONT_COLOR_DARK));
        VBox.setMargin(title, new Insets(0, 0, 10, 0));

        // --- Form Fields ---
        TextField nameField = createStyledTextField("Full Name");
        TextField emailField = createStyledTextField("Email Address");
        PasswordField passwordField = createStyledPasswordField("Password");
        ComboBox<String> userType = createStyledComboBox();

        // --- Sign-Up Button ---
        Button signUpButton = new Button("Sign Up");
        signUpButton.setFont(getFont(16, FontWeight.BOLD));
        signUpButton.setTextFill(Color.web(FONT_COLOR_LIGHT));
        signUpButton.setStyle("-fx-background-color: " + PRIMARY_BLUE + "; -fx-background-radius: 8; -fx-cursor: hand;");
        signUpButton.setPrefHeight(50);
        signUpButton.setMaxWidth(Double.MAX_VALUE);

        // --- Bottom Link ---
        Hyperlink loginLink = new Hyperlink("Already have an account? Log In");
        loginLink.setFont(getFont(14, FontWeight.MEDIUM));
        HBox bottomLinkPane = new HBox(loginLink);
        bottomLinkPane.setAlignment(Pos.CENTER);
        VBox.setMargin(bottomLinkPane, new Insets(10, 0, 0, 0));


        // --- Event Handlers ---
        signUpButton.setOnAction(e -> {
            String name = nameField.getText();
            String email = emailField.getText();
            String password = passwordField.getText();
            String role = userType.getValue();

            // *** START: UNCHANGED BACKEND LOGIC ***
            if (name.isEmpty() || email.isEmpty() || password.isEmpty() || role == null) {
                showAlert("Error", "Please fill in all fields.");
                return;
            }

            SignUpController signUpController = new SignUpController();
            SignUpController.Result result = signUpController.signUp(name, email, password, role);

            if (result.success) {
                showAlert("Success", "User registered successfully! Please log in.");
                primaryStage.close();
                 // This part can be uncommented if you want to open the login window after sign up
                 // new LoginUI().start(new Stage());
            } else {
                showAlert("Error", "Failed to register user. " + result.message);
            }
            // *** END: UNCHANGED BACKEND LOGIC ***
        });
        
        loginLink.setOnAction(e -> {
            primaryStage.close();
            new LoginUI().start(new Stage());
        });


        formPane.getChildren().addAll(
            title,
            createLabel("Full Name"), nameField,
            createLabel("Email"), emailField,
            createLabel("Password"), passwordField,
            createLabel("I am a..."), userType,
            signUpButton,
            bottomLinkPane
        );

        return formPane;
    }

    // --- Helper methods for creating styled form elements ---

    private Label createLabel(String text) {
        Label label = new Label(text);
        label.setFont(getFont(14, FontWeight.MEDIUM));
        label.setTextFill(Color.web(FONT_COLOR_DARK));
        VBox.setMargin(label, new Insets(5, 0, 0, 0));
        return label;
    }

    private TextField createStyledTextField(String prompt) {
        TextField textField = new TextField();
        textField.setPromptText(prompt);
        textField.setFont(getFont(15, FontWeight.NORMAL));
        textField.setPrefHeight(50);
        textField.setStyle("-fx-background-color: " + BACKGROUND_COLOR + "; -fx-background-radius: 8; -fx-border-color: " + BORDER_COLOR + "; -fx-border-radius: 8; -fx-border-width: 1.5;");
        return textField;
    }

    private PasswordField createStyledPasswordField(String prompt) {
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText(prompt);
        passwordField.setFont(getFont(15, FontWeight.NORMAL));
        passwordField.setPrefHeight(50);
        passwordField.setStyle("-fx-background-color: " + BACKGROUND_COLOR + "; -fx-background-radius: 8; -fx-border-color: " + BORDER_COLOR + "; -fx-border-radius: 8; -fx-border-width: 1.5;");
        return passwordField;
    }

    private ComboBox<String> createStyledComboBox() {
        ComboBox<String> comboBox = new ComboBox<>();
        comboBox.getItems().addAll("Citizen", "Volunteer", "NGO Representative", "Government Official");
        comboBox.setPromptText("Select Role");
        comboBox.setPrefHeight(50);
        comboBox.setMaxWidth(Double.MAX_VALUE);
        comboBox.setStyle("-fx-font-size: 15px; -fx-background-color: " + BACKGROUND_COLOR + "; -fx-border-color: " + BORDER_COLOR + "; -fx-border-radius: 8; -fx-border-width: 1.5;");
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