package com.thinkspark.model;


import java.util.Date;

/**
 * Represents a broadcast alert.
 * This class is structured as a JavaBean for Firestore compatibility.
 */
public class Alert {

    private String title;
    private String message;
    private String priority;

    @ServerTimestamp
    private Date timestamp;

    public Alert() {}

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

    // Firestore uses these to automatically map data to and from the object.

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getPriority() { return priority; }
    public void setPriority(String priority) { this.priority = priority; }

    public Date getTimestamp() { return timestamp; }
    public void setTimestamp(Date timestamp) { this.timestamp = timestamp; }

    @Override
    public String toString() {
        return "Alert [" + priority + "]: " + title;
    }
}
