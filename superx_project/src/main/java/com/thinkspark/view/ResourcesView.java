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

public class ResourcesView extends Application {

    // --- Style Definitions ---
    private static final String BACKGROUND_COLOR = "#F4F7FC";
    private static final String PRIMARY_BLUE = "#005A9C";
    private static final String FONT_COLOR_DARK = "#2D3748";
    private static final String CARD_BACKGROUND_COLOR = "#FFFFFF";
    private static final String BORDER_COLOR = "#E2E8F0";

    private final Scene previousScene;
    private final Stage primaryStage;

    // --- Font Helper ---
    private static Font getFont(double size, FontWeight weight) {
        return Font.font("System", weight, size);
    }

    public ResourcesView(Stage primaryStage, Scene previousScene) {
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

        Scene scene = new Scene(scrollPane);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Safe Serve");
    }

    private Node createHeader() {
        HBox header = new HBox();
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(0, 0, 20, 0));

        Label title = new Label("📚 Resources & Information");
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

    private Node createResourcesContent() {
        VBox card = new VBox(30);
        card.setPadding(new Insets(40));
        card.setStyle("-fx-background-color: " + CARD_BACKGROUND_COLOR + "; " +
                      "-fx-background-radius: 12; " +
                      "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.05), 10, 0, 0, 4);");
        card.setMaxWidth(900);

        // --- Emergency Contacts Section ---
        Node contactsSection = createResourceSection(
            "Emergency Contacts",
            "National Disaster Helpline: 108\n" +
            "Police: 100\n" +
            "Fire Department: 101\n" +
            "Ambulance: 102"
        );

        // --- Safety Guides Section ---
        Node guidesSection = createResourceSection(
            "Safety Guides",
            "• Earthquake: Drop, Cover, and Hold On.\n" +
            "• Flood: Seek higher ground. Do not walk or drive through floodwaters.\n" +
            "• Wildfire: Evacuate immediately if advised. Close all windows and doors."
        );
        
        // --- Shelter Locations Section ---
        Node sheltersSection = createResourceSection(
            "Nearby Shelters",
            "• City Community Hall, 123 Central Plaza\n" +
            "• North District High School, 789 North Ave\n" +
            "• West Suburbs Sports Complex, 456 West Blvd"
        );

        card.getChildren().addAll(contactsSection, guidesSection, sheltersSection);
        
        VBox container = new VBox(card);
        container.setAlignment(Pos.CENTER);
        
        return container;
    }

    private Node createResourceSection(String title, String content) {
        VBox section = new VBox(10);
        
        Label titleLabel = new Label(title);
        titleLabel.setFont(getFont(22, FontWeight.BOLD));
        titleLabel.setTextFill(Color.web(PRIMARY_BLUE));
        
        Text contentText = new Text(content);
        contentText.setFont(getFont(16, FontWeight.NORMAL));
        contentText.setFill(Color.web(FONT_COLOR_DARK));
        contentText.setWrappingWidth(800);
        contentText.setLineSpacing(5);
        
        section.getChildren().addAll(titleLabel, contentText);
        section.setStyle("-fx-border-color: " + BORDER_COLOR + " transparent transparent transparent; -fx-border-width: 1; -fx-padding: 20 0 0 0;");

        // Remove border from the first item
        section.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                 if (((VBox)section.getParent()).getChildren().get(0) == section) {
                     section.setStyle("-fx-border-width: 0; -fx-padding: 0;");
                 }
            }
        });
        
        return section;
    }

}
