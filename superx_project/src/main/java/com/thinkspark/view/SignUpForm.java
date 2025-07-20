package com.thinkspark.view;

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

    // --- Style Definitions (Consistent Light Theme) ---
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
    public void start(Stage stage) {
        // --- Main Layout ---
        HBox mainLayout = new HBox();
        mainLayout.setAlignment(Pos.CENTER);
        mainLayout.setMaxSize(960, 720); // Slightly taller to accommodate more fields
        mainLayout.setStyle("-fx-background-color: " + CARD_BACKGROUND_COLOR + "; " +
                           "-fx-background-radius: 16; " +
                           "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 20, 0, 0, 8);");

        // Add the two main panes to the layout
        mainLayout.getChildren().addAll(createBrandingPane(), createSignUpForm(stage));

        // --- Root Pane ---
        StackPane root = new StackPane(mainLayout);
        root.setStyle("-fx-background-color: " + BACKGROUND_COLOR + ";");
        root.setPadding(new Insets(40));

        // --- Scene and Stage Setup ---
        Scene scene = new Scene(root, 1200, 900);
        stage.setTitle("Sign Up - Disaster Relief Helper");
        stage.setScene(scene);
        stage.setResizable(true);
        stage.show();
    }

    /**
     * Creates the left-side branding pane with a title and message.
     * @return A styled VBox node for the branding pane.
     */
    private Node createBrandingPane() {
        VBox brandingPane = new VBox(20);
        brandingPane.setPrefWidth(480);
        brandingPane.setAlignment(Pos.CENTER_LEFT);
        brandingPane.setPadding(new Insets(40));
        brandingPane.setStyle("-fx-background-color: " + PRIMARY_BLUE + "; -fx-background-radius: 16 0 0 16;");

        Label logo = new Label("🏢 Helper");
        logo.setFont(getFont(40, FontWeight.BOLD));
        logo.setTextFill(Color.web(FONT_COLOR_LIGHT));

        Text welcomeText = new Text("Join a community of responders. Create your account to start making a difference.");
        welcomeText.setFont(getFont(18, FontWeight.NORMAL));
        welcomeText.setFill(Color.web(FONT_COLOR_LIGHT));
        welcomeText.setOpacity(0.9);
        welcomeText.setWrappingWidth(380);

        brandingPane.getChildren().addAll(logo, welcomeText);
        return brandingPane;
    }

    /**
     * Creates the right-side sign-up form.
     * @param stage The primary stage to handle window switching.
     * @return A styled VBox node for the sign-up form.
     */
    private Node createSignUpForm(Stage stage) {
        VBox formPane = new VBox(15); // Reduced spacing for a tighter form
        formPane.setPrefWidth(480);
        formPane.setAlignment(Pos.CENTER_LEFT);
        formPane.setPadding(new Insets(40));

        // --- Form Title ---
        Label title = new Label("Create Your Account");
        title.setFont(getFont(28, FontWeight.BOLD));
        title.setTextFill(Color.web(FONT_COLOR_DARK));
        VBox.setMargin(title, new Insets(0, 0, 10, 0));

        // --- Form Fields ---
        TextField nameField = createStyledTextField("Full Name");
        TextField emailField = createStyledTextField("Email");
        PasswordField passwordField = createStyledPasswordField("Create Password");
        PasswordField confirmPasswordField = createStyledPasswordField("Confirm Password");
        ComboBox<String> roleComboBox = createStyledComboBox();
        
        // --- Sign Up Button ---
        Button signUpBtn = new Button("Create Account");
        signUpBtn.setFont(getFont(16, FontWeight.BOLD));
        signUpBtn.setTextFill(Color.web(FONT_COLOR_LIGHT));
        signUpBtn.setStyle("-fx-background-color: " + PRIMARY_BLUE + "; -fx-background-radius: 8; -fx-cursor: hand;");
        signUpBtn.setPrefHeight(50);
        signUpBtn.setMaxWidth(Double.MAX_VALUE);

        // --- Bottom Link ---
        Hyperlink loginLink = new Hyperlink("Already have an account? Log In");
        loginLink.setFont(getFont(14, FontWeight.MEDIUM));
        HBox bottomLinkBox = new HBox(loginLink);
        bottomLinkBox.setAlignment(Pos.CENTER);
        
        // --- Event Handlers ---
        signUpBtn.setOnAction(e -> handleSignUp(stage, passwordField.getText(), confirmPasswordField.getText()));
        loginLink.setOnAction(e -> handleLoginRedirect(stage));

        formPane.getChildren().addAll(
            title,
            new Label("Full Name"), nameField,
            new Label("Email"), emailField,
            new Label("Password"), passwordField,
            new Label("Confirm Password"), confirmPasswordField,
            new Label("Register as"), roleComboBox,
            signUpBtn,
            bottomLinkBox
        );

        return formPane;
    }

    // --- Helper methods for creating styled form elements ---

    private TextField createStyledTextField(String prompt) {
        TextField textField = new TextField();
        textField.setPromptText(prompt);
        textField.setFont(getFont(15, FontWeight.NORMAL));
        textField.setPrefHeight(45);
        textField.setStyle("-fx-background-color: " + BACKGROUND_COLOR + "; -fx-background-radius: 8; -fx-border-color: " + BORDER_COLOR + "; -fx-border-radius: 8; -fx-border-width: 1.5;");
        return textField;
    }

    private PasswordField createStyledPasswordField(String prompt) {
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText(prompt);
        passwordField.setFont(getFont(15, FontWeight.NORMAL));
        passwordField.setPrefHeight(45);
        passwordField.setStyle("-fx-background-color: " + BACKGROUND_COLOR + "; -fx-background-radius: 8; -fx-border-color: " + BORDER_COLOR + "; -fx-border-radius: 8; -fx-border-width: 1.5;");
        return passwordField;
    }

    private ComboBox<String> createStyledComboBox() {
        ComboBox<String> comboBox = new ComboBox<>();
        comboBox.getItems().addAll("Citizen", "Volunteer", "NGO", "Government");
        comboBox.setPromptText("Select your role");
        comboBox.setPrefHeight(45);
        comboBox.setMaxWidth(Double.MAX_VALUE);
        comboBox.setStyle("-fx-font-size: 15px; -fx-background-color: " + BACKGROUND_COLOR + "; -fx-border-color: " + BORDER_COLOR + "; -fx-border-radius: 8; -fx-border-width: 1.5;");
        return comboBox;
    }

    // --- Event Handling Logic ---

    private void handleSignUp(Stage currentStage, String password, String confirmPassword) {
        if (!password.equals(confirmPassword)) {
            // In a real app, show an alert dialog.
            System.out.println("Passwords do not match.");
            return;
        }
        
        // Placeholder for actual sign-up logic
        System.out.println("Sign-up successful! Redirecting to login...");
        
        // Redirect to login after successful sign-up
        handleLoginRedirect(currentStage);
    }

    private void handleLoginRedirect(Stage currentStage) {
        currentStage.close();
        Stage loginStage = new Stage();
        // This launches the redesigned login UI.
        new LoginUI().start(loginStage);
    }

    
}
