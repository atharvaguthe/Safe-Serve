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
import javafx.stage.Stage;
import javafx.util.Duration;

public class DonationsView extends Application {

    // --- Style Definitions (consistent with other views) ---
    private static final String BACKGROUND_COLOR = "#F4F7FC";
    private static final String PRIMARY_BLUE = "#005A9C";
    private static final String FONT_COLOR_DARK = "#2D3748";
    private static final String FONT_COLOR_LIGHT = "#FFFFFF";
    private static final String CARD_BACKGROUND_COLOR = "#FFFFFF";
    private static final String BORDER_COLOR = "#E2E8F0";
    private static final String SUCCESS_COLOR = "#48BB78";

    private final Scene previousScene;
    private final Stage primaryStage;
    private final ToggleGroup amountGroup = new ToggleGroup();

    // --- Font Helper ---
    private static Font getFont(double size, FontWeight weight) {
        return Font.font("System", weight, size);
    }

    /**
     * Constructor to receive the previous scene for back navigation.
     * @param primaryStage The main application stage.
     * @param previousScene The scene of the CitizenDashboard.
     */
    public DonationsView(Stage primaryStage, Scene previousScene) {
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
        Node donationForm = createDonationForm();

        rootLayout.getChildren().addAll(header, donationForm);

        ScrollPane scrollPane = new ScrollPane(rootLayout);
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        Scene scene = new Scene(scrollPane, 1280, 900);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Make a Donation | Disaster Relief Helper");
    }

    private Node createHeader() {
        HBox header = new HBox();
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(0, 0, 20, 0));

        Label title = new Label("💖 Make a Donation");
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
            primaryStage.setTitle("Citizen Dashboard | Disaster Relief Helper");
        });

        header.getChildren().addAll(title, spacer, backButton);
        return header;
    }

    private Node createDonationForm() {
        VBox card = new VBox(25);
        card.setPadding(new Insets(30));
        card.setStyle("-fx-background-color: " + CARD_BACKGROUND_COLOR + "; " +
                      "-fx-background-radius: 12; " +
                      "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.05), 10, 0, 0, 4);");
        card.setMaxWidth(800);

        // --- Monetary Donation Section ---
        Label monetaryTitle = new Label("Monetary Donation");
        monetaryTitle.setFont(getFont(20, FontWeight.BOLD));

        GridPane formGrid = new GridPane();
        formGrid.setHgap(20);
        formGrid.setVgap(15);

        // Donation Amount
        formGrid.add(createLabel("Select Amount ($)"), 0, 0);
        HBox amountBox = new HBox(10);
        amountBox.getChildren().addAll(
            createAmountButton("25"),
            createAmountButton("50"),
            createAmountButton("100"),
            createAmountButton("250")
        );
        formGrid.add(amountBox, 1, 0);

        // Custom Amount
        formGrid.add(createLabel("Or Custom Amount"), 0, 1);
        TextField customAmountField = createTextField("e.g., 75");
        customAmountField.textProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null && !newVal.isEmpty()) {
                amountGroup.selectToggle(null); // Deselect preset buttons
            }
        });
        formGrid.add(customAmountField, 1, 1);
        
        // --- Payment Details Section (Mock) ---
        Label paymentTitle = new Label("Payment Details");
        paymentTitle.setFont(getFont(20, FontWeight.BOLD));
        
        GridPane paymentGrid = new GridPane();
        paymentGrid.setHgap(20);
        paymentGrid.setVgap(15);
        
        paymentGrid.add(createLabel("Card Number"), 0, 0);
        paymentGrid.add(createTextField("•••• •••• •••• ••••"), 1, 0);
        
        paymentGrid.add(createLabel("Cardholder Name"), 0, 1);
        paymentGrid.add(createTextField("John Doe"), 1, 1);
        
        HBox expiryAndCvvBox = new HBox(20);
        
        VBox expiryBox = new VBox(5);
        expiryBox.getChildren().addAll(createLabel("Expiry Date"), createTextField("MM/YY"));
        
        VBox cvvBox = new VBox(5);
        cvvBox.getChildren().addAll(createLabel("CVV"), createTextField("•••"));
        
        expiryAndCvvBox.getChildren().addAll(expiryBox, cvvBox);
        paymentGrid.add(expiryAndCvvBox, 1, 2);


        // --- Submission Button ---
        Button donateButton = new Button("Donate Now");
        donateButton.setFont(getFont(18, FontWeight.BOLD));
        donateButton.setTextFill(Color.web(FONT_COLOR_LIGHT));
        donateButton.setStyle("-fx-background-color: " + SUCCESS_COLOR + "; -fx-background-radius: 8; -fx-cursor: hand;");
        donateButton.setPadding(new Insets(12, 25, 12, 25));
        HBox actionBox = new HBox(donateButton);
        actionBox.setAlignment(Pos.CENTER_RIGHT);
        
        VBox container = new VBox(card);
        container.setAlignment(Pos.CENTER);

        donateButton.setOnAction(e -> {
            System.out.println("Donation Submitted!");
            
            VBox confirmationVBox = new VBox(20);
            confirmationVBox.setAlignment(Pos.CENTER);
            confirmationVBox.setPadding(new Insets(40));
            
            Label confirmationTitle = new Label("💖 Thank You for Your Generosity!");
            confirmationTitle.setFont(getFont(24, FontWeight.BOLD));
            confirmationTitle.setTextFill(Color.web(FONT_COLOR_DARK));
            
            Label confirmationText = new Label("Your donation will make a significant impact. We are grateful for your support. You will be redirected shortly.");
            confirmationText.setFont(getFont(16, FontWeight.NORMAL));
            confirmationText.setTextFill(Color.web(FONT_COLOR_DARK));
            confirmationText.setWrapText(true);
            confirmationText.setTextAlignment(TextAlignment.CENTER);
            
            confirmationVBox.getChildren().addAll(confirmationTitle, confirmationText);
            
            container.getChildren().setAll(confirmationVBox);
            
            PauseTransition delay = new PauseTransition(Duration.seconds(4));
            delay.setOnFinished(event -> {
                Button backButton = (Button) ((HBox) ((VBox) container.getParent()).getChildren().get(0)).getChildren().get(2);
                backButton.fire();
            });
            delay.play();
        });

        card.getChildren().addAll(monetaryTitle, formGrid, paymentTitle, paymentGrid, actionBox);
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

    private ToggleButton createAmountButton(String text) {
        ToggleButton button = new ToggleButton(text);
        button.setToggleGroup(amountGroup);
        button.setFont(getFont(14, FontWeight.BOLD));
        button.setPrefWidth(80);
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

}
