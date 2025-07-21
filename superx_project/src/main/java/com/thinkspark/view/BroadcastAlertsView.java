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
        VBox rootLayout = new VBox(30);
        rootLayout.setPadding(new Insets(40));
        rootLayout.setStyle("-fx-background-color: " + BACKGROUND_COLOR + ";");
        rootLayout.setAlignment(Pos.TOP_CENTER);

        rootLayout.getChildren().addAll(createHeader(), createBroadcastForm());

        ScrollPane scrollPane = new ScrollPane(rootLayout);
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        Scene scene = new Scene(scrollPane, 1280, 900);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Broadcast Alert | Government Dashboard");
    }

    private Node createHeader() {
        HBox header = new HBox();
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(0, 0, 20, 0));

        Label title = new Label("📢 Broadcast New Alert");
        title.setFont(getFont(32, FontWeight.BOLD));
        title.setTextFill(Color.web(FONT_COLOR_DARK));

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button backButton = new Button("⬅️ Back to Dashboard");
        backButton.setFont(getFont(16, FontWeight.MEDIUM));
        backButton.setTextFill(Color.web(PRIMARY_BLUE));
        backButton.setStyle("-fx-background-color: transparent; -fx-cursor: hand; -fx-border-color: " + PRIMARY_BLUE + "; -fx-border-radius: 8;");
        backButton.setPadding(new Insets(8, 16, 8, 16));
        backButton.setOnAction(e -> primaryStage.setScene(previousScene));

        header.getChildren().addAll(title, spacer, backButton);
        return header;
    }

    private Node createBroadcastForm() {
        VBox card = new VBox(25);
        card.setPadding(new Insets(30));
        card.setStyle("-fx-background-color: " + CARD_BACKGROUND_COLOR + "; " +
                      "-fx-background-radius: 12; " +
                      "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.05), 10, 0, 0, 4);");
        card.setMaxWidth(800);

        GridPane formGrid = new GridPane();
        formGrid.setHgap(20);
        formGrid.setVgap(15);

        TextField titleField = new TextField();
        titleField.setPromptText("e.g., Evacuation Notice for Sector 5");
        styleFormControl(titleField);

        TextArea messageArea = new TextArea();
        messageArea.setPromptText("Enter the full alert details here...");
        messageArea.setWrapText(true);
        messageArea.setPrefRowCount(6);
        styleFormControl(messageArea);

        ComboBox<String> priorityBox = new ComboBox<>();
        priorityBox.getItems().addAll("High", "Medium", "Low");
        priorityBox.setPromptText("Select Priority");
        styleFormControl(priorityBox);

        formGrid.add(createLabel("Alert Title"), 0, 0);
        formGrid.add(titleField, 1, 0);
        formGrid.add(createLabel("Alert Message"), 0, 1);
        formGrid.add(messageArea, 1, 1);
        formGrid.add(createLabel("Priority Level"), 0, 2);
        formGrid.add(priorityBox, 1, 2);

        Button submitButton = new Button("Broadcast Alert");
        submitButton.setFont(getFont(18, FontWeight.BOLD));
        submitButton.setTextFill(Color.web(FONT_COLOR_LIGHT));
        submitButton.setStyle("-fx-background-color: " + SUCCESS_COLOR + "; -fx-background-radius: 8; -fx-cursor: hand;");
        submitButton.setPadding(new Insets(12, 25, 12, 25));
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
            VBox confirmationVBox = new VBox(20);
            confirmationVBox.setAlignment(Pos.CENTER);
            confirmationVBox.setPadding(new Insets(40));
            
            Label confirmationTitle = new Label("✅ Alert Sent Successfully!");
            confirmationTitle.setFont(getFont(24, FontWeight.BOLD));
            
            Label confirmationText = new Label("The alert has been broadcast and saved to the database.");
            confirmationText.setFont(getFont(16, FontWeight.NORMAL));
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
        label.setFont(getFont(16, FontWeight.MEDIUM));
        label.setTextFill(Color.web(FONT_COLOR_DARK));
        return label;
    }

    private void styleFormControl(Control control) {
        control.setStyle("-fx-font-size: 14px; -fx-background-color: #FFFFFF; -fx-border-color: " + BORDER_COLOR + "; -fx-border-radius: 8; -fx-padding: 8;");
    }
}
