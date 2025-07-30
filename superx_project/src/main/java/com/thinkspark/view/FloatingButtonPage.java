package com.thinkspark.view;

import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;

import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONObject;

public class FloatingButtonPage extends Application {

    private static final String BACKGROUND_COLOR = "#F4F7FC";
    private static final String PRIMARY_BLUE = "#005A9C";
    private static final String FONT_COLOR_DARK = "#2D3748";
    private static final String CARD_BACKGROUND_COLOR = "#FFFFFF";

    // For navigation (use dummy if you are running standalone)
    private Scene previousScene = null;
    private Stage primaryStage;

    private VBox chatHistoryBox;
    private TextField messageInput;
    private ProgressIndicator loadingIndicator;
    private Label placeholderLabel; // New: Placeholder label

    private static final String GEMINI_API_KEY = "AIzaSyC-WSo0_OCgUTU9zS_AOtJl98CCticuK_U"; // replace
    private static final String GEMINI_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent?key=";

    private static Font getFont(double size, FontWeight weight) {
        return Font.font("System", weight, size);
    }

    public FloatingButtonPage() {}

    public FloatingButtonPage(Stage primaryStage, Scene previousScene) {
        this.primaryStage = primaryStage;
        this.previousScene = previousScene;
    }

    @Override
    public void start(Stage stage) {
        this.primaryStage = stage;
        // Main layout
        VBox rootLayout = new VBox(20);
        rootLayout.setPadding(new Insets(20));
        rootLayout.setStyle("-fx-background-color: " + BACKGROUND_COLOR + ";");

        // Header
        HBox header = new HBox();
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(0, 0, 10, 0));

        Label title = new Label("🤖 AI Chatbot");
        title.setFont(getFont(32, FontWeight.BOLD));
        title.setTextFill(Color.web(FONT_COLOR_DARK));

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button backButton = new Button("⬅️ Back to Home");
        backButton.setFont(getFont(16, FontWeight.MEDIUM));
        backButton.setTextFill(Color.web(PRIMARY_BLUE));
        backButton.setStyle("-fx-background-color: transparent; -fx-cursor: hand; -fx-border-color: " + PRIMARY_BLUE + "; -fx-border-radius: 8;");
        backButton.setPadding(new Insets(8, 16, 8, 16));
        backButton.setOnAction(e -> {
            if (previousScene != null) {
                primaryStage.setScene(previousScene);
                primaryStage.setTitle("Safe Serve");
            }
        });

        header.getChildren().addAll(title, spacer, backButton);

