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

public class GovDashboard extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Main vertical container
        VBox mainContent = new VBox(40);
        mainContent.setPadding(new Insets(40));
        mainContent.setStyle("-fx-background-color: #19263A;");
        mainContent.setAlignment(Pos.TOP_CENTER);

        // NAVBAR
        HBox navbar = new HBox(50);
        navbar.setPadding(new Insets(20));
        navbar.setAlignment(Pos.CENTER_LEFT);
        Label logo = new Label("🏢 Helper");
        logo.setTextFill(Color.web("#fca311"));
        logo.setFont(Font.font("Arial", 40));

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox navLinks = new HBox(40);
        navLinks.setAlignment(Pos.CENTER_RIGHT);
        String[] navItems = { "Home", "Assistance", "Resources", "About us" };
        for (String item : navItems) {
            Label link = new Label(item);
            link.setTextFill(Color.WHITE);
            link.setFont(Font.font(22));
            navLinks.getChildren().add(link);
        }

        navbar.getChildren().addAll(logo, spacer, navLinks);

        // BANNER
        ImageView banner = new ImageView(new Image("Assets//Images//disaster.java"));
        banner.setFitWidth(1000);
        banner.setPreserveRatio(true);
        banner.setSmooth(true);
        banner.setStyle("-fx-effect: dropshadow(three-pass-box, gray, 10, 0, 0, 0);");

        // QUICK ACCESS
        VBox quickAccess = new VBox(20);
        quickAccess.setAlignment(Pos.CENTER);
        Label quickLabel = new Label("Quick Access");
        quickLabel.setTextFill(Color.WHITE);
        quickLabel.setFont(Font.font(30));

        HBox quickButtons = new HBox(30);
        quickButtons.setAlignment(Pos.CENTER);
        String[] buttonTexts = { "Broadcast Alerts", "Manage NGOs", "Map", "Analytics" };
        for (String text : buttonTexts) {
            Button btn = new Button(text);
            btn.setPrefSize(200, 100);
            btn.setFont(Font.font(18));
            btn.setStyle("-fx-background-color: #7a90b8; -fx-text-fill: white; -fx-background-radius: 10;");
            quickButtons.getChildren().add(btn);
        }

        quickAccess.getChildren().addAll(quickLabel, quickButtons);

        // LIVE ALERTS
        Label alerts = new Label("Live Alerts");
        alerts.setFont(Font.font(28));
        alerts.setTextFill(Color.WHITE);
        StackPane alertBox = new StackPane(alerts);
        alertBox.setPrefHeight(150);
        alertBox.setStyle("-fx-background-color: #7a90b8; -fx-background-radius: 10;");

        // FEATURED NEWS
        Label newsLabel = new Label("Featured News");
        newsLabel.setFont(Font.font(28));
        newsLabel.setTextFill(Color.WHITE);

        HBox newsCards = new HBox(30);
        newsCards.setPadding(new Insets(10));
        newsCards.setAlignment(Pos.CENTER);
        for (int i = 0; i < 3; i++) {
            Region card = new Region();
            card.setPrefSize(200, 300);
            card.setStyle("-fx-background-color: #7a90b8; -fx-background-radius: 10;");
            newsCards.getChildren().add(card);
        }

        // STATISTICS
        Label statsLabel = new Label("Statistics");
        statsLabel.setFont(Font.font(28));
        statsLabel.setTextFill(Color.WHITE);

        GridPane statsGrid = new GridPane();
        statsGrid.setPadding(new Insets(30));
        statsGrid.setHgap(50);
        statsGrid.setAlignment(Pos.CENTER);
        statsGrid.setStyle("-fx-background-color: #46c7c1; -fx-background-radius: 10;");

        String[][] statsData = {
                { "12K", "people rescued" },
                { "56K", "Request Handled" },
                { "343", "Active Volunteers" },
                { "39K", "Resources Delivered" }
        };

        for (int i = 0; i < statsData.length; i++) {
            VBox box = new VBox(10);
            box.setAlignment(Pos.CENTER);
            Label number = new Label(statsData[i][0]);
            number.setFont(Font.font(40));
            number.setTextFill(Color.BLACK);
            Label label = new Label(statsData[i][1]);
            label.setFont(Font.font(20));
            label.setTextFill(Color.BLACK);
            box.getChildren().addAll(number, label);
            statsGrid.add(box, i, 0);
        }

        // Assemble layout
        mainContent.getChildren().addAll(
                navbar, banner, quickAccess,
                alertBox, newsLabel, newsCards,
                statsLabel, statsGrid
        );

        // Make it scrollable
        ScrollPane scrollPane = new ScrollPane(mainContent);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: #19263A;");

        Scene scene = new Scene(scrollPane, 1200, 800);
        primaryStage.setTitle("Disaster Relief Helper");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    
}
