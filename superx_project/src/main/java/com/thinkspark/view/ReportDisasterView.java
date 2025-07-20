package com.thinkspark.view;

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
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;

public class ReportDisasterView extends Application {

    // --- Style Definitions (consistent with CitizenDashboard) ---
    private static final String BACKGROUND_COLOR = "#F4F7FC";
    private static final String PRIMARY_BLUE = "#005A9C";
    private static final String FONT_COLOR_DARK = "#2D3748";
    private static final String FONT_COLOR_LIGHT = "#FFFFFF";
    private static final String CARD_BACKGROUND_COLOR = "#FFFFFF";
    private static final String BORDER_COLOR = "#E2E8F0";
    private static final String SUCCESS_COLOR = "#48BB78";

    private final Scene previousScene;
    private final Stage primaryStage;
    private File selectedFile; // To store the selected file

    // --- Font Helper ---
    private static Font getFont(double size, FontWeight weight) {
        return Font.font("System", weight, size);
    }

    /**
     * Constructor to receive the previous scene for back navigation.
     * @param primaryStage The main application stage.
     * @param previousScene The scene of the CitizenDashboard.
     */
    public ReportDisasterView(Stage primaryStage, Scene previousScene) {
        this.primaryStage = primaryStage;
        this.previousScene = previousScene;
    }


    @Override
    public void start(Stage stage) {
        // The primary stage is already passed in the constructor, so we use it.
        VBox rootLayout = new VBox(30);
        rootLayout.setPadding(new Insets(40));
        rootLayout.setStyle("-fx-background-color: " + BACKGROUND_COLOR + ";");
        rootLayout.setAlignment(Pos.TOP_CENTER);

        // --- Header ---
        Node header = createHeader();

        // --- Form ---
        Node form = createDisasterReportForm();

        rootLayout.getChildren().addAll(header, form);

        ScrollPane scrollPane = new ScrollPane(rootLayout);
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        Scene scene = new Scene(scrollPane, 1280, 900);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Report a Disaster | Disaster Relief Helper");
    }

    /**
     * Creates the header with a title and a back button.
     * @return A styled HBox node for the header.
     */
    private Node createHeader() {
        HBox header = new HBox();
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(0, 0, 20, 0));

        Label title = new Label("🚨 Report a New Disaster");
        title.setFont(getFont(32, FontWeight.BOLD));
        title.setTextFill(Color.web(FONT_COLOR_DARK));

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button backButton = new Button("⬅️ Back to Dashboard");
        backButton.setFont(getFont(16, FontWeight.MEDIUM));
        backButton.setTextFill(Color.web(PRIMARY_BLUE));
        backButton.setStyle("-fx-background-color: transparent; -fx-cursor: hand; -fx-border-color: " + PRIMARY_BLUE + "; -fx-border-radius: 8;");
        backButton.setPadding(new Insets(8, 16, 8, 16));

        // Event handler to return to the dashboard
        backButton.setOnAction(e -> {
            primaryStage.setScene(previousScene);
            primaryStage.setTitle("Citizen Dashboard | Disaster Relief Helper");
        });


