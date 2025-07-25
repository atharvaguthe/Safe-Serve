package com.thinkspark.view;

import com.thinkspark.dao.AlertDao;
import com.thinkspark.model.DataManager;
// We use the fully qualified name for com.thinkspark.model.Alert to avoid name conflicts
import javafx.animation.PauseTransition;
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
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;

public class BroadcastAlertsView extends Application {
    // --- Style Definitions ---
    private static final String BACKGROUND_COLOR = "#F4F7FC";
    private static final String PRIMARY_BLUE = "#005A9C";
    private static final String FONT_COLOR_DARK = "#2D3748";
    private static final String FONT_COLOR_LIGHT = "#FFFFFF";
    private static final String CARD_BACKGROUND_COLOR = "#FFFFFF";
    private static final String BORDER_COLOR = "#E2E8F0";
    private static final String SUCCESS_COLOR = "#38A169";

    private final Scene previousScene;
    private final Stage primaryStage;

    private static Font getFont(double size, FontWeight weight) {
        return Font.font("System", weight, size);
    }

    public BroadcastAlertsView(Stage primaryStage, Scene previousScene) {
        this.primaryStage = primaryStage;
        this.previousScene = previousScene;
    }

    @Override
    public void start(Stage stage) {
        VBox rootLayout = new VBox(50); // Increased spacing
        rootLayout.setPadding(new Insets(60, 80, 80, 80)); // Increased padding
        rootLayout.setStyle("-fx-background-color: " + BACKGROUND_COLOR + ";");
        rootLayout.setAlignment(Pos.TOP_CENTER);

        rootLayout.getChildren().addAll(createHeader(), createBroadcastForm());

        ScrollPane scrollPane = new ScrollPane(rootLayout);
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        Scene scene = new Scene(scrollPane,1920,1080);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Safe Serve");
    }

    private Node createHeader() {
        HBox header = new HBox();
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(20, 0, 30, 0)); // Increased padding

        Label title = new Label("📢 Broadcast New Alert");
        title.setFont(getFont(42, FontWeight.BOLD)); // Increased font size
        title.setTextFill(Color.web(FONT_COLOR_DARK));

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button backButton = new Button("⬅️ Back to Dashboard");
        backButton.setFont(getFont(20, FontWeight.MEDIUM)); // Increased font size
        backButton.setTextFill(Color.web(PRIMARY_BLUE));
        backButton.setStyle("-fx-background-color: transparent; -fx-cursor: hand; -fx-border-color: " + PRIMARY_BLUE + "; -fx-border-radius: 12;"); // Increased radius
        backButton.setPadding(new Insets(15, 28, 15, 28)); // Increased padding
        backButton.setOnAction(e -> primaryStage.setScene(previousScene));

        header.getChildren().addAll(title, spacer, backButton);
        return header;
    }

    private Node createBroadcastForm() {
        VBox card = new VBox(35); // Increased spacing
        card.setPadding(new Insets(50)); // Increased padding
        card.setStyle("-fx-background-color: " + CARD_BACKGROUND_COLOR + "; " +
                      "-fx-background-radius: 20; " + // Increased radius
                      "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.06), 20, 0, 0, 8);"); // Increased shadow
        card.setMaxWidth(1000); // Increased max width

        GridPane formGrid = new GridPane();
        formGrid.setHgap(30); // Increased gap
        formGrid.setVgap(25); // Increased gap

        TextField titleField = new TextField();
        titleField.setPromptText("e.g., Evacuation Notice for Sector 5");
        styleFormControl(titleField);
        titleField.setPrefHeight(60); // Set preferred height

        TextArea messageArea = new TextArea();
        messageArea.setPromptText("Enter the full alert details here...");
        messageArea.setWrapText(true);
        messageArea.setPrefRowCount(5); // Increased row count
        styleFormControl(messageArea);
        messageArea.setPrefHeight(150); // Set preferred height

        ComboBox<String> priorityBox = new ComboBox<>();
        priorityBox.getItems().addAll("High", "Medium", "Low");
        priorityBox.setPromptText("Select Priority");
        styleFormControl(priorityBox);
        priorityBox.setPrefHeight(60); // Set preferred height

        formGrid.add(createLabel("Alert Title"), 0, 0);
        formGrid.add(titleField, 1, 0);
        formGrid.add(createLabel("Alert Message"), 0, 1);
        formGrid.add(messageArea, 1, 1);
        formGrid.add(createLabel("Priority Level"), 0, 2);
        formGrid.add(priorityBox, 1, 2);

        Button submitButton = new Button("Broadcast Alert");
        submitButton.setFont(getFont(22, FontWeight.BOLD)); // Increased font size
        submitButton.setTextFill(Color.web(FONT_COLOR_LIGHT));
        submitButton.setStyle("-fx-background-color: " + SUCCESS_COLOR + "; -fx-background-radius: 12; -fx-cursor: hand;"); // Increased radius
        submitButton.setPadding(new Insets(18, 35, 18, 35)); // Increased padding
        HBox actionBox = new HBox(submitButton);
        actionBox.setAlignment(Pos.CENTER_RIGHT);

        VBox container = new VBox(card);
        container.setAlignment(Pos.CENTER);

        submitButton.setOnAction(e -> {
            String title = titleField.getText();
            String message = messageArea.getText();
            String priority = priorityBox.getValue();

            if (title.isEmpty() || message.isEmpty() || priority == null) {
                new Alert(Alert.AlertType.ERROR, "Please fill in all fields to broadcast an alert.").showAndWait();
                return;
            }

            // 1. Create the Alert object using its fully qualified name
            com.thinkspark.model.Alert newAlert = new com.thinkspark.model.Alert(title, message, priority);

            // 2. Use the AlertDao to save the alert to the database
            AlertDao alertDao = new AlertDao();
            alertDao.saveAlert(newAlert);
            
            // 3. (Optional) Also add to the local DataManager for immediate UI updates.
            DataManager.getBroadcastAlerts().add(newAlert);

            // --- Confirmation UI ---
            VBox confirmationVBox = new VBox(30); // Increased spacing
            confirmationVBox.setAlignment(Pos.CENTER);
            confirmationVBox.setPadding(new Insets(60)); // Increased padding
            
            Label confirmationTitle = new Label("✅ Alert Sent Successfully!");
            confirmationTitle.setFont(getFont(32, FontWeight.BOLD)); // Increased font size
            
            Label confirmationText = new Label("The alert has been broadcast and saved to the database.");
            confirmationText.setFont(getFont(20, FontWeight.NORMAL)); // Increased font size
            confirmationText.setTextAlignment(TextAlignment.CENTER);
            
            confirmationVBox.getChildren().addAll(confirmationTitle, confirmationText);
            
            container.getChildren().setAll(confirmationVBox);
            
            PauseTransition delay = new PauseTransition(Duration.seconds(3));
            delay.setOnFinished(event -> primaryStage.setScene(previousScene));
            delay.play();
        });

        card.getChildren().addAll(formGrid, actionBox);
        return container;
    }

    private Label createLabel(String text) {
        Label label = new Label(text);
        label.setFont(getFont(18, FontWeight.MEDIUM)); // Increased font size
        label.setTextFill(Color.web(FONT_COLOR_DARK));
        return label;
    }

    private void styleFormControl(Control control) {
        control.setStyle("-fx-font-size: 16px; -fx-background-color: #FFFFFF; -fx-border-color: " + BORDER_COLOR + "; -fx-border-radius: 10; -fx-padding: 10;"); // Increased font size, padding, radius
        GridPane.setHgrow(control, Priority.ALWAYS); // Ensure it grows within the grid
    }
}