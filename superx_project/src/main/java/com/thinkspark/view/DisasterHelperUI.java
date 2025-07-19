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
import javafx.stage.Stage;

public class DisasterHelperUI extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Main content container
        VBox mainContent = new VBox(40);
        mainContent.setPadding(new Insets(40));
        // Set background to white for a clean, modern look
        mainContent.setStyle("-fx-background-color: #FFFFFF;");
        mainContent.setAlignment(Pos.TOP_CENTER);

        // NAVBAR
        HBox navbar = new HBox(50);
        navbar.setPadding(new Insets(20));
        navbar.setAlignment(Pos.CENTER_LEFT);

        Label logo = new Label("🏢 Safe Serve");
        // Use a strong, professional blue for the logo text
        logo.setTextFill(Color.web("#005A9C"));
        logo.setFont(Font.font("Arial", 40));

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox navLinks = new HBox(40);
        navLinks.setAlignment(Pos.CENTER);
        String[] navItems = { "Home", "Assistance", "Resources", "About us" };
        for (String item : navItems) {
            Label link = new Label(item);
            // Use a dark gray for navigation links for readability
            link.setTextFill(Color.web("#333333"));
            link.setFont(Font.font(22));
            navLinks.getChildren().add(link);
        }

        // Log in & Sign up buttons
        VBox authButtons = new VBox(10);
        authButtons.setAlignment(Pos.CENTER_RIGHT);

        Button loginBtn = new Button("Log in");
        Button signupBtn = new Button("Sign up");

        for (Button btn : new Button[]{loginBtn, signupBtn}) {
            btn.setFont(Font.font(18));
            btn.setTextFill(Color.WHITE);
            // Style buttons with the primary blue color
            btn.setStyle("-fx-background-color: #007BFF; -fx-background-radius: 20; -fx-padding: 6 20 6 20;");
        }

        authButtons.getChildren().addAll(loginBtn, signupBtn);
        navbar.getChildren().addAll(logo, spacer, navLinks, authButtons);

        // BANNER IMAGE
        // Replaced local image with a placeholder for preview purposes
        ImageView banner = new ImageView(new Image("https://placehold.co/1000x400/007BFF/FFFFFF?text=We+Are+Here+To+Help"));
        banner.setFitWidth(1000);
        banner.setPreserveRatio(true);
        banner.setSmooth(true);
        // Add a subtle drop shadow and rounded corners to the banner
        banner.setStyle("-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 10, 0, 0, 0); -fx-background-radius: 10;");

        // QUICK ACCESS SECTION
        VBox quickAccess = new VBox(20);
        quickAccess.setAlignment(Pos.CENTER);
        Label quickLabel = new Label("Quick Access");
        quickLabel.setTextFill(Color.BLACK);
        quickLabel.setFont(Font.font(30));

        HBox quickButtons = new HBox(30);
        quickButtons.setAlignment(Pos.CENTER);
        String[] quickItems = { "Disaster Info Type", "Nearest Safe Zone", "Relief Resources", "Donate Now" };
        for (String item : quickItems) {
            Button btn = new Button(item);
            btn.setPrefSize(200, 100);
            btn.setFont(Font.font(18));
            // Use a light gray for secondary buttons to distinguish them
            btn.setStyle("-fx-background-color: #F0F0F0; -fx-text-fill: black; -fx-background-radius: 10; -fx-border-color: #DDDDDD; -fx-border-width: 1;");
            quickButtons.getChildren().add(btn);
        }

        quickAccess.getChildren().addAll(quickLabel, quickButtons);

        // LIVE ALERTS
        Label alerts = new Label("Live Alerts");
        alerts.setFont(Font.font(28));
        alerts.setTextFill(Color.web("#5C3D03")); // Dark brown text for contrast
        StackPane alertBox = new StackPane(alerts);
        alertBox.setPrefHeight(150);
        // Use a light warning yellow for the alerts section
        alertBox.setStyle("-fx-background-color: #FFF3CD; -fx-background-radius: 10;");

        // FEATURED NEWS
        Label newsLabel = new Label("Featured News");
        newsLabel.setFont(Font.font(28));
        newsLabel.setTextFill(Color.BLACK);

        HBox newsCards = new HBox(30);
        newsCards.setPadding(new Insets(10));
        newsCards.setAlignment(Pos.CENTER);
        for (int i = 0; i < 3; i++) {
            // Replaced Region with a VBox to hold placeholder content
            VBox card = new VBox(10);
            card.setPrefSize(200, 300);
            // Style cards with a white background and a subtle border
            card.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 10; -fx-border-color: #E0E0E0; -fx-border-width: 1; -fx-border-radius: 10;");
            card.setPadding(new Insets(15));
            
            Label cardTitle = new Label("News Headline " + (i + 1));
            cardTitle.setFont(Font.font("Arial", 18));
            cardTitle.setWrapText(true);
            
            Label cardContent = new Label("A brief summary of the news article will appear here, giving users a glimpse of the content.");
            cardContent.setWrapText(true);
            
            card.getChildren().addAll(cardTitle, cardContent);
            newsCards.getChildren().add(card);
        }

        // STATISTICS SECTION
        Label statsLabel = new Label("Statistics");
        statsLabel.setFont(Font.font(28));
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
            number.setFont(Font.font(40));
            // Use the primary blue to highlight the numbers
            number.setTextFill(Color.web("#005A9C"));
            Label label = new Label(statsData[i][1]);
            label.setFont(Font.font(20));
            label.setTextFill(Color.BLACK);
            box.getChildren().addAll(number, label);
            statsGrid.add(box, i, 0);
        }

        signupBtn.setOnAction(e -> {
            Stage currentStage = (Stage) signupBtn.getScene().getWindow();
            currentStage.close();
            Stage signUpStage = new Stage();
            SignUpForm signUpForm = new SignUpForm();
            try {
                signUpForm.start(signUpStage);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        loginBtn.setOnAction(e -> {
            Stage currentStage = (Stage) loginBtn.getScene().getWindow();
            currentStage.close();
            Stage loginStage = new Stage();
            LoginUI loginUI = new LoginUI();
            try {
                loginUI.start(loginStage);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        // Assemble full page
        mainContent.getChildren().addAll(
                navbar, banner, quickAccess,
                alertBox, newsLabel, newsCards,
                statsLabel, statsGrid
        );

        // Make scrollable
        ScrollPane scrollPane = new ScrollPane(mainContent);
        scrollPane.setFitToWidth(true);
        // Ensure the scroll pane background is also white
        scrollPane.setStyle("-fx-background: #FFFFFF;");

        // Scene
        Scene scene = new Scene(scrollPane, 1200, 800);
        primaryStage.setTitle("Disaster Relief Helper");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
