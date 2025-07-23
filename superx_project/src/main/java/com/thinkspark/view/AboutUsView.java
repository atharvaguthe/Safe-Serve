package com.thinkspark.view;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

public class AboutUsView extends Application {

    private static final String BACKGROUND_COLOR = "#F4F7FC";
    private static final String PRIMARY_BLUE = "#005A9C";
    private static final String FONT_COLOR_DARK = "#2D3748";
    private static final String CARD_BACKGROUND_COLOR = "#FFFFFF";

    private final Scene previousScene;
    private final Stage primaryStage;

    private static Font getFont(double size, FontWeight weight) {
        return Font.font("System", weight, size);
    }

    public AboutUsView(Stage primaryStage, Scene previousScene) {
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
        Node aboutContent = createAboutContent();

        rootLayout.getChildren().addAll(header, aboutContent);

        ScrollPane scrollPane = new ScrollPane(rootLayout);
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        Scene scene = new Scene(scrollPane, 1280, 900);
        primaryStage.setScene(scene);
        primaryStage.setTitle("About Us | Disaster Relief Helper");
    }

    private Node createHeader() {
        HBox header = new HBox();
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(0, 0, 20, 0));

        Label title = new Label("💡 About Safe Serve");
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

    private Node createAboutContent() {
        VBox card = new VBox(30);
        card.setPadding(new Insets(40));
        card.setStyle("-fx-background-color: " + CARD_BACKGROUND_COLOR + "; " +
                      "-fx-background-radius: 12; " +
                      "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.05), 10, 0, 0, 4);");
        card.setMaxWidth(1100);

        VBox projectSection = (VBox) createSection(
            "Our Project : Safe Serve",
            "Safe Serve is a comprehensive disaster relief platform designed to connect citizens, volunteers, and authorities during emergencies. Our mission is to streamline the process of reporting incidents, requesting aid, and distributing resources efficiently to save lives and support communities in times of crisis."
        );

        VBox spacerBeforeTeacher = new VBox();
        spacerBeforeTeacher.setPrefHeight(50);
        projectSection.getChildren().add(spacerBeforeTeacher);

        Label teacherSubTitle = new Label("Our Teacher : Shashi Bagal");
        teacherSubTitle.setFont(getFont(22, FontWeight.BOLD));
        teacherSubTitle.setTextFill(Color.web(PRIMARY_BLUE));
        projectSection.getChildren().add(teacherSubTitle);

        VBox teacherCardContainer = new VBox();
        teacherCardContainer.setAlignment(Pos.CENTER);
        teacherCardContainer.getChildren().add(createPersonCard("Shashi Bagal", "/image/newshahsi.jpg", 471, 471));
        projectSection.getChildren().add(teacherCardContainer);

        VBox teamSection = (VBox) createSection(
            "Our Team : ThinkSpark",
            "ThinkSpark is a team of passionate developers, designers, and problem-solvers dedicated to using technology for social good. We believe in the power of innovation to create practical solutions for real-world challenges. The Safe Serve project is our commitment to building a more resilient and connected society."
        );

        HBox teamRow = new HBox(30); // Changed from GridPane to HBox
        teamRow.setAlignment(Pos.CENTER);
        teamRow.getChildren().addAll(
            createPersonCard("Shivkumar Kapse(Team Lead)", "/image/shiv.jpg"),
            createPersonCard("Atharva Guthe", "/image/athrva.jpg"),
            createPersonCard("Varad Wani", "/image/new.jpg"),
        
            createPersonCard("Harish Gangurde", "/image/harish.jpg")
        );

        teamSection.getChildren().add(teamRow);

        Node missionSection = createSection(
            "Our Goal : ",
            "To empower communities with a reliable and intuitive platform that facilitates rapid response and effective communication during disasters, ensuring that help reaches those who need it most, as quickly as possible."
        );

        card.getChildren().addAll(projectSection, teamSection, missionSection);

        VBox container = new VBox(card);
        container.setAlignment(Pos.CENTER);

        return container;
    }

    private Node createSection(String title, String content) {
        VBox section = new VBox(10);

        Label titleLabel = new Label(title);
        titleLabel.setFont(getFont(22, FontWeight.BOLD));
        titleLabel.setTextFill(Color.web(PRIMARY_BLUE));

        Text contentText = new Text(content);
        contentText.setFont(getFont(16, FontWeight.NORMAL));
        contentText.setFill(Color.web(FONT_COLOR_DARK));
        contentText.setWrappingWidth(1000);
        contentText.setTextAlignment(TextAlignment.JUSTIFY);

        section.getChildren().addAll(titleLabel, contentText);
        return section;
    }

    private Node createPersonCard(String name, String imagePath) {
        return createPersonCard(name, imagePath, 200, 200);
    }

    private Node createPersonCard(String name, String imagePath, double width, double height) {
        VBox personCard = new VBox(10);
        personCard.setAlignment(Pos.CENTER);
        personCard.setPadding(new Insets(10));

        ImageView imageView;
        try {
            Image image = new Image(getClass().getResource(imagePath).toExternalForm());
            imageView = new ImageView(image);
            imageView.setFitWidth(width);
            imageView.setFitHeight(height);
            imageView.setPreserveRatio(false);
        } catch (NullPointerException e) {
            System.err.println("Warning: Image not found at path: " + imagePath);
            imageView = new ImageView();
        }

        Label nameLabel = new Label(name);
        nameLabel.setFont(getFont(16, FontWeight.BOLD));
        nameLabel.setTextFill(Color.web(FONT_COLOR_DARK));
        nameLabel.setTextAlignment(TextAlignment.CENTER);
        nameLabel.setWrapText(true);

        personCard.getChildren().addAll(imageView, nameLabel);
        return personCard;
    }
}
