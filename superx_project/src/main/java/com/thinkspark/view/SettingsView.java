package com.thinkspark.view;

import com.thinkspark.Controller.DeleteAccountController;
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

import java.util.Optional;

public class SettingsView extends Application {

    // --- Style Definitions ---
    private static final String BACKGROUND_COLOR = "#F4F7FC";
    private static final String PRIMARY_BLUE = "#005A9C";
    private static final String SECONDARY_ACCENT_COLOR = "#48BB78";
    private static final String FONT_COLOR_DARK = "#2D3748";
    private static final String CARD_BACKGROUND_COLOR = "#FFFFFF";
    private static final String BORDER_COLOR = "#E2E8F0";
    private static final String ALERT_COLOR = "#E53E3E";

    private final Scene previousScene;
    private final Stage primaryStage;
    private final String userEmail; // To store the current user's email

    private final ForgotPasswordController forgotPasswordController = new ForgotPasswordController();
    private final DeleteAccountController deleteAccountController = new DeleteAccountController();


    // --- Font Helper ---
    private static Font getFont(double size, FontWeight weight) {
        return Font.font("System", weight, size);
    }

    public SettingsView(Stage primaryStage, Scene previousScene, String userEmail) {
        this.primaryStage = primaryStage;
        this.previousScene = previousScene;
        this.userEmail = userEmail;
    }

    @Override
    public void start(Stage stage) {
        VBox rootLayout = new VBox(30);
        rootLayout.setPadding(new Insets(40));
        rootLayout.setStyle("-fx-background-color: " + BACKGROUND_COLOR + ";");
        rootLayout.setAlignment(Pos.TOP_CENTER);

        Node header = createHeader();
        Node settingsContent = createSettingsContent();

        rootLayout.getChildren().addAll(header, settingsContent);

        ScrollPane scrollPane = new ScrollPane(rootLayout);
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        Scene scene = new Scene(scrollPane, 1280, 900);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Settings | Disaster Relief Helper");
    }

    private Node createHeader() {
        HBox header = new HBox();
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(0, 0, 20, 0));

        Label title = new Label("⚙️ Settings");
        title.setFont(getFont(32, FontWeight.BOLD));
        title.setTextFill(Color.web(FONT_COLOR_DARK));

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button backButton = new Button("⬅️ Back to Dashboard");
        backButton.setFont(getFont(16, FontWeight.MEDIUM));
        backButton.setTextFill(Color.web(PRIMARY_BLUE));
        backButton.setStyle("-fx-background-color: transparent; -fx-cursor: hand; -fx-border-color: " + PRIMARY_BLUE + "; -fx-border-radius: 8;");
        backButton.setPadding(new Insets(8, 16, 8, 16));
        backButton.setOnAction(e -> {
            primaryStage.setScene(previousScene);
            // You might want to restore the previous title here if it's dynamic
        });

        header.getChildren().addAll(title, spacer, backButton);
        return header;
    }

    private Node createSettingsContent() {
        VBox card = new VBox(25);
        card.setPadding(new Insets(40));
        card.setStyle("-fx-background-color: " + CARD_BACKGROUND_COLOR + "; " +
                      "-fx-background-radius: 12; " +
                      "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.05), 10, 0, 0, 4);");
        card.setMaxWidth(900);

        // Notification Settings
        Label notificationLabel = new Label("Notifications");
        notificationLabel.setFont(getFont(22, FontWeight.BOLD));
        notificationLabel.setTextFill(Color.web(PRIMARY_BLUE));
        
        card.getChildren().add(notificationLabel);
        card.getChildren().add(createSettingItem("Email Notifications", "Receive updates via email.", true));
        card.getChildren().add(createSettingItem("Push Notifications", "Get alerts on your mobile device.", true));
        card.getChildren().add(createSettingItem("SMS Alerts", "Receive critical alerts via text message.", false));

        // Account Settings
        Label accountLabel = new Label("Account");
        accountLabel.setFont(getFont(22, FontWeight.BOLD));
        accountLabel.setTextFill(Color.web(PRIMARY_BLUE));
        accountLabel.setPadding(new Insets(20, 0, 0, 0));
        
        Button changePasswordButton = new Button("Change Password");
        changePasswordButton.setFont(getFont(14, FontWeight.MEDIUM));
        changePasswordButton.setOnAction(e -> handleChangePassword());

        Button deleteAccountButton = new Button("Delete Account");
        deleteAccountButton.setFont(getFont(14, FontWeight.MEDIUM));
        deleteAccountButton.setTextFill(Color.web(ALERT_COLOR));
        deleteAccountButton.setOnAction(e -> handleDeleteAccount());


        HBox accountButtons = new HBox(20, changePasswordButton, deleteAccountButton);
        
        card.getChildren().addAll(accountLabel, accountButtons);

        VBox container = new VBox(card);
        container.setAlignment(Pos.CENTER);
        
        return container;
    }
    
    private void handleChangePassword() {
        boolean success = forgotPasswordController.sendPasswordResetEmail(this.userEmail);
        if (success) {
            showAlert("Success", "A password reset link has been sent to " + this.userEmail + ". Please check your inbox.");
        } else {
            showAlert("Error", "Could not send password reset email. Please try again later.");
        }
    }

    private void handleDeleteAccount() {
        Alert confirmationDialog = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationDialog.setTitle("Delete Account");
        confirmationDialog.setHeaderText("Are you sure you want to permanently delete your account?");
        confirmationDialog.setContentText("This action cannot be undone.");

        Optional<ButtonType> result = confirmationDialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            boolean success = deleteAccountController.deleteAccount(this.userEmail);
            if (success) {
                showAlert("Account Deleted", "Your account has been successfully deleted.");
                primaryStage.close();
                new LoginUI().start(new Stage());
            } else {
                showAlert("Error", "Failed to delete account. Please try again later.");
            }
        }
    }


    private Node createSettingItem(String title, String description, boolean selected) {
        HBox settingRow = new HBox();
        settingRow.setAlignment(Pos.CENTER_LEFT);

        VBox textContainer = new VBox(2);
        Label titleLabel = new Label(title);
        titleLabel.setFont(getFont(16, FontWeight.BOLD));
        Label descriptionLabel = new Label(description);
        descriptionLabel.setFont(getFont(14, FontWeight.NORMAL));
        descriptionLabel.setOpacity(0.7);
        textContainer.getChildren().addAll(titleLabel, descriptionLabel);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        ToggleButton toggle = new ToggleButton(selected ? "ON" : "OFF");
        toggle.setSelected(selected);
        updateToggleStyle(toggle);
        toggle.setOnAction(e -> {
            toggle.setText(toggle.isSelected() ? "ON" : "OFF");
            updateToggleStyle(toggle);
        });

        settingRow.getChildren().addAll(textContainer, spacer, toggle);
        settingRow.setPadding(new Insets(15,0,15,0));
        settingRow.setStyle("-fx-border-color: transparent transparent " + BORDER_COLOR + " transparent; -fx-border-width: 1;");

        return settingRow;
    }
    
    private void updateToggleStyle(ToggleButton toggle) {
        if (toggle.isSelected()) {
            toggle.setStyle("-fx-background-color: " + SECONDARY_ACCENT_COLOR + "; -fx-text-fill: white; -fx-background-radius: 15; -fx-padding: 5 15;");
        } else {
            toggle.setStyle("-fx-background-color: " + BORDER_COLOR + "; -fx-text-fill: " + FONT_COLOR_DARK + "; -fx-background-radius: 15; -fx-padding: 5 15;");
        }
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