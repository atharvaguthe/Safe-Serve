package com.thinkspark.model;

import java.util.Date;

/**
 * Represents a single disaster incident.
 * This class is structured as a JavaBean for Firestore compatibility.
 */
public class Disaster {

    private String type;
    private String location;
    private String description;
    
    // This annotation tells Firestore to automatically populate this field with the server's timestamp upon creation.
    @ServerTimestamp
    private Date timestamp;

    /**
     * A no-argument constructor is required by Firestore for deserialization.
     */
    public Disaster() {}

    /**
     * Constructs a new Disaster object.
     * @param type        The category of the disaster.
     * @param location    The geographical location of the incident.
     * @param description A detailed description of the situation.
     */
    public Disaster(String type, String location, String description) {
        this.type = type;
        this.location = location;
        this.description = description;
    }

    // --- Getters and Setters ---
    // Firestore uses these to automatically map data to and from the object.

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Date getTimestamp() { return timestamp; }
    public void setTimestamp(Date timestamp) { this.timestamp = timestamp; }

    @Override
    public String toString() {
        return "Disaster Report [Type='" + type + "', Location='" + location + "']";
    }
}
