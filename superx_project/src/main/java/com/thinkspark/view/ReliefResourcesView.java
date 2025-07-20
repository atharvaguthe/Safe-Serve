package com.thinkspark.view;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class ReliefResourcesView extends Application {

    // --- Style Definitions (consistent with other views) ---
    private static final String BACKGROUND_COLOR = "#F4F7FC";
    private static final String PRIMARY_BLUE = "#005A9C";
    private static final String FONT_COLOR_DARK = "#2D3748";
    private static final String CARD_BACKGROUND_COLOR = "#FFFFFF";
    private static final String BORDER_COLOR = "#E2E8F0";
    private static final String ACCENT_COLOR_GREEN = "#38A169"; // For availability
    private static final String ACCENT_COLOR_ORANGE = "#DD6B20"; // For limited stock

    private final Scene previousScene;
    private final Stage primaryStage;

    // --- Font Helper ---
    private static Font getFont(double size, FontWeight weight) {
        return Font.font("System", weight, size);
    }

    /**
     * Constructor to receive the previous scene for back navigation.
     * @param primaryStage The main application stage.
     * @param previousScene The scene of the main dashboard.
     */
    public ReliefResourcesView(Stage primaryStage, Scene previousScene) {
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
        Node resourcesContent = createResourcesContent();

        rootLayout.getChildren().addAll(header, resourcesContent);

        ScrollPane scrollPane = new ScrollPane(rootLayout);
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        Scene scene = new Scene(scrollPane, 1280, 900);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Relief Resources | Disaster Relief Helper");
    }

    private Node createHeader() {
        HBox header = new HBox();
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(0, 0, 20, 0));

        Label title = new Label("📦 Relief Resources");
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
            primaryStage.setTitle("Safe Serve | Disaster Relief Helper");
        });

        header.getChildren().addAll(title, spacer, backButton);
        return header;
    }

    private Node createResourcesContent() {
        VBox card = new VBox(30);
        card.setPadding(new Insets(40));
        card.setStyle("-fx-background-color: " + CARD_BACKGROUND_COLOR + "; " +
                      "-fx-background-radius: 12; " +
                      "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.05), 10, 0, 0, 4);");
        card.setMaxWidth(900);

        // --- Resource Categories ---
        GridPane resourcesGrid = new GridPane();
        resourcesGrid.setHgap(30);
        resourcesGrid.setVgap(30);

        resourcesGrid.add(createResourceCategory(
            "💧 Food & Water",
            "Distribution centers for essential supplies.",
            new String[]{
                "Collector Office, Nashik - Water Bottles (High Stock)",
                "Rajiv Gandhi Bhavan, Nashik - Food Packets (Medium Stock)",
                "Panchavati Karanja - Community Kitchen (Operating 24/7)"
            }
        ), 0, 0);

        resourcesGrid.add(createResourceCategory(
            "⚕️ Medical Supplies",
            "First-aid, medicines, and hygiene kits.",
            new String[]{
                "Nashik Civil Hospital - First-Aid Kits (High Stock)",
                "Wockhardt Hospital - General Medicines (Limited Stock)",
                "Red Cross Society, Shalimar - Hygiene Kits (Medium Stock)"
            }
        ), 1, 0);

        resourcesGrid.add(createResourceCategory(
            "⛺ Temporary Shelters",
            "Safe locations for temporary accommodation.",
            new String[]{
                "Meenatai Thackeray Stadium, Panchavati",
                "Kalidas Kalamandir, Shalimar",
                "Various schools in the affected zones (check local announcements)"
            }
        ), 0, 1);
        
        resourcesGrid.add(createResourceCategory(
            "👕 Clothing & Blankets",
            "Warm clothing and blankets for all ages.",
            new String[]{
                "Godavari Ghat Collection Point, Panchavati",
                "City Centre Mall (Basement Collection)",
                "Local NGO distribution vans (mobile)"
            }
        ), 1, 1);

        card.getChildren().add(resourcesGrid);
        
        VBox container = new VBox(card);
        container.setAlignment(Pos.CENTER);
        
        return container;
    }

    private Node createResourceCategory(String title, String description, String[] items) {
        VBox categoryBox = new VBox(15);
        categoryBox.setPadding(new Insets(20));
        categoryBox.setStyle("-fx-background-color: " + BACKGROUND_COLOR + "; -fx-border-color: " + BORDER_COLOR + "; -fx-border-radius: 10;");
        categoryBox.setPrefHeight(250);

        Label titleLabel = new Label(title);
        titleLabel.setFont(getFont(20, FontWeight.BOLD));
        titleLabel.setTextFill(Color.web(PRIMARY_BLUE));

        Text descText = new Text(description);
        descText.setFont(getFont(14, FontWeight.NORMAL));
        descText.setFill(Color.web(FONT_COLOR_DARK));
        descText.setWrappingWidth(350);

        VBox itemsList = new VBox(8);
        for (String item : items) {
            itemsList.getChildren().add(createResourceItem(item));
        }

        categoryBox.getChildren().addAll(titleLabel, descText, itemsList);
        return categoryBox;
    }

    private Node createResourceItem(String itemText) {
        HBox itemBox = new HBox(5);
        itemBox.setAlignment(Pos.CENTER_LEFT);
        
        String statusText = "";
        String color = FONT_COLOR_DARK;

        if (itemText.contains("(High Stock)")) {
            statusText = "High Stock";
            color = ACCENT_COLOR_GREEN;
        } else if (itemText.contains("(Medium Stock)")) {
            statusText = "Medium Stock";
            color = ACCENT_COLOR_ORANGE;
        } else if (itemText.contains("(Limited Stock)")) {
            statusText = "Limited Stock";
            color = Color.RED.toString();
        }
        
        Label mainLabel = new Label(itemText.split("\\(")[0].trim());
        mainLabel.setFont(getFont(14, FontWeight.NORMAL));
        mainLabel.setTextFill(Color.web(FONT_COLOR_DARK));

        Label statusLabel = new Label(statusText);
        statusLabel.setFont(getFont(12, FontWeight.BOLD));
        statusLabel.setTextFill(Color.web(color));
        statusLabel.setPadding(new Insets(2, 6, 2, 6));
        statusLabel.setStyle("-fx-background-color: " + Color.web(color).deriveColor(0, 1, 1, 0.1).toString().replace("0x", "#") + "; -fx-background-radius: 5;");
        
        if (!statusText.isEmpty()) {
            itemBox.getChildren().addAll(mainLabel, statusLabel);
        } else {
            itemBox.getChildren().add(mainLabel);
        }

        return itemBox;
    }
}