        // Chat History area & scroll pane
        chatHistoryBox = new VBox(10);
        chatHistoryBox.setPadding(new Insets(15));
        chatHistoryBox.setStyle("-fx-background-color: " + CARD_BACKGROUND_COLOR + "; " +
                "-fx-background-radius: 12; " +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.05), 10, 0, 0, 4);");

        // --- NEW: Placeholder Label ---
        placeholderLabel = new Label("Ready when you are!");
        placeholderLabel.setFont(getFont(24, FontWeight.LIGHT));
        placeholderLabel.setTextFill(Color.web("#6C757D")); // A subtle grey
        // Wrap the placeholder in an HBox to center it easily
        HBox placeholderContainer = new HBox(placeholderLabel);
        placeholderContainer.setAlignment(Pos.CENTER);
        // Make sure it grows to fill vertical space, so it truly centers vertically
        VBox.setVgrow(placeholderContainer, Priority.ALWAYS);
        chatHistoryBox.getChildren().add(placeholderContainer);
        // --- END NEW ---

        ScrollPane chatScrollPane = new ScrollPane(chatHistoryBox);
        chatScrollPane.setFitToWidth(true);
        chatScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        chatScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        chatScrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent;");
        chatScrollPane.setPadding(new Insets(0));
        // We'll let the placeholderContainer handle the vertical growth initially
        // VBox.setVgrow(chatScrollPane, Priority.ALWAYS); // This line is not needed here as placeholderContainer handles initial growth

        // Auto-scroll to bottom on new message
        chatHistoryBox.heightProperty().addListener((obs, oldVal, newVal) -> chatScrollPane.setVvalue(1.0));

        // Input and Send area
        messageInput = new TextField();
        messageInput.setPromptText("Type your message here...");
        messageInput.setFont(getFont(16, FontWeight.NORMAL));
        messageInput.setPrefHeight(40);
        HBox.setHgrow(messageInput, Priority.ALWAYS);

        Button sendButton = new Button("Send");
        sendButton.setFont(getFont(16, FontWeight.BOLD));
        sendButton.setTextFill(Color.web(FONT_COLOR_DARK));
        sendButton.setStyle("-fx-background-color: " + PRIMARY_BLUE + "; -fx-background-radius: 8; -fx-cursor: hand;");
        sendButton.setPadding(new Insets(8, 15, 8, 15));
        sendButton.setOnAction(e -> sendMessage());

        // Allow pressing Enter to send
        messageInput.setOnAction(e -> sendMessage());

        HBox inputArea = new HBox(10, messageInput, sendButton);
        inputArea.setAlignment(Pos.CENTER_LEFT);
        inputArea.setPadding(new Insets(10, 0, 0, 0));
        inputArea.setMaxWidth(Double.MAX_VALUE);

        VBox.setMargin(inputArea, new Insets(0, 0, 100, 0));

        // Add everything to the root layout
        rootLayout.getChildren().addAll(header, chatScrollPane, inputArea);

        // Use a resizable scene
        Scene scene = new Scene(rootLayout, 1920,1080); // Start at a convenient size
        stage.setScene(scene);
        stage.setTitle("Safe Serve | AI Chatbot");
        stage.setResizable(true);
        stage.show();
    }

    // Handles sending user messages
    private void sendMessage() {
        String userMessage = messageInput.getText().trim();
        if (userMessage.isEmpty()) {
            return;
        }

        // --- NEW: Remove placeholder if it's the first message ---
        if (placeholderLabel != null && chatHistoryBox.getChildren().contains(placeholderLabel.getParent())) {
            // Remove the HBox container of the placeholder label
            chatHistoryBox.getChildren().remove(placeholderLabel.getParent());
            placeholderLabel = null; // Set to null so we don't try to remove it again
            // Ensure chatScrollPane can now grow as expected if the placeholder was removed
            VBox.setVgrow(chatHistoryBox.getParent(), Priority.ALWAYS); // Apply to ScrollPane
        }
        // --- END NEW ---

        displayMessage(userMessage, true);
        messageInput.clear();
        messageInput.setDisable(true);
        showLoading(true);

        new Thread(() -> {
            String aiResponse = "Error: Could not get response.";
            try {
                aiResponse = callGemini(userMessage);
            } catch (Exception e) {
                System.err.println("Gemini API Error: " + e.getMessage());
                e.printStackTrace();
                aiResponse = "Error: Failed to get response from AI. Please try again later. (Details: " + e.getMessage() + ")";
            }
            String finalAiResponse = aiResponse;
            Platform.runLater(() -> {
                showLoading(false);
                displayMessage(finalAiResponse, false);
                messageInput.setDisable(false);
                messageInput.requestFocus();
            });
        }).start();
    }

    // Displays a chat message bubble on the UI
    private void displayMessage(String message, boolean isUser) {
        Label messageLabel = new Label(message);
        messageLabel.setFont(getFont(16, FontWeight.NORMAL));
        messageLabel.setWrapText(true);
        messageLabel.setPadding(new Insets(8, 12, 8, 12));

        // Create a wrapper HBox for the message bubble to control its width
        HBox messageWrapper = new HBox();
        messageWrapper.setMaxWidth(Region.USE_PREF_SIZE); // Allows the wrapper to shrink to content

        HBox messageBubble = new HBox(messageLabel);
        messageBubble.setMaxWidth(400); // Still keeps the bubble from being too wide

        if (isUser) {
            messageLabel.setTextFill(Color.WHITE);
            messageBubble.setStyle("-fx-background-color: " + PRIMARY_BLUE + "; -fx-background-radius: 15;");
            // Align the messageWrapper (which contains the bubble) to the center
            messageWrapper.setAlignment(Pos.CENTER);
        } else {
            messageLabel.setTextFill(Color.web(FONT_COLOR_DARK));
            messageBubble.setStyle("-fx-background-color: #e9ecef; -fx-background-radius: 15;");
            // Align the messageWrapper (which contains the bubble) to the center
            messageWrapper.setAlignment(Pos.CENTER);
        }

        // Add the messageBubble to the messageWrapper
        messageWrapper.getChildren().add(messageBubble);

        messageWrapper.setOpacity(0); // Apply fade transition to the wrapper
        chatHistoryBox.getChildren().add(messageWrapper); // Add the wrapper to chat history
        FadeTransition ft = new FadeTransition(Duration.millis(500), messageWrapper);
        ft.setToValue(1.0);
        ft.play();
    }

    // Show/hide loading spinner in chat area
    private void showLoading(boolean show) {
        if (show) {
            loadingIndicator = new ProgressIndicator();
            loadingIndicator.setPrefSize(30, 30);
            HBox loadingContainer = new HBox(loadingIndicator);
            // Center the loading indicator
            loadingContainer.setAlignment(Pos.CENTER);
            loadingContainer.setPadding(new Insets(5, 0, 5, 0));
            chatHistoryBox.getChildren().add(loadingContainer);
        } else {
            // Remove last ProgressIndicator (if exists)
            if (loadingIndicator != null) {
                for (Node node : chatHistoryBox.getChildren()) {
                    if (node instanceof HBox && ((HBox) node).getChildren().contains(loadingIndicator)) {
                        chatHistoryBox.getChildren().remove(node);
                        break;
                    }
                }
            }
        }
    }

    // Call Gemini API (replace API key!)
    private String callGemini(String userInput) throws IOException {
        OkHttpClient client = new OkHttpClient();

        JSONObject content = new JSONObject();
        content.put("parts", new JSONArray().put(new JSONObject().put("text", userInput)));

        JSONObject requestBodyJson = new JSONObject();
        requestBodyJson.put("contents", new JSONArray().put(content));

        RequestBody body = RequestBody.create(
                requestBodyJson.toString(),
                MediaType.parse("application/json")
        );

        Request request = new Request.Builder()
                .url(GEMINI_URL + GEMINI_API_KEY)
                .post(body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                String err = response.body().string();
                System.err.println("Gemini API Error: " + err);
                throw new IOException("API call failed with code: " + response.code() + " and body: " + err);
            }

            String responseBody = response.body().string();
            JSONObject json = new JSONObject(responseBody);

            return json
                    .getJSONArray("candidates")
                    .getJSONObject(0)
                    .getJSONObject("content")
                    .getJSONArray("parts")
                    .getJSONObject(0)
                    .getString("text")
                    .trim();
        }
    }

    // Optional for error popups
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}