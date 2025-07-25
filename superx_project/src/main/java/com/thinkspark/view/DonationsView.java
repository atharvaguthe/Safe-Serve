package com.thinkspark.view;

import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
        VBox rootLayout = new VBox(40); // Increased spacing
        rootLayout.setPadding(new Insets(60)); // Increased padding
        rootLayout.setStyle("-fx-background-color: " + BACKGROUND_COLOR + ";");
        rootLayout.setAlignment(Pos.TOP_CENTER);

        Node header = createHeader();
        Node donationForm = createDonationForm();

        rootLayout.getChildren().addAll(header, donationForm);

        ScrollPane scrollPane = new ScrollPane(rootLayout);
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        Scene scene = new Scene(scrollPane,1920,1080);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Safe Serve");
    }

    private Node createHeader() {
        HBox header = new HBox(20); // Increased spacing
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(0, 0, 30, 0)); // Increased padding

        Label title = new Label("💖 Make a Donation");
        title.setFont(getFont(40, FontWeight.BOLD)); // Increased font size
        title.setTextFill(Color.web(FONT_COLOR_DARK));

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button backButton = new Button("⬅️ Back to Dashboard");
        backButton.setFont(getFont(20, FontWeight.MEDIUM)); // Increased font size
        backButton.setTextFill(Color.web(PRIMARY_BLUE));
        backButton.setStyle("-fx-background-color: transparent; -fx-cursor: hand; -fx-border-color: " + PRIMARY_BLUE + "; -fx-border-radius: 10;"); // Increased radius
        backButton.setPadding(new Insets(10, 20, 10, 20)); // Increased padding
        backButton.setOnAction(e -> {
            primaryStage.setScene(previousScene);
            primaryStage.setTitle("Citizen Dashboard | Disaster Relief Helper");
        });

        header.getChildren().addAll(title, spacer, backButton);
        return header;
    }

    private Node createDonationForm() {
        VBox card = new VBox(35); // Increased spacing
        card.setPadding(new Insets(40)); // Increased padding
        card.setStyle("-fx-background-color: " + CARD_BACKGROUND_COLOR + "; " +
                      "-fx-background-radius: 15; " + // Increased radius
                      "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.05), 15, 0, 0, 6);"); // Increased shadow
        card.setMaxWidth(900); // Increased max width

        // --- Monetary Donation Section ---
        Label monetaryTitle = new Label("Monetary Donation");
        monetaryTitle.setFont(getFont(28, FontWeight.BOLD)); // Increased font size

        GridPane formGrid = new GridPane();
        formGrid.setHgap(30); // Increased gap
        formGrid.setVgap(25); // Increased gap

        // Donation Amount
        formGrid.add(createLabel("Select Amount ($)"), 0, 0);
        HBox amountBox = new HBox(15); // Increased spacing
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
        
        // --- Payment Method Selection ---
        formGrid.add(createLabel("Payment Method"), 0, 2);
        ComboBox<String> paymentMethodComboBox = new ComboBox<>();
        paymentMethodComboBox.getItems().addAll("UPI", "Card");
        paymentMethodComboBox.setPromptText("Select payment method");
        paymentMethodComboBox.setPrefHeight(60); // Increased height
        paymentMethodComboBox.setMaxWidth(Double.MAX_VALUE);
        paymentMethodComboBox.setStyle("-fx-font-size: 18px; -fx-background-color: #FFFFFF; -fx-border-color: " + BORDER_COLOR + "; -fx-border-radius: 10; -fx-padding: 10;");
        formGrid.add(paymentMethodComboBox, 1, 2);

        // --- Payment Details Section (Conditional) ---
        VBox paymentDetailsContainer = new VBox(25); // Spacing for elements within payment details
        paymentDetailsContainer.setPadding(new Insets(20, 0, 0, 0)); // Padding above payment details

        // Card Details Section
        VBox cardDetailsSection = new VBox(20);
        cardDetailsSection.getChildren().addAll(
            createLabel("Card Number"), createTextField("•••• •••• •••• ••••"),
            createLabel("Cardholder Name"), createTextField("John Doe")
        );
        HBox expiryAndCvvBox = new HBox(20);
        VBox expiryBox = new VBox(5);
        expiryBox.getChildren().addAll(createLabel("Expiry Date"), createTextField("MM/YY"));
        VBox cvvBox = new VBox(5);
        cvvBox.getChildren().addAll(createLabel("CVV"), createTextField("•••"));
        expiryAndCvvBox.getChildren().addAll(expiryBox, cvvBox);
        cardDetailsSection.getChildren().add(expiryAndCvvBox);

        // UPI QR Code Section
        VBox upiDetailsSection = new VBox(20);
        upiDetailsSection.setAlignment(Pos.CENTER);
        Label scanLabel = new Label("Scan to Pay:");
        scanLabel.setFont(getFont(20, FontWeight.BOLD));
        ImageView qrCodeImageView = new ImageView(new Image(getClass().getResourceAsStream("/Assets/Images/qr-varad.jpg")));
        qrCodeImageView.setFitWidth(300); // Set QR code size
        qrCodeImageView.setFitHeight(300);
        qrCodeImageView.setPreserveRatio(true);
        upiDetailsSection.getChildren().addAll(scanLabel, qrCodeImageView);

        // Initially hide both, show based on selection
        cardDetailsSection.setVisible(false);
        cardDetailsSection.setManaged(false); // Does not take up space
        upiDetailsSection.setVisible(false);
        upiDetailsSection.setManaged(false); // Does not take up space

        paymentMethodComboBox.setOnAction(e -> {
            cardDetailsSection.setVisible(false);
            cardDetailsSection.setManaged(false);
            upiDetailsSection.setVisible(false);
            upiDetailsSection.setManaged(false);

            if ("UPI".equals(paymentMethodComboBox.getValue())) {
                upiDetailsSection.setVisible(true);
                upiDetailsSection.setManaged(true);
            } else if ("Card".equals(paymentMethodComboBox.getValue())) {
                cardDetailsSection.setVisible(true);
                cardDetailsSection.setManaged(true);
            }
        });

        paymentDetailsContainer.getChildren().addAll(cardDetailsSection, upiDetailsSection);
        
        // --- Submission Button ---
        Button donateButton = new Button("Donate Now");
        donateButton.setFont(getFont(22, FontWeight.BOLD)); // Increased font size
        donateButton.setTextFill(Color.web(FONT_COLOR_LIGHT));
        donateButton.setStyle("-fx-background-color: " + SUCCESS_COLOR + "; -fx-background-radius: 10; -fx-cursor: hand;"); // Increased radius
        donateButton.setPadding(new Insets(15, 30, 15, 30)); // Increased padding
        HBox actionBox = new HBox(donateButton);
        actionBox.setAlignment(Pos.CENTER_RIGHT);
        
        VBox container = new VBox(card);
        container.setAlignment(Pos.CENTER);

        donateButton.setOnAction(e -> {
            System.out.println("Donation Submitted!");
            
            VBox confirmationVBox = new VBox(30); // Increased spacing
            confirmationVBox.setAlignment(Pos.CENTER);
            confirmationVBox.setPadding(new Insets(50)); // Increased padding
            
            Label confirmationTitle = new Label("💖 Thank You for Your Generosity!");
            confirmationTitle.setFont(getFont(30, FontWeight.BOLD)); // Increased font size
            confirmationTitle.setTextFill(Color.web(FONT_COLOR_DARK));
            
            Label confirmationText = new Label("Your donation will make a significant impact. We are grateful for your support. You will be redirected shortly.");
            confirmationText.setFont(getFont(18, FontWeight.NORMAL)); // Increased font size
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

        card.getChildren().addAll(monetaryTitle, formGrid, paymentDetailsContainer, actionBox); // Added paymentDetailsContainer
        return container;
    }

    private Label createLabel(String text) {
        Label label = new Label(text);
        label.setFont(getFont(20, FontWeight.MEDIUM)); // Increased font size
        label.setTextFill(Color.web(FONT_COLOR_DARK));
        return label;
    }

    private TextField createTextField(String prompt) {
        TextField textField = new TextField();
        textField.setPromptText(prompt);
        styleFormControl(textField);
        textField.setPrefHeight(60); // Increased height
        return textField;
    }

    private ToggleButton createAmountButton(String text) {
        ToggleButton button = new ToggleButton(text);
        button.setToggleGroup(amountGroup);
        button.setFont(getFont(18, FontWeight.BOLD)); // Increased font size
        button.setPrefWidth(100); // Increased width
        button.setPrefHeight(60); // Increased height
        button.setStyle("-fx-background-color: " + BACKGROUND_COLOR + "; -fx-border-color: " + BORDER_COLOR + "; -fx-border-radius: 8; -fx-cursor: hand;"); // Increased radius
        button.selectedProperty().addListener((obs, wasSelected, isSelected) -> {
            if (isSelected) {
                button.setStyle("-fx-background-color: " + PRIMARY_BLUE + "; -fx-text-fill: " + FONT_COLOR_LIGHT + "; -fx-border-color: " + PRIMARY_BLUE + "; -fx-border-radius: 8;");
            } else {
                button.setStyle("-fx-background-color: " + BACKGROUND_COLOR + "; -fx-text-fill: " + FONT_COLOR_DARK + "; -fx-border-color: " + BORDER_COLOR + "; -fx-border-radius: 8;");
            }
        });
        return button;
    }

    private void styleFormControl(Control control) {
        control.setStyle("-fx-font-size: 18px; -fx-background-color: #FFFFFF; -fx-border-color: " + BORDER_COLOR + "; -fx-border-radius: 10; -fx-padding: 10;"); // Increased font size, padding, radius
        if (control instanceof TextArea) {
            ((TextArea) control).setPrefHeight(120); // Increased height for text area
        } else if (control instanceof ComboBox) {
            ((ComboBox<?>) control).setPrefHeight(60); // Increased height for combo box
        } else if (control instanceof Spinner) {
            ((Spinner<?>) control).setPrefHeight(60); // Increased height for spinner
        }
    }

}