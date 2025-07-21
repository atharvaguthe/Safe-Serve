package com.thinkspark.dao;

import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.Query;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.thinkspark.model.FirebaseConfig;
import com.thinkspark.model.Task;
import javafx.application.Platform;
import javafx.collections.ObservableList;

import java.io.IOException;

public class TaskDao {

    private final Firestore db;

    public TaskDao() {
        Firestore initializedDb = null;
        try {
            initializedDb = FirebaseConfig.initialize();
        } catch (IOException e) {
            System.err.println("FATAL: Firestore could not be initialized in TaskDao. " + e.getMessage());
            e.printStackTrace();
        }
        this.db = initializedDb;
    }

    public void listenForTasks(ObservableList<Task> targetList) {
        if (db == null) {
            System.err.println("Firestore is not initialized. Cannot listen for tasks.");
            return;
        }

        db.collection("tasks").orderBy("assignedTo", Query.Direction.ASCENDING)
            .addSnapshotListener((snapshots, e) -> {
                if (e != null) {
                    System.err.println("Listen for tasks failed: " + e);
                    return;
                }

                Platform.runLater(() -> {
                    targetList.clear();
                    for (QueryDocumentSnapshot doc : snapshots) {
                        if (doc.exists()) {
                            Task task = doc.toObject(Task.class);
                            targetList.add(task);
                        }
                    }
                });
            });
    }
}