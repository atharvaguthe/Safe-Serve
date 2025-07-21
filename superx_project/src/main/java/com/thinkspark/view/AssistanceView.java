package com.thinkspark.view;

import com.thinkspark.model.DataManager;
import com.thinkspark.model.Disaster;
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

public class AssistanceView extends Application {

    // --- Style Definitions ---
    private static final String BACKGROUND_COLOR = "#F4F7FC";
    private static final String PRIMARY_BLUE = "#005A9C";
    private static final String FONT_COLOR_DARK = "#2D3748";
    private static final String FONT_COLOR_LIGHT = "#FFFFFF";
    private static final String CARD_BACKGROUND_COLOR = "#FFFFFF";
    private static final String BORDER_COLOR = "#E2E8F0";
    private static final String SUCCESS_COLOR = "#48BB78";

    private final Scene previousScene;
    private final Stage primaryStage;

    private static Font getFont(double size, FontWeight weight) {
        return Font.font("System", weight, size);
    }

    public AssistanceView(Stage primaryStage, Scene previousScene) {
        this.primaryStage = primaryStage;
        this.previousScene = previousScene;
    }

    @Override
    public void start(Stage stage) {
        VBox rootLayout = new VBox(30);
        rootLayout.setPadding(new Insets(40));
        rootLayout.setStyle("-fx-background-color: " + BACKGROUND_COLOR + ";");
        rootLayout.setAlignment(Pos.TOP_CENTER);

        Node header = createHeader();
        Node assistanceForm = createAssistanceForm();

        rootLayout.getChildren().addAll(header, assistanceForm);

        ScrollPane scrollPane = new ScrollPane(rootLayout);
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        Scene scene = new Scene(scrollPane, 1280, 900);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Request Assistance | Disaster Relief Helper");
    }

    private Node createHeader() {
        HBox header = new HBox();
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(0, 0, 20, 0));

        Label title = new Label("🤝 Request Assistance");
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

    private Node createAssistanceForm() {
        VBox card = new VBox(25);
        card.setPadding(new Insets(30));
        card.setStyle("-fx-background-color: " + CARD_BACKGROUND_COLOR + "; " +
                      "-fx-background-radius: 12; " +
                      "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.05), 10, 0, 0, 4);");
        card.setMaxWidth(800);

        GridPane formGrid = new GridPane();
        formGrid.setHgap(20);
        formGrid.setVgap(15);

        // --- Form Fields ---
        ComboBox<String> assistanceType = new ComboBox<>();
        assistanceType.getItems().addAll("Medical", "Food & Water", "Shelter", "Rescue", "Other");
        assistanceType.setPromptText("Select a type of help needed");
        styleFormControl(assistanceType);

        Spinner<Integer> peopleSpinner = new Spinner<>(1, 50, 1);
        styleFormControl(peopleSpinner);

        TextField locationField = createTextField("e.g., Near City Park, 456 Oak Ave");
        TextArea detailsArea = new TextArea();
        detailsArea.setPromptText("Describe your situation, e.g., 'Trapped on rooftop', 'Need baby formula'...");
        detailsArea.setWrapText(true);
        detailsArea.setPrefRowCount(4);
        styleFormControl(detailsArea);

        TextField contactField = createTextField("A number where you can be reached");

        formGrid.add(createLabel("Type of Assistance"), 0, 0);
        formGrid.add(assistanceType, 1, 0);
        formGrid.add(createLabel("Number of People"), 0, 1);
        formGrid.add(peopleSpinner, 1, 1);
        formGrid.add(createLabel("Current Location"), 0, 2);
        formGrid.add(locationField, 1, 2);
        formGrid.add(createLabel("Additional Details"), 0, 3);
        formGrid.add(detailsArea, 1, 3);
        formGrid.add(createLabel("Contact Number"), 0, 4);
        formGrid.add(contactField, 1, 4);

        // --- Form Actions ---
        Button submitButton = new Button("Submit Request");
        submitButton.setFont(getFont(18, FontWeight.BOLD));
        submitButton.setTextFill(Color.web(FONT_COLOR_LIGHT));
        submitButton.setStyle("-fx-background-color: " + SUCCESS_COLOR + "; -fx-background-radius: 8; -fx-cursor: hand;");
        submitButton.setPadding(new Insets(12, 25, 12, 25));
        HBox actionBox = new HBox(submitButton);
        actionBox.setAlignment(Pos.CENTER_RIGHT);

        VBox container = new VBox(card);
        container.setAlignment(Pos.CENTER);

        submitButton.setOnAction(e -> {
            // --- Data Submission Logic ---
            String type = assistanceType.getValue();
            String location = locationField.getText();
            String details = detailsArea.getText();
            Integer peopleCount = peopleSpinner.getValue();

            if (type == null || location.isEmpty() || details.isEmpty()) {
                new Alert(Alert.AlertType.ERROR, "Please fill in all required fields.").showAndWait();
                return;
            }

            String fullDescription = "Assistance for " + peopleCount + " person(s). Details: " + details;
            
            // Add the request as a new Disaster to the central DataManager
            DataManager.getDisasterReports().add(new Disaster(type, location, fullDescription));

            // --- Confirmation UI ---
            VBox confirmationVBox = new VBox(20);
            confirmationVBox.setAlignment(Pos.CENTER);
            confirmationVBox.setPadding(new Insets(40));
            
            Label confirmationTitle = new Label("✅ Request Sent!");
            confirmationTitle.setFont(getFont(24, FontWeight.BOLD));
            confirmationTitle.setTextFill(Color.web(FONT_COLOR_DARK));
            
            Label confirmationText = new Label("Your request for assistance has been received. Help is on the way. You will be redirected to the dashboard.");
            confirmationText.setFont(getFont(16, FontWeight.NORMAL));
            confirmationText.setTextFill(Color.web(FONT_COLOR_DARK));
            confirmationText.setWrapText(true);
            confirmationText.setTextAlignment(TextAlignment.CENTER);
            
            confirmationVBox.getChildren().addAll(confirmationTitle, confirmationText);
            
            container.getChildren().setAll(confirmationVBox);
            
            PauseTransition delay = new PauseTransition(Duration.seconds(4));
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

    private TextField createTextField(String prompt) {
        TextField textField = new TextField();
        textField.setPromptText(prompt);
        styleFormControl(textField);
        return textField;
    }

    private void styleFormControl(Control control) {
        control.setStyle("-fx-font-size: 14px; -fx-background-color: #FFFFFF; -fx-border-color: " + BORDER_COLOR + "; -fx-border-radius: 8; -fx-padding: 8;");
    }
}
