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
        VBox rootLayout = new VBox(40); // Increased spacing
        rootLayout.setPadding(new Insets(60)); // Increased padding
        rootLayout.setStyle("-fx-background-color: " + BACKGROUND_COLOR + ";");
        rootLayout.setAlignment(Pos.TOP_CENTER);

        Node header = createHeader();
        Node resourcesContent = createResourcesContent();

        rootLayout.getChildren().addAll(header, resourcesContent);

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
        header.setPadding(new Insets(0, 0, 30, 0)); // Increased padding

        Label title = new Label("📚 Resources & Information");
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

    private Node createResourcesContent() {
        VBox card = new VBox(40); // Increased spacing
        card.setPadding(new Insets(50)); // Increased padding
        card.setStyle("-fx-background-color: " + CARD_BACKGROUND_COLOR + "; " +
                      "-fx-background-radius: 15; " + // Increased radius
                      "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.05), 15, 0, 0, 6);"); // Increased shadow
        card.setMaxWidth(1000); // Increased max width

        // --- Emergency Contacts Section ---
        Node contactsSection = createResourceSection(
            "Emergency Contacts",
            "In any emergency, prompt contact with authorities is crucial. Use these numbers to report incidents or request immediate assistance.\n\n" +
            "National Disaster Helpline: 108 - For all disaster-related emergencies.\n" +
            "Police: 100 - For law enforcement and public safety issues.\n" +
            "Fire Department: 101 - For fire emergencies and rescue operations.\n" +
            "Ambulance: 102 - For medical emergencies and patient transport."
        );

        // --- Safety Guides Section ---
        Node guidesSection = createResourceSection(
            "Safety Guides",
            "Understanding basic safety protocols can save lives. Familiarize yourself with these guidelines for common disasters:\n\n" +
            "• Earthquake: Drop, Cover, and Hold On. Get under a sturdy table or desk. Stay away from windows. If outdoors, move to an open area away from buildings and power lines.\n\n" +
            "• Flood: Seek higher ground immediately. Do NOT walk or drive through floodwaters – even shallow water can be dangerous. Turn off utilities if advised. Have an emergency kit ready.\n\n" +
            "• Wildfire: Evacuate immediately if advised by authorities. Close all windows and doors to prevent embers from entering. If trapped, seek an area with minimal vegetation or a large body of water.\n\n" +
            "• Cyclone/Hurricane: Secure loose outdoor items. Stay indoors in the strongest part of your home. Prepare for power outages and have a supply of water and non-perishable food.\n\n" +
            "• Chemical Leak: Stay indoors, close windows and doors, and turn off ventilation systems. If outdoors, move away from the source quickly, preferably upwind. Follow official evacuation orders."
        );
        
        // --- Shelter Locations Section ---
        Node sheltersSection = createResourceSection(
            "Nearby Shelters (Nashik)",
            "Designated shelters offer a safe haven during and after disasters. Capacity and services may vary, so check local announcements for updates:\n\n" +
            "• Public Night Shelter, Sai Baba Nagar, Nashik: A leading NGO for the homeless, operates 24/7.\n" +
            "• Chhatrapati Shivaji Stadium (Gymkhana Stadium), CBS, Nashik: A major multi-purpose stadium often used for large gatherings.\n" +
            "• Raosaheb Thorat Auditorium, Sharanpur Road, Nashik: A community hall that can serve as a temporary shelter.\n" +
            "• Parshuram Saikhedkar Natymandir, Shalimar Rd, Nashik: Another community hall in a central area.\n" +
            "• Mahesh Bhavan, CIDCO, Nashik: A large community hall suitable for temporary accommodation.\n" +
            "• NMC Shelter Homes: Nashik Municipal Corporation operates three shelter homes, with plans to build four more in areas like Nashik Road, Tapovan, Wadala, and Chehedi to increase capacity."
        );

        card.getChildren().addAll(contactsSection, guidesSection, sheltersSection);
        
        VBox container = new VBox(card);
        container.setAlignment(Pos.CENTER);
        
        return container;
    }

    private Node createResourceSection(String title, String content) {
        VBox section = new VBox(15); // Increased spacing
        section.setPadding(new Insets(25, 0, 25, 0)); // Increased padding
        section.setStyle("-fx-border-color: " + BORDER_COLOR + " transparent transparent transparent; -fx-border-width: 1;");

        // Remove border from the first item
        section.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                 if (((VBox)section.getParent()).getChildren().get(0) == section) {
                     section.setStyle("-fx-border-width: 0; -fx-padding-top: 0;");
                 }
            }
        });
        
        Label titleLabel = new Label(title);
        titleLabel.setFont(getFont(28, FontWeight.BOLD)); // Increased font size
        titleLabel.setTextFill(Color.web(PRIMARY_BLUE));
        
        Text contentText = new Text(content);
        contentText.setFont(getFont(18, FontWeight.NORMAL)); // Increased font size
        contentText.setFill(Color.web(FONT_COLOR_DARK));
        contentText.setWrappingWidth(900); // Increased wrapping width
        contentText.setLineSpacing(7); // Increased line spacing
        
        section.getChildren().addAll(titleLabel, contentText);
        
        return section;
    }

}