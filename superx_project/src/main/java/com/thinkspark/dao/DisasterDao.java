package com.thinkspark.dao;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.thinkspark.model.Disaster;
import com.thinkspark.model.FirebaseConfig;
import javafx.application.Platform;
import javafx.collections.ObservableList;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

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
        disasterData.put("imageUrl", disaster.getImageUrl()); // ADDED
        disasterData.put("timestamp", FieldValue.serverTimestamp());

        ApiFuture<DocumentReference> future = db.collection("disasters").add(disasterData);

        future.addListener(() -> {
            try {
                System.out.println("DAO Success: Disaster report saved to Firebase with new ID: " + future.get().getId());
            } catch (InterruptedException | ExecutionException e) {
                System.err.println("DAO Error: Failed to save disaster report to Firebase. " + e.getMessage());
                e.printStackTrace();
            }
        }, Runnable::run);
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
                    targetList.clear();
                    for (QueryDocumentSnapshot doc : snapshots) {
                        if (doc.exists()) {
                            Disaster disaster = doc.toObject(Disaster.class);
                            targetList.add(disaster);
                        }
                    }
                });
            });
    }

    // NEW METHOD to listen for requests by a specific citizen
    public void listenForDisastersByCitizen(ObservableList<Disaster> targetList, String citizenName) {
        if (db == null) {
            System.err.println("Firestore is not initialized. Cannot listen for disaster reports.");
            return;
        }

        db.collection("disasters")
            .whereEqualTo("citizenName", citizenName) // Filter by citizenName
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .addSnapshotListener((snapshots, e) -> {
                if (e != null) {
                    System.err.println("Listen for citizen's disaster reports failed: " + e);
                    return;
                }

                Platform.runLater(() -> {
                    targetList.clear();
                    for (QueryDocumentSnapshot doc : snapshots) {
                        if (doc.exists()) {
                            Disaster disaster = doc.toObject(Disaster.class);
                            targetList.add(disaster);
                        }
                    }
                });
            });
    }
}