        header.getChildren().addAll(title, spacer, backButton);
        return header;
    }

    /**
     * Creates the main form layout inside a styled card.
     * @return A VBox node containing the form.
     */
    private Node createDisasterReportForm() {
        VBox card = new VBox(25);
        card.setPadding(new Insets(30));
        card.setStyle("-fx-background-color: " + CARD_BACKGROUND_COLOR + "; " +
                      "-fx-background-radius: 12; " +
                      "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.05), 10, 0, 0, 4);");
        card.setMaxWidth(800); // Set a max width for the form card for better readability

        GridPane formGrid = new GridPane();
        formGrid.setHgap(20);
        formGrid.setVgap(15);

        // --- Form Fields ---
        // Disaster Type
        formGrid.add(createLabel("Type of Disaster"), 0, 0);
        ComboBox<String> disasterType = new ComboBox<>();
        disasterType.getItems().addAll("Flood", "Earthquake", "Wildfire", "Hurricane", "Power Outage", "Other");
        disasterType.setPromptText("Select a type");
        disasterType.setMaxWidth(Double.MAX_VALUE);
        styleFormControl(disasterType);
        formGrid.add(disasterType, 1, 0);

        // Location
        formGrid.add(createLabel("Location / Address"), 0, 1);
        TextField locationField = createTextField("e.g., City Hall, 123 Main St");
        formGrid.add(locationField, 1, 1);

        // Description
        formGrid.add(createLabel("Description"), 0, 2);
        TextArea descriptionArea = new TextArea();
        descriptionArea.setPromptText("Provide a brief description of the situation...");
        descriptionArea.setWrapText(true);
        descriptionArea.setPrefRowCount(4);
        styleFormControl(descriptionArea);
        formGrid.add(descriptionArea, 1, 2);

        // Urgency Level
        formGrid.add(createLabel("Urgency Level"), 0, 3);
        ToggleGroup urgencyGroup = new ToggleGroup();
        HBox urgencyBox = new HBox(10);
        urgencyBox.getChildren().addAll(
            createToggleButton("Low", urgencyGroup),
            createToggleButton("Medium", urgencyGroup),
            createToggleButton("High", urgencyGroup)
        );
        formGrid.add(urgencyBox, 1, 3);

        // Contact Information
        formGrid.add(createLabel("Contact Number (Optional)"), 0, 4);
        TextField contactField = createTextField("Your phone number");
        formGrid.add(contactField, 1, 4);

        // --- MODIFIED: Photo Upload ---
        formGrid.add(createLabel("Upload Photo (Optional)"), 0, 5);
        Label fileNameLabel = new Label("No file selected.");
        fileNameLabel.setFont(getFont(14, FontWeight.NORMAL));
        Button chooseFileButton = new Button("Choose File...");
        chooseFileButton.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Select a Photo");
            fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif")
            );
            selectedFile = fileChooser.showOpenDialog(primaryStage);
            if (selectedFile != null) {
                fileNameLabel.setText(selectedFile.getName());
            } else {
                fileNameLabel.setText("No file selected.");
            }
        });
        HBox fileUploadBox = new HBox(10, chooseFileButton, fileNameLabel);
        fileUploadBox.setAlignment(Pos.CENTER_LEFT);
        formGrid.add(fileUploadBox, 1, 5);


        // --- Form Actions ---
        HBox actionBox = new HBox(20);
        actionBox.setAlignment(Pos.CENTER_RIGHT);
        actionBox.setPadding(new Insets(20, 0, 0, 0));

        Button submitButton = new Button("Submit Report");
        submitButton.setFont(getFont(18, FontWeight.BOLD));
        submitButton.setTextFill(Color.web(FONT_COLOR_LIGHT));
        submitButton.setStyle("-fx-background-color: " + SUCCESS_COLOR + "; -fx-background-radius: 8; -fx-cursor: hand;");
        submitButton.setPadding(new Insets(12, 25, 12, 25));

        VBox container = new VBox(card);
        container.setAlignment(Pos.CENTER);

        submitButton.setOnAction(e -> {
            // --- MODIFIED: Show confirmation message ---
            System.out.println("Disaster Report Submitted!");
            if (selectedFile != null) {
                System.out.println("File to upload: " + selectedFile.getAbsolutePath());
            }
            
            VBox confirmationVBox = new VBox(20);
            confirmationVBox.setAlignment(Pos.CENTER);
            confirmationVBox.setPadding(new Insets(40));
            
            Label confirmationTitle = new Label("✅ Submission Received!");
            confirmationTitle.setFont(getFont(24, FontWeight.BOLD));
            confirmationTitle.setTextFill(Color.web(FONT_COLOR_DARK));
            
            Label confirmationText = new Label("Thank you for your report. Your contribution is valuable for a faster response. You will now be redirected to the dashboard.");
            confirmationText.setFont(getFont(16, FontWeight.NORMAL));
            confirmationText.setTextFill(Color.web(FONT_COLOR_DARK));
            confirmationText.setWrapText(true);
            confirmationText.setTextAlignment(TextAlignment.CENTER);
            
            confirmationVBox.getChildren().addAll(confirmationTitle, confirmationText);
            
            // Replace form with confirmation message
            container.getChildren().setAll(confirmationVBox);
            
            // Pause for a few seconds and then go back
            PauseTransition delay = new PauseTransition(Duration.seconds(4));
            delay.setOnFinished(event -> {
                // Find the back button and fire its action
                 Button backButton = (Button) ((HBox) ((VBox) container.getParent()).getChildren().get(0)).getChildren().get(2);
                 backButton.fire();
            });
            delay.play();
        });

        actionBox.getChildren().add(submitButton);

        card.getChildren().addAll(formGrid, actionBox);
        
        return container;
    }

    // --- Helper methods for creating styled form elements ---

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
    
    private ToggleButton createToggleButton(String text, ToggleGroup group) {
        ToggleButton button = new ToggleButton(text);
        button.setToggleGroup(group);
        button.setFont(getFont(14, FontWeight.NORMAL));
        button.setStyle("-fx-background-color: " + BACKGROUND_COLOR + "; -fx-border-color: " + BORDER_COLOR + "; -fx-border-radius: 5; -fx-cursor: hand;");
        button.selectedProperty().addListener((obs, wasSelected, isSelected) -> {
            if (isSelected) {
                button.setStyle("-fx-background-color: " + PRIMARY_BLUE + "; -fx-text-fill: " + FONT_COLOR_LIGHT + "; -fx-border-color: " + PRIMARY_BLUE + "; -fx-border-radius: 5;");
            } else {
                button.setStyle("-fx-background-color: " + BACKGROUND_COLOR + "; -fx-text-fill: " + FONT_COLOR_DARK + "; -fx-border-color: " + BORDER_COLOR + "; -fx-border-radius: 5;");
            }
        });
        return button;
    }

    private void styleFormControl(Control control) {
        control.setStyle("-fx-font-size: 14px; -fx-background-color: #FFFFFF; -fx-border-color: " + BORDER_COLOR + "; -fx-border-radius: 8; -fx-padding: 8;");
    }

    public static void main(String[] args) {
        // This main method is for standalone testing if needed.
        // In the actual application, this view will be launched from the dashboard.
        Application.launch(args);
    }

}
