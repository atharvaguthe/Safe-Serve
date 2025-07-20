package com.thinkspark.view;

import javafx.application.Application;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class CitizenDashboard extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Main vertical container with a clean white background
        VBox mainContent = new VBox(40);
        mainContent.setPadding(new Insets(0, 40, 40, 40)); // Adjust padding
        mainContent.setStyle("-fx-background-color: #FFFFFF;");
        mainContent.setAlignment(Pos.TOP_CENTER);

        // --- Header Section ---
        VBox headerSection = new VBox();
        headerSection.setStyle("-fx-background-color: #FFFFFF;");
        
        // NAVBAR
        HBox navbar = new HBox(50);
        navbar.setPadding(new Insets(20));
        navbar.setAlignment(Pos.CENTER_LEFT);
        
        Label logo = new Label("🏢 Helper");
        // Use the primary professional blue for the logo
        logo.setTextFill(Color.web("#005A9C"));
        logo.setFont(Font.font("Arial", FontWeight.BOLD, 40));

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox navLinks = new HBox(40);
        navLinks.setAlignment(Pos.CENTER_RIGHT);
        String[] navItems = { "Home", "Assistance", "Resources", "About us" };
        for (String item : navItems) {
            Label link = new Label(item);
            // Use dark gray for navigation links for readability
            link.setTextFill(Color.web("#333333"));
            link.setFont(Font.font("Arial", 22));
            navLinks.getChildren().add(link);
        }
        
        navbar.getChildren().addAll(logo, spacer, navLinks);

        // Welcome Message
        Label welcomeLabel = new Label("Hello, Citizen!");
        welcomeLabel.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        welcomeLabel.setTextFill(Color.web("#333333"));
        welcomeLabel.setPadding(new Insets(0, 20, 20, 20));

        headerSection.getChildren().addAll(navbar, welcomeLabel);


        // BANNER
        // Replaced image with a theme-appropriate placeholder
        ImageView banner = new ImageView(new Image("https://placehold.co/1000x300/005A9C/FFFFFF?text=Stay+Informed,+Stay+Safe"));
        banner.setFitWidth(1000);
        banner.setPreserveRatio(true);
        banner.setSmooth(true);
        // Add a subtle drop shadow and rounded corners
        banner.setStyle("-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 10, 0, 0, 0); -fx-background-radius: 10;");

        // QUICK ACCESS
        VBox quickAccess = new VBox(20);
        quickAccess.setAlignment(Pos.CENTER);
        Label quickLabel = new Label("Quick Access");
        quickLabel.setTextFill(Color.BLACK);
        quickLabel.setFont(Font.font("Arial", FontWeight.BOLD, 30));

        HBox quickButtons = new HBox(30);
        quickButtons.setAlignment(Pos.CENTER);
        String[] buttonTexts = { "All Requests", "Report Disaster", "Map", "Donate Now" };
        for (String text : buttonTexts) {
            Button btn = new Button(text);
            btn.setPrefSize(200, 100);
            btn.setFont(Font.font("Arial", 18));
            // Style secondary buttons with a light gray background
            btn.setStyle("-fx-background-color: #F0F0F0; -fx-text-fill: black; -fx-background-radius: 10; -fx-border-color: #DDDDDD; -fx-border-width: 1;");
            quickButtons.getChildren().add(btn);
        }

        quickAccess.getChildren().addAll(quickLabel, quickButtons);

        // LIVE ALERTS
        Label alertsLabel = new Label("Live Alerts");
        alertsLabel.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        alertsLabel.setTextFill(Color.web("#5C3D03")); // Dark brown for contrast
        StackPane alertBox = new StackPane(alertsLabel);
        alertBox.setPrefHeight(150);
        // Use a light warning yellow for the alerts section
        alertBox.setStyle("-fx-background-color: #FFF3CD; -fx-background-radius: 10;");

        // FEATURED NEWS
        Label newsLabel = new Label("Featured News");
        newsLabel.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        newsLabel.setTextFill(Color.BLACK);

        HBox newsCards = new HBox(30);
        newsCards.setPadding(new Insets(10));
        newsCards.setAlignment(Pos.CENTER);
        for (int i = 0; i < 3; i++) {
            VBox card = new VBox(10);
            card.setPrefSize(200, 300);
            // Style news cards with a white background and a subtle border
            card.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 10; -fx-border-color: #E0E0E0; -fx-border-width: 1; -fx-border-radius: 10;");
            card.setPadding(new Insets(15));
            
            Label cardTitle = new Label("News Headline " + (i + 1));
            cardTitle.setFont(Font.font("Arial", FontWeight.BOLD, 18));
            cardTitle.setWrapText(true);
            
            Label cardContent = new Label("A brief summary of the news article will appear here, providing a glimpse of the content.");
            cardContent.setWrapText(true);
            
            card.getChildren().addAll(cardTitle, cardContent);
            newsCards.getChildren().add(card);
        }

        // STATISTICS
        Label statsLabel = new Label("Statistics");
        statsLabel.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        statsLabel.setTextFill(Color.BLACK);

        GridPane statsGrid = new GridPane();
        statsGrid.setPadding(new Insets(30));
        statsGrid.setHgap(50);
        statsGrid.setAlignment(Pos.CENTER);
        // Use a very light, professional blue for the statistics background
        statsGrid.setStyle("-fx-background-color: #E9F5FF; -fx-background-radius: 10;");

        String[][] statsData = {
                { "12K", "People Rescued" },
                { "56K", "Requests Handled" },
                { "343", "Active Volunteers" },
                { "39K", "Resources Delivered" }
        };

        for (int i = 0; i < statsData.length; i++) {
            VBox box = new VBox(10);
            box.setAlignment(Pos.CENTER);
            Label number = new Label(statsData[i][0]);
            number.setFont(Font.font("Arial", FontWeight.BOLD, 40));
            // Use the primary blue to highlight the numbers
            number.setTextFill(Color.web("#005A9C"));
            Label label = new Label(statsData[i][1]);
            label.setFont(Font.font("Arial", 20));
            label.setTextFill(Color.BLACK);
            box.getChildren().addAll(number, label);
            statsGrid.add(box, i, 0);
        }

        // Assemble layout
        mainContent.getChildren().addAll(
                headerSection, banner, quickAccess,
                alertBox, newsLabel, newsCards,
                statsLabel, statsGrid
        );

        // Make it scrollable
        ScrollPane scrollPane = new ScrollPane(mainContent);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: #FFFFFF;"); // Ensure scroll pane background is white

        Scene scene = new Scene(scrollPane, 1200, 800);
        primaryStage.setTitle("Citizen Dashboard | Disaster Relief Helper");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
