package com.thinkspark.dao;

import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.thinkspark.model.FirebaseConfig;
import com.thinkspark.view.ManageNgosView;
import javafx.application.Platform;
import javafx.collections.ObservableList;

import java.io.IOException;

public class NgoDao {

    private final Firestore db;

    public NgoDao() {
        Firestore initializedDb = null;
        try {
            initializedDb = FirebaseConfig.initialize();
        } catch (IOException e) {
            System.err.println("FATAL: Firestore could not be initialized in NgoDao. " + e.getMessage());
            e.printStackTrace();
        }
        this.db = initializedDb;
    }

    /**
     * Attaches a real-time listener to the 'users' collection in Firestore,
     * filtering for documents where the role is "NGO".
     *
     * @param targetList The ObservableList from the UI to keep updated with live NGO data.
     */
    public void listenForNgos(ObservableList<ManageNgosView.NgoItem> targetList) {
        if (db == null) {
            System.err.println("Firestore is not initialized. Cannot listen for NGOs.");
            return;
        }

        db.collection("users").whereEqualTo("role", "NGO")
            .addSnapshotListener((snapshots, e) -> {
                if (e != null) {
                    System.err.println("Listen for NGOs failed: " + e);
                    return;
                }

                Platform.runLater(() -> {
                    targetList.clear();
                    for (QueryDocumentSnapshot doc : snapshots) {
                        if (doc.exists()) {
                            targetList.add(new ManageNgosView.NgoItem(
                                doc.getString("name"),
                                "Pending Verification", // You can enhance this to fetch status from DB
                                doc.getString("email")
                            ));
                        }
                    }
                });
            });
    }
}