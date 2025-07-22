package com.thinkspark.model;

/**
 * Represents a specific task assigned to a volunteer.
 * This links a disaster-related need to a specific person for action.
 */
public class Task {

    private final String description;
    private final String assignedTo; // Stores the name of the volunteer

    /**
     * Constructs a new Task object.
     *
     * @param description A description of the work to be done.
     * @param assignedTo  The name of the volunteer this task is assigned to.
     */
    public Task(String description, String assignedTo) {
        this.description = description;
        this.assignedTo = assignedTo;
    }

    // --- Getters ---

    public String getDescription() {
        return description;
    }

    public String getAssignedTo() {
        return assignedTo;
    }
}