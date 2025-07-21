package com.thinkspark.dao;

import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.thinkspark.model.FirebaseConfig;
import com.thinkspark.model.Volunteer;
import javafx.application.Platform;
import javafx.collections.ObservableList;

import java.io.IOException;

public class VolunteerDao {

    private final Firestore db;

    public VolunteerDao() {
        Firestore initializedDb = null;
        try {
            initializedDb = FirebaseConfig.initialize();
        } catch (IOException e) {
            System.err.println("FATAL: Firestore could not be initialized in VolunteerDao. " + e.getMessage());
            e.printStackTrace();
        }
        this.db = initializedDb;
    }

    /**
     * Attaches a real-time listener to the 'users' collection in Firestore,
     * filtering for documents where the role is "Volunteer".
     *
     * @param targetList The ObservableList from the UI to keep updated with live volunteer data.
     */
    public void listenForVolunteers(ObservableList<Volunteer> targetList) {
        if (db == null) {
            System.err.println("Firestore is not initialized. Cannot listen for volunteers.");
            return;
        }

        db.collection("users").whereEqualTo("role", "Volunteer")
            .addSnapshotListener((snapshots, e) -> {
                if (e != null) {
                    System.err.println("Listen for volunteers failed: " + e);
                    return;
                }

                Platform.runLater(() -> {
                    targetList.clear();
                    for (QueryDocumentSnapshot doc : snapshots) {
                        if (doc.exists()) {
                            Volunteer volunteer = doc.toObject(Volunteer.class);
                            targetList.add(volunteer);
                        }
                    }
                });
            });
    }
}