package com.thinkspark.model;

/**
 * Represents a broadcast alert sent by a government authority.
 * This class holds the content and priority of an official alert.
 */
public class Alert {

    private final String title;
    private final String message;
    private final String priority;

    /**
     * Constructs a new Alert object.
     *
     * @param title    The title of the alert.
     * @param message  The detailed content of the alert message.
     * @param priority The priority level (e.g., "High", "Medium", "Low").
     */
    public Alert(String title, String message, String priority) {
        this.title = title;
        this.message = message;
        this.priority = priority;
    }

    // --- Getters ---

    public String getTitle() {
        return title;
    }

    public String getMessage() {
        return message;
    }

    public String getPriority() {
        return priority;
    }

    /**
     * Provides a string representation of the alert, useful for UI lists and debugging.
     *
     * @return A formatted string with the alert's priority and title.
     */
    @Override
    public String toString() {
        return "Alert [" + priority + "]: " + title;
    }
}
