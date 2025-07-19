package com.thinkspark.view;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class RequestHelpForm extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Request Help Form");

        // Overall layout container
        VBox root = new VBox();
        root.getStyleClass().add("root");

        // Header Section
        HBox header = createHeader();

        // Form Section
        VBox formContainer = createFormContainer();
        VBox.setVgrow(formContainer, Priority.ALWAYS); // Allow form to take available space

        // Add header and form to the root layout
        root.getChildren().addAll(header, formContainer);

        // Create and set the scene
        Scene scene = new Scene(root, 900, 700);
        // Link the CSS file
        scene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private HBox createHeader() {
        HBox header = new HBox(30);
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(10, 30, 10, 30));
        header.getStyleClass().add("header");

        // Logo
        Text logoIcon = new Text("🏢");
        logoIcon.setFont(Font.font("System", FontWeight.BOLD, 36));
        Text logoText = new Text("Helper");
        logoText.setId("logo-text");
        HBox logo = new HBox(5, logoIcon, logoText);
        logo.setAlignment(Pos.CENTER);

        // Navigation Links
        HBox navLinks = new HBox(25);
        navLinks.setAlignment(Pos.CENTER);
        Hyperlink homeLink = new Hyperlink("Home");
        Hyperlink assistanceLink = new Hyperlink("Assistance");
        Hyperlink resourcesLink = new Hyperlink("Resources");
        Hyperlink aboutUsLink = new Hyperlink("About us");
        navLinks.getChildren().addAll(homeLink, assistanceLink, resourcesLink, aboutUsLink);

        // Spacer to push right-side items
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        // Right-side icons
        HBox rightIcons = new HBox(25);
        rightIcons.setAlignment(Pos.CENTER);
        Label bellIcon = new Label("🔔");
        bellIcon.getStyleClass().add("icon");
        Circle profileCircle = new Circle(22);
        profileCircle.setId("profile-circle");
        rightIcons.getChildren().addAll(bellIcon, profileCircle);

        header.getChildren().addAll(logo, navLinks, spacer, rightIcons);
        return header;
    }

    private VBox createFormContainer() {
        VBox formContainer = new VBox(20);
        formContainer.setAlignment(Pos.TOP_CENTER);
        formContainer.setPadding(new Insets(30));

        // Form Title
        Label title = new Label("Request Help");
        title.setId("form-title");

        // Form Grid
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(20);
        grid.setVgap(15);
        grid.setMaxWidth(600);

        // Add form fields
        grid.add(new Label("Help type:-"), 0, 0);
        grid.add(new TextField(), 1, 0);

        grid.add(new Label("Description:-"), 0, 1);
        grid.add(new TextField(), 1, 1);

        grid.add(new Label("Address:-"), 0, 2);
        grid.add(new TextField(), 1, 2);

        grid.add(new Label("Disaster type:-"), 0, 3);
        grid.add(new TextField(), 1, 3);

        grid.add(new Label("Date & Time:-"), 0, 4);
        grid.add(new TextField(), 1, 4);

        grid.add(new Label("Upload Photo:-"), 0, 5);
        grid.add(new TextField(), 1, 5);

        grid.add(new Label("Location:-"), 0, 6);
        grid.add(new TextField(), 1, 6);

        // Submit Button
        Button submitButton = new Button("submit");
        submitButton.setId("submit-button");
        HBox buttonBox = new HBox(submitButton); // To align button
        buttonBox.setAlignment(Pos.CENTER_LEFT);
        grid.add(buttonBox, 1, 7);

        formContainer.getChildren().addAll(title, grid);
        return formContainer;
    }
}
