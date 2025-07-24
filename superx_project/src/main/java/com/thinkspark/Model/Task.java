package com.thinkspark.model;

public class Task {

    private String description;
    private String assignedTo; 
    private String status;     // NEW FIELD: To track task status (e.g., "Assigned", "Completed")
    private String disasterId; // NEW FIELD: To link to the original Disaster report in Firestore
    private String firebaseDocId; // NEW FIELD: To store Firestore document ID

    public Task() {} // No-argument constructor for Firestore

    /**
     * Constructs a new Task object.
     *
     * @param description A description of the work to be done.
     * @param assignedTo  The name of the volunteer this task is assigned to.
     * @param disasterId  The Firestore document ID of the associated disaster report.
     */
    public Task(String description, String assignedTo, String disasterId) {
        this.description = description;
        this.assignedTo = assignedTo;
        this.disasterId = disasterId;
        this.status = "Assigned"; // Default status when a task is created
    }

    public String getDescription() {
        return description;
    }

    public String getAssignedTo() {
        return assignedTo;
    }

    // NEW Getters and Setters for status, disasterId, and firebaseDocId
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getDisasterId() { return disasterId; }
    public void setDisasterId(String disasterId) { this.disasterId = disasterId; }

    public String getFirebaseDocId() { return firebaseDocId; }
    public void setFirebaseDocId(String firebaseDocId) { this.firebaseDocId = firebaseDocId; }
}