package com.thinkspark.model;

import com.thinkspark.view.ManageNgosView; // Assuming NgoItem is a public static class in ManageNgosView
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * A centralized, static class to manage shared application data in memory.
 * It uses ObservableLists to allow UI components to update automatically when data changes.
 */
public class DataManager {

    // --- Private Observable Lists for Data Storage ---

    // Stores reports submitted by citizens.
    private static final ObservableList<Disaster> disasterReports = FXCollections.observableArrayList();

    // Stores alerts broadcast by the government.
    private static final ObservableList<Alert> broadcastAlerts = FXCollections.observableArrayList();

    // Stores tasks assigned by NGOs to volunteers.
    private static final ObservableList<Task> assignedTasks = FXCollections.observableArrayList();

    // Manages a list of registered volunteers.
    private static final ObservableList<Volunteer> volunteers = FXCollections.observableArrayList();

    // Manages a list of partner NGOs, tracked by the government.
    private static final ObservableList<ManageNgosView.NgoItem> ngos = FXCollections.observableArrayList();

    // Static initializer block to populate the lists with some default/dummy data upon application startup.
    static {
        // Add initial volunteers
        volunteers.addAll(
            new Volunteer("Aarav Sharma"),
            new Volunteer("Priya Singh"),
            new Volunteer("Rohan Joshi"),
            new Volunteer("Ananya Patel")
        );

        // Add initial partner NGOs
        ngos.addAll(
            new ManageNgosView.NgoItem("Sahayata Foundation", "Verified", "contact@sahayata.org"),
            new ManageNgosView.NgoItem("Hope India Initiative", "Verified", "info@hopeindia.org"),
            new ManageNgosView.NgoItem("Nashik Community Aid", "Pending Verification", "apply@nashikca.com")
        );

        // Add a default government alert
        broadcastAlerts.add(
            new Alert("Weather Warning", "Heavy rainfall expected in Nashik region for the next 48 hours. Please stay indoors.", "High")
        );

        // Add an initial disaster report for demonstration
        disasterReports.add(
            new Disaster("Medical Emergency", "Near Gangapur Dam", "Elderly person requires immediate medical attention.")
        );
    }


    // --- Public Getters to Access the Data ---

    /**
     * @return The observable list of all disaster reports.
     */
    public static ObservableList<Disaster> getDisasterReports() {
        return disasterReports;
    }

    /**
     * @return The observable list of all broadcast alerts.
     */
    public static ObservableList<Alert> getBroadcastAlerts() {
        return broadcastAlerts;
    }

    /**
     * @return The observable list of all assigned tasks.
     */
    public static ObservableList<Task> getAssignedTasks() {
        return assignedTasks;
    }

    /**
     * @return The observable list of all registered volunteers.
     */
    public static ObservableList<Volunteer> getVolunteers() {
        return volunteers;
    }

    /**
     * @return The observable list of all partner NGOs.
     */
    public static ObservableList<ManageNgosView.NgoItem> getNgos() {
        return ngos;
    }
}