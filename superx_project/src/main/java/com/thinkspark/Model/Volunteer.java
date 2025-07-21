package com.thinkspark.model;

/**
 * Represents a volunteer in the system.
 * This simple model holds information about an individual volunteer.
 */
public class Volunteer {

    private final String name;

    /**
     * Constructs a new Volunteer object.
     *
     * @param name The full name of the volunteer.
     */
    public Volunteer(String name) {
        this.name = name;
    }

    // --- Getter ---

    public String getName() {
        return name;
    }

    /**
     * Overridden to provide a user-friendly string representation of the object.
     * This is particularly useful for displaying volunteer names in UI components
     * like ComboBoxes or ChoiceDialogs.
     *
     * @return The name of the volunteer.
     */
    @Override
    public String toString() {
        return name;
    }
}
