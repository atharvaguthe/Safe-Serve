package com.thinkspark.view;

import com.thinkspark.Controller.LoginController;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class LoginUI extends Application {

    private static final String BACKGROUND_COLOR = "#F4F7FC";
    private static final String CARD_BACKGROUND_COLOR = "#FFFFFF";
    private static final String PRIMARY_BLUE = "#005A9C";
    private static final String FONT_COLOR_DARK = "#2D3748";
    private static final String FONT_COLOR_LIGHT = "#FFFFFF";
    private static final String BORDER_COLOR = "#E2E8F0";

    private static Font getFont(double size, FontWeight weight) {
        return Font.font("System", weight, size);
    }

    LoginController loginController = new LoginController();

    @Override
    public void start(Stage stage) {
        HBox mainLayout = new HBox();
        mainLayout.setAlignment(Pos.CENTER);
        mainLayout.setMaxSize(960, 640);
        mainLayout.setStyle("-fx-background-color: " + CARD_BACKGROUND_COLOR + "; " +
                "-fx-background-radius: 16; " +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 20, 0, 0, 8);");

        mainLayout.getChildren().addAll(createBrandingPane(), createLoginForm(stage));

        StackPane root = new StackPane(mainLayout);
        root.setStyle("-fx-background-color: " + BACKGROUND_COLOR + ";");
        root.setPadding(new Insets(40));

        Scene scene = new Scene(root, 1200, 800);
        stage.setTitle("Login - Disaster Relief Helper");
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

        Label logo = new Label("🏢 Helper");
        logo.setFont(getFont(40, FontWeight.BOLD));
        logo.setTextFill(Color.web(FONT_COLOR_LIGHT));

        Text welcomeText = new Text("A unified platform for citizens, volunteers, NGOs, and government agencies to collaborate during crises.");
        welcomeText.setFont(getFont(18, FontWeight.NORMAL));
        welcomeText.setFill(Color.web(FONT_COLOR_LIGHT));
        welcomeText.setOpacity(0.9);
        welcomeText.setWrappingWidth(380);

        brandingPane.getChildren().addAll(logo, welcomeText);
        return brandingPane;
    }

    private Node createLoginForm(Stage stage) {
        VBox formPane = new VBox(25);
        formPane.setPrefWidth(480);
        formPane.setAlignment(Pos.CENTER_LEFT);
        formPane.setPadding(new Insets(40));

        Label title = new Label("Log In to Your Account");
        title.setFont(getFont(28, FontWeight.BOLD));
        title.setTextFill(Color.web(FONT_COLOR_DARK));
        VBox.setMargin(title, new Insets(0, 0, 10, 0));

        TextField emailField = createStyledTextField("Email");
        PasswordField passwordField = createStyledPasswordField("Password");
        ComboBox<String> roleComboBox = createStyledComboBox();

        Button loginBtn = new Button("Log In");
        loginBtn.setFont(getFont(16, FontWeight.BOLD));
        loginBtn.setTextFill(Color.web(FONT_COLOR_LIGHT));
        loginBtn.setStyle("-fx-background-color: " + PRIMARY_BLUE + "; -fx-background-radius: 8; -fx-cursor: hand;");
        loginBtn.setPrefHeight(50);
        loginBtn.setMaxWidth(Double.MAX_VALUE);

        Hyperlink forgotPasswordLink = new Hyperlink("Forgot Password?");
        forgotPasswordLink.setFont(getFont(14, FontWeight.MEDIUM));

        Hyperlink signUpLink = new Hyperlink("Don't have an account? Sign Up");
        signUpLink.setFont(getFont(14, FontWeight.MEDIUM));

        HBox bottomLinks = new HBox(forgotPasswordLink, signUpLink);
        HBox.setHgrow(forgotPasswordLink, Priority.ALWAYS);
        bottomLinks.setAlignment(Pos.CENTER);

        // --- Enter Key Navigation ---
        emailField.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                passwordField.requestFocus();
            }
        });

        passwordField.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                roleComboBox.requestFocus();
            }
        });

        roleComboBox.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                loginBtn.fire();
            }
        });

        loginBtn.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                loginBtn.fire();
            }
        });

        loginBtn.setOnAction(e -> handleLogin(stage, roleComboBox.getValue()));
        signUpLink.setOnAction(e -> handleSignUp(stage));

        formPane.getChildren().addAll(
                title,
                new Label("Email"), emailField,
                new Label("Password"), passwordField,
                new Label("Log in as"), roleComboBox,
                loginBtn,
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
        comboBox.getItems().addAll("Citizen", "Volunteer", "NGO", "Government");
        comboBox.setPromptText("Select your role");
        comboBox.setPrefHeight(50);
        comboBox.setMaxWidth(Double.MAX_VALUE);
        comboBox.setStyle("-fx-font-size: 15px; -fx-background-color: " + BACKGROUND_COLOR + "; -fx-border-color: " + BORDER_COLOR + "; -fx-border-radius: 8; -fx-border-width: 1.5;");
        return comboBox;
    }

    private void handleLogin(Stage currentStage, String selectedRole) {
        if (selectedRole == null) {
            System.out.println("Please select a role.");
            return;
        }

        currentStage.close();
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
    }

    private void handleSignUp(Stage currentStage) {
        currentStage.close();
        Stage signUpStage = new Stage();
        new SignUpForm().start(signUpStage);
    }
}
