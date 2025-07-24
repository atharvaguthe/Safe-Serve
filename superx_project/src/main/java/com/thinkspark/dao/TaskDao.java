package com.thinkspark.dao;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.Query;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.DocumentReference;
import com.thinkspark.model.FirebaseConfig;
import com.thinkspark.model.Task;
import javafx.application.Platform;
import javafx.collections.ObservableList;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

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

    // NEW METHOD: Save a Task object to Firestore
    public void saveTask(Task task) {
        if (db == null) {
            System.err.println("Firestore is not initialized. Cannot save task.");
            return;
        }

        Map<String, Object> taskData = new HashMap<>();
        taskData.put("description", task.getDescription());
        taskData.put("assignedTo", task.getAssignedTo());
        taskData.put("status", task.getStatus()); // Save initial status (Assigned)
        taskData.put("disasterId", task.getDisasterId()); // Save linked disaster ID

        ApiFuture<DocumentReference> future = db.collection("tasks").add(taskData);

        future.addListener(() -> {
            try {
                System.out.println("DAO Success: Task saved to Firebase with new ID: " + future.get().getId());
            } catch (InterruptedException | ExecutionException e) {
                System.err.println("DAO Error: Failed to save task to Firebase. " + e.getMessage());
                e.printStackTrace();
            }
        }, Runnable::run);
    }

    // NEW METHOD: Update the status of a task
    public void updateTaskStatus(String taskId, String newStatus) {
        if (db == null) {
            System.err.println("Firestore is not initialized. Cannot update task status.");
            return;
        }
        db.collection("tasks").document(taskId).update("status", newStatus)
            .addListener(() -> {
                System.out.println("Task " + taskId + " status updated to " + newStatus);
            }, Runnable::run);
    }

    // Modified to filter by assignedTo and include firebaseDocId
    public void listenForTasks(ObservableList<Task> targetList, String assignedToVolunteerName) {
        if (db == null) {
            System.err.println("Firestore is not initialized. Cannot listen for tasks.");
            return;
        }

        db.collection("tasks")
            .whereEqualTo("assignedTo", assignedToVolunteerName) // Filter by volunteer
            .orderBy("status", Query.Direction.ASCENDING) // Order to show assigned before completed
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
                            task.setFirebaseDocId(doc.getId()); // NEW: Set Firebase document ID
                            targetList.add(task);
                        }
                    }
                });
            });
    }

    // Overloaded method for AllRequestsView which doesn't filter by volunteer name
    public void listenForTasks(ObservableList<Task> targetList) {
        if (db == null) {
            System.err.println("Firestore is not initialized. Cannot listen for tasks.");
            return;
        }

        db.collection("tasks")
            .orderBy("status", Query.Direction.ASCENDING) // Order to show assigned before completed
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
                            task.setFirebaseDocId(doc.getId()); // Set Firebase document ID
                            targetList.add(task);
                        }
                    }
                });
            });
    }
}