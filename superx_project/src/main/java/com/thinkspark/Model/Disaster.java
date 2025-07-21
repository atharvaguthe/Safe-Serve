package com.thinkspark.model;

import com.google.cloud.firestore.annotation.ServerTimestamp;
import java.util.Date;

public class Disaster {

    private String type;
    private String location;
    private String description;
    private String citizenName; // ADDED: To track the user who created the report

    @ServerTimestamp
    private Date timestamp;

    public Disaster() {}

    // UPDATED CONSTRUCTOR
    public Disaster(String type, String location, String description, String citizenName) {
        this.type = type;
        this.location = location;
        this.description = description;
        this.citizenName = citizenName;
    }
    
    // --- Getters and Setters ---

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    // ADDED GETTER AND SETTER for citizenName
    public String getCitizenName() { return citizenName; }
    public void setCitizenName(String citizenName) { this.citizenName = citizenName; }

    public Date getTimestamp() { return timestamp; }
    public void setTimestamp(Date timestamp) { this.timestamp = timestamp; }

    @Override
    public String toString() {
        return "Disaster Report [Type='" + type + "', Location='" + location + "']";
    }
}
