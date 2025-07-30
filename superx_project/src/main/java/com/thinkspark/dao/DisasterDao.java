package com.thinkspark.dao;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.thinkspark.model.Disaster;
import com.thinkspark.model.FirebaseConfig;
import javafx.application.Platform;
import javafx.collections.ObservableList;
// Import DocumentChange
import com.google.cloud.firestore.DocumentChange;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors; // NEW IMPORT

public class DisasterDao {

    private final Firestore db;

    public DisasterDao() {
        Firestore initializedDb = null;
        try {
            initializedDb = FirebaseConfig.initialize();
        } catch (IOException e) {
            System.err.println("FATAL: Firestore could not be initialized in DisasterDao. " + e.getMessage());
            e.printStackTrace();
        }
        this.db = initializedDb;
    }

    public void saveDisasterReport(Disaster disaster) {
        if (db == null) {
            System.err.println("Firestore is not initialized. Cannot save report.");
            return;
        }

        Map<String, Object> disasterData = new HashMap<>();
        disasterData.put("type", disaster.getType());
        disasterData.put("location", disaster.getLocation());
        disasterData.put("description", disaster.getDescription());
        disasterData.put("citizenName", disaster.getCitizenName());
        disasterData.put("imageUrl", disaster.getImageUrl());
        disasterData.put("status", disaster.getStatus()); // NEW: Save initial status
        disasterData.put("timestamp", FieldValue.serverTimestamp());

        ApiFuture<DocumentReference> future = db.collection("disasters").add(disasterData);

        // Execute the listener on a separate single-thread executor to prevent blocking the UI.
        future.addListener(() -> {
            try {
                System.out.println("DAO Success: Disaster report saved to Firebase with new ID: " + future.get().getId());
            } catch (InterruptedException | ExecutionException e) {
                System.err.println("DAO Error: Failed to save disaster report to Firebase. " + e.getMessage());
                e.printStackTrace();
            }
        }, Executors.newSingleThreadExecutor());
    }

    // NEW METHOD: Update the status of a disaster report
    public void updateDisasterStatus(String disasterId, String newStatus) {
        if (db == null) {
            System.err.println("Firestore is not initialized. Cannot update disaster status.");
            return;
        }
        // Execute the listener on a separate single-thread executor to prevent blocking the UI.
        db.collection("disasters").document(disasterId).update("status", newStatus)
            .addListener(() -> {
                System.out.println("Disaster report " + disasterId + " status updated to " + newStatus);
            }, Executors.newSingleThreadExecutor()); // Corrected: Run on a background thread
    }

    public void listenForDisasterReports(ObservableList<Disaster> targetList) {
        if (db == null) {
            System.err.println("Firestore is not initialized. Cannot listen for disaster reports.");
            return;
        }

        db.collection("disasters").orderBy("timestamp", Query.Direction.DESCENDING).limit(10)
            .addSnapshotListener((snapshots, e) -> {
                if (e != null) {
                    System.err.println("Listen for disaster reports failed: " + e);
                    return;
                }

                Platform.runLater(() -> {
                    // Process only the changes
                    for (DocumentChange dc : snapshots.getDocumentChanges()) {
                        Disaster disaster = dc.getDocument().toObject(Disaster.class);
                        disaster.setFirebaseDocId(dc.getDocument().getId()); // Set the Firebase document ID
                        disaster.setStatus(dc.getDocument().getString("status"));

                        switch (dc.getType()) {
                            case ADDED:
                                targetList.add(disaster);
                                break;
                            case MODIFIED:
                                for (int i = 0; i < targetList.size(); i++) {
                                    if (targetList.get(i).getFirebaseDocId().equals(disaster.getFirebaseDocId())) {
                                        targetList.set(i, disaster);
                                        break;
                                    }
                                }
                                break;
                            case REMOVED:
                                targetList.removeIf(d -> d.getFirebaseDocId().equals(disaster.getFirebaseDocId()));
                                break;
                        }
                    }
                });
            });
    }

    public void listenForDisastersByCitizen(ObservableList<Disaster> targetList, String citizenName) {
        if (db == null) {
            System.err.println("Firestore is not initialized. Cannot listen for disaster reports.");
            return;
        }

        db.collection("disasters")
            .whereEqualTo("citizenName", citizenName)
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .addSnapshotListener((snapshots, e) -> {
                if (e != null) {
                    System.err.println("Listen for citizen's disaster reports failed: " + e);
                    return;
                }

                Platform.runLater(() -> {
                    // Process only the changes
                    for (DocumentChange dc : snapshots.getDocumentChanges()) {
                        Disaster disaster = dc.getDocument().toObject(Disaster.class);
                        disaster.setFirebaseDocId(dc.getDocument().getId()); // NEW: Set the Firebase document ID
                        disaster.setStatus(dc.getDocument().getString("status")); // NEW: Set the status from Firestore

                        switch (dc.getType()) {
                            case ADDED:
                                targetList.add(disaster);
                                break;
                            case MODIFIED:
                                for (int i = 0; i < targetList.size(); i++) {
                                    if (targetList.get(i).getFirebaseDocId().equals(disaster.getFirebaseDocId())) {
                                        targetList.set(i, disaster);
                                        break;
                                    }
                                }
                                break;
                            case REMOVED:
                                targetList.removeIf(d -> d.getFirebaseDocId().equals(disaster.getFirebaseDocId()));
                                break;
                        }
                    }
                });
            });
    }
}