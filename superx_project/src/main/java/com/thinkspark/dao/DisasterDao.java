package com.thinkspark.dao;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import com.thinkspark.model.Disaster;
import com.thinkspark.model.FirebaseConfig;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

/**
 * A Data Access Object (DAO) for handling all database operations related to Disaster entities.
 * This class abstracts the low-level details of communicating with Firestore for disaster data.
 */
public class DisasterDao {

    private final Firestore db;

    /**
     * Initializes the DAO and gets the Firestore database instance from the central config.
     */
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

    /**
     * Saves a Disaster object to the 'disasters' collection in Firestore.
     *
     * @param disaster The Disaster object to save.
     */
    public void saveDisasterReport(Disaster disaster) {
        if (db == null) {
            System.err.println("Firestore is not initialized. Cannot save report.");
            return;
        }

        // Asynchronously add a new document with a Firestore-generated ID to the "disasters" collection.
        ApiFuture<WriteResult> future = db.collection("disasters").add(disaster);

        // Add a listener to log the result of the asynchronous operation.
        future.addListener(() -> {
            try {
                System.out.println("DAO Success: Disaster report saved to Firebase with update time: " + future.get().getUpdateTime());
            } catch (InterruptedException | ExecutionException e) {
                System.err.println("DAO Error: Failed to save disaster report to Firebase. " + e.getMessage());
                e.printStackTrace();
            }
        }, Runnable::run); // Use a direct executor to run the callback immediately.
    }

    // You can add other methods here in the future, for example:
    // public List<Disaster> getAllDisasters() { ... }
    // public void updateDisaster(String documentId, Disaster disaster) { ... }
}
