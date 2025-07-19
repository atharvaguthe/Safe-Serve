package com.thinkspark.view;

import com.thinkspark.Controller.SignUpController;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class SignUpForm extends Application {

    SignUpController signUpController = new SignUpController();

    @Override
    public void start(Stage primaryStage) {
        // Header Label
        Label header = new Label("Create Your Account");
        header.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        header.setTextFill(Color.WHITE);
        header.setPadding(new Insets(12, 30, 12, 30));
        header.setBackground(new Background(new BackgroundFill(Color.web("#7389A6"), new CornerRadii(20), Insets.EMPTY)));

        // Input fields
        TextField emailField = createInputField("Enter your email");
        TextField nameField = createInputField("Full name");
        PasswordField passwordField = createPasswordField("Create a password");
        PasswordField confirmPasswordField = createPasswordField("Confirm your password");

        // Role selection
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


        // Buttons
        Button signUpBtn = new Button("Sign Up");
        styleButton(signUpBtn, "#8D4C57");
        signUpBtn.setOnAction(e -> {
            String emailString = emailField.getText();
            String passwordString = passwordField.getText();
            signUpController.signUp(emailString, passwordString);
            System.out.println("button clicked");
        });

        Button loginBtn = new Button("Log In");
        styleButton(loginBtn, "#FF6B6B");

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

        signUpBtn.setOnAction(e -> {
            String emailString = emailField.getText();
            String passwordString = passwordField.getText();
            // Assuming signUp is successful
            signUpController.signUp(emailString, passwordString);
            System.out.println("button clicked");

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

        HBox buttonBox = new HBox(20, signUpBtn, loginBtn);
        buttonBox.setAlignment(Pos.CENTER);

        // Form layout
        VBox formBox = new VBox(18, header, emailField, nameField, passwordField, confirmPasswordField, selectionBox, buttonBox);
        formBox.setAlignment(Pos.CENTER);
        formBox.setPadding(new Insets(30));
        formBox.setStyle("-fx-background-color: #15233A; -fx-border-color: #00A8FF;");

        // Wrapper to center everything
        StackPane root = new StackPane(formBox);
        root.setStyle("-fx-background-color: white;");
        StackPane.setAlignment(formBox, Pos.CENTER);

        Scene scene = new Scene(root, 1200, 800);
        primaryStage.setTitle("Sign Up | ThinkSpark");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private TextField createInputField(String placeholder) {
        TextField tf = new TextField();
        tf.setPromptText(placeholder);
        tf.setPrefSize(250, 40);
        tf.setMaxWidth(400);
        tf.setFont(Font.font("Arial", FontWeight.NORMAL, 14));
        tf.setStyle("-fx-background-color: #2C3E50; -fx-text-fill: white; -fx-prompt-text-fill: lightgray;");
        tf.setBackground(new Background(new BackgroundFill(Color.web("#2C3E50"), new CornerRadii(10), Insets.EMPTY)));
        return tf;
    }

    private PasswordField createPasswordField(String placeholder) {
        PasswordField pf = new PasswordField();
        pf.setPromptText(placeholder);
        pf.setPrefSize(250, 40);
        pf.setMaxWidth(400);
        pf.setFont(Font.font("Arial", FontWeight.NORMAL, 14));
        pf.setStyle("-fx-background-color: #2C3E50; -fx-text-fill: white; -fx-prompt-text-fill: lightgray;");
        pf.setBackground(new Background(new BackgroundFill(Color.web("#2C3E50"), new CornerRadii(10), Insets.EMPTY)));
        return pf;
    }

    private void styleButton(Button btn, String color) {
        btn.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        btn.setTextFill(Color.WHITE);
        btn.setPrefSize(120, 40);
        btn.setBackground(new Background(new BackgroundFill(Color.web(color), new CornerRadii(20), Insets.EMPTY)));
    }

}
