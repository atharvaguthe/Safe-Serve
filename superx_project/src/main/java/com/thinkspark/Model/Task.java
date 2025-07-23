package com.thinkspark.model;

public class Task {

    private final String description;
    private final String assignedTo; 

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


    public String getDescription() {
        return description;
    }

    public String getAssignedTo() {
        return assignedTo;
    }
}