package com.thinkspark.dao;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.FieldValue;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.Query;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.thinkspark.model.Alert;
import com.thinkspark.model.FirebaseConfig;
import javafx.application.Platform;
import javafx.collections.ObservableList;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * A Data Access Object (DAO) for handling all database operations related to Alert entities.
 * This class handles both saving new alerts and listening for real-time updates.
 */
public class AlertDao {

    private final Firestore db;

    /**
     * Initializes the DAO and gets the Firestore database instance from the central config.
     */
    public AlertDao() {
        Firestore initializedDb = null;
        try {
            initializedDb = FirebaseConfig.initialize();
        } catch (IOException e) {
            System.err.println("FATAL: Firestore could not be initialized in AlertDao. " + e.getMessage());
            e.printStackTrace();
        }
        this.db = initializedDb;
    }

    /**
     * Saves an Alert object to the 'alerts' collection in Firestore.
     * This method is used by forms like BroadcastAlertsView.
     *
     * @param alert The Alert object to save.
     */
    public void saveAlert(Alert alert) {
        if (db == null) {
            System.err.println("Firestore is not initialized. Cannot save alert.");
            return;
        }

        // Manually create a Map from the Alert object for robust saving.
        Map<String, Object> alertData = new HashMap<>();
        alertData.put("title", alert.getTitle());
        alertData.put("message", alert.getMessage());
        alertData.put("priority", alert.getPriority());
        alertData.put("timestamp", FieldValue.serverTimestamp());

        // Asynchronously add the new map as a document to the "alerts" collection.
        ApiFuture<DocumentReference> future = db.collection("alerts").add(alertData);

        future.addListener(() -> {
            try {
                System.out.println("DAO Success: Alert saved to Firebase with new ID: " + future.get().getId());
            } catch (InterruptedException | ExecutionException e) {
                System.err.println("DAO Error: Failed to save alert to Firebase. " + e.getMessage());
                e.printStackTrace();
            }
        }, Runnable::run);
    }

    /**
     * Attaches a real-time listener to the 'alerts' collection in Firestore.
     * This method is used by dashboards to display live data.
     *
     * @param targetList The ObservableList from the UI to keep updated with live alerts.
     */
    public void listenForAlerts(ObservableList<Alert> targetList) {
        if (db == null) {
            System.err.println("Firestore is not initialized. Cannot listen for alerts.");
            return;
        }

        // Listen for real-time updates on the 'alerts' collection, ordering by the newest first.
        db.collection("alerts").orderBy("timestamp", Query.Direction.DESCENDING).limit(10)
            .addSnapshotListener((snapshots, e) -> {
                if (e != null) {
                    System.err.println("Listen for alerts failed: " + e);
                    return;
                }

                // Use Platform.runLater to ensure UI updates happen on the JavaFX Application Thread.
                Platform.runLater(() -> {
                    targetList.clear(); 
                    for (QueryDocumentSnapshot doc : snapshots) {
                        if (doc.exists()) {
                            // Convert the document from Firestore into an Alert object
                            Alert alert = doc.toObject(Alert.class);
                            targetList.add(alert); 
                        }
                    }
                });
            });
    }
}
