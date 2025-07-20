package com.thinkspark.view;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class ManageNgosView extends Application {
    // Style Definitions
    private static final String BACKGROUND_COLOR = "#F4F7FC";
    private static final String PRIMARY_BLUE = "#005A9C";
    private static final String CARD_BACKGROUND_COLOR = "#FFFFFF";
    private static final String BORDER_COLOR = "#E2E8F0";
    private static final String STATUS_VERIFIED_COLOR = "#38A169";
    private static final String STATUS_PENDING_COLOR = "#DD6B20";

    private final Scene previousScene;
    private final Stage primaryStage;

    private static Font getFont(double size, FontWeight weight) {
        return Font.font("System", weight, size);
    }

    private static class NgoItem {
        final String name;
        final String status;
        final String contact;
        public NgoItem(String name, String status, String contact) {
            this.name = name;
            this.status = status;
            this.contact = contact;
        }
    }

    private static class NgoCell extends ListCell<NgoItem> {
        public NgoCell() {
            super();
        }
        @Override
        protected void updateItem(NgoItem item, boolean empty) {
            super.updateItem(item, empty);
            if (empty || item == null) {
                setGraphic(null);
            } else {
                HBox content = new HBox();
                VBox details = new VBox(5);
                Label nameLabel = new Label(item.name);
                nameLabel.setFont(ManageNgosView.getFont(16, FontWeight.BOLD));
                Label contactLabel = new Label("Contact: " + item.contact);
                contactLabel.setOpacity(0.8);
                details.getChildren().addAll(nameLabel, contactLabel);

                Region spacer = new Region();
                HBox.setHgrow(spacer, Priority.ALWAYS);

                Label statusLabel = new Label(item.status.toUpperCase());
                statusLabel.setFont(ManageNgosView.getFont(12, FontWeight.BOLD));
                statusLabel.setPadding(new Insets(4, 8, 4, 8));
                statusLabel.setTextFill(Color.WHITE);
                if ("Verified".equalsIgnoreCase(item.status)) {
                    statusLabel.setStyle("-fx-background-color: " + STATUS_VERIFIED_COLOR + "; -fx-background-radius: 6;");
                } else {
                    statusLabel.setStyle("-fx-background-color: " + STATUS_PENDING_COLOR + "; -fx-background-radius: 6;");
                }
                
                content.setAlignment(Pos.CENTER);
                content.setSpacing(20);
                content.getChildren().addAll(details, spacer, statusLabel);
                content.setPadding(new Insets(15));
                content.setStyle("-fx-border-color: transparent transparent " + BORDER_COLOR + " transparent; -fx-border-width: 1;");
                setGraphic(content);
            }
        }
    }

    public ManageNgosView(Stage primaryStage, Scene previousScene) {
        this.primaryStage = primaryStage;
        this.previousScene = previousScene;
    }

    @Override
    public void start(Stage stage) {
        VBox rootLayout = new VBox(30);
        rootLayout.setPadding(new Insets(40));
        rootLayout.setStyle("-fx-background-color: " + BACKGROUND_COLOR + ";");
        rootLayout.setAlignment(Pos.TOP_CENTER);

        rootLayout.getChildren().addAll(createHeader(), createNgosList());

        ScrollPane scrollPane = new ScrollPane(rootLayout);
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        Scene scene = new Scene(scrollPane, 1280, 900);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Manage NGOs | Government Dashboard");
    }

    private Node createHeader() {
        HBox header = new HBox();
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(0, 0, 20, 0));

        Label title = new Label("Manage Partner NGOs");
        title.setFont(getFont(32, FontWeight.BOLD));

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button backButton = new Button("⬅️ Back to Dashboard");
        backButton.setFont(getFont(16, FontWeight.MEDIUM));
        backButton.setTextFill(Color.web(PRIMARY_BLUE));
        backButton.setStyle("-fx-background-color: transparent; -fx-cursor: hand; -fx-border-color: " + PRIMARY_BLUE + "; -fx-border-radius: 8;");
        backButton.setPadding(new Insets(8, 16, 8, 16));
        backButton.setOnAction(e -> {
            primaryStage.setScene(previousScene);
            primaryStage.setTitle("Government Dashboard | Disaster Command Center");
        });

        header.getChildren().addAll(title, spacer, backButton);
        return header;
    }

    private Node createNgosList() {
        VBox card = new VBox(0);
        card.setPadding(new Insets(10));
        card.setStyle("-fx-background-color: " + CARD_BACKGROUND_COLOR + "; " +
                      "-fx-background-radius: 12; " +
                      "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.05), 10, 0, 0, 4);");

        ObservableList<NgoItem> ngos = FXCollections.observableArrayList(
            new NgoItem("Red Cross Society", "Verified", "contact@redcross.org"),
            new NgoItem("Green Earth Foundation", "Verified", "info@greenearth.org"),
            new NgoItem("Community Helpers", "Pending Verification", "apply@communityhelpers.com")
        );

        ListView<NgoItem> listView = new ListView<>(ngos);
        listView.setCellFactory(param -> new NgoCell());
        listView.setStyle("-fx-background-color: transparent;");

        card.getChildren().add(listView);
        
        VBox container = new VBox(card);
        container.setAlignment(Pos.CENTER);
        
        return container;
    }

}
