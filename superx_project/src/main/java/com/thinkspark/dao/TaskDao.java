package com.thinkspark.dao;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.Query;
import com.google.cloud.firestore.DocumentReference;
import com.thinkspark.model.FirebaseConfig;
import com.thinkspark.model.Task;
import javafx.application.Platform;
import javafx.collections.ObservableList;
// Import DocumentChange
import com.google.cloud.firestore.DocumentChange;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors; // NEW IMPORT

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
        taskData.put("assignedTo", task.getAssignedTo()); // This field is crucial for filtering!
        taskData.put("status", task.getStatus()); // Save initial status (Assigned)
        taskData.put("disasterId", task.getDisasterId()); // Save linked disaster ID

        System.out.println("DEBUG DAO: Saving Task Data: " + taskData); // DEBUG PRINT

        ApiFuture<DocumentReference> future = db.collection("tasks").add(taskData);

        // Execute the listener on a separate single-thread executor to prevent blocking the UI.
        future.addListener(() -> {
            try {
                System.out.println("DAO Success: Task saved to Firebase with new ID: " + future.get().getId());
            } catch (InterruptedException | ExecutionException e) {
                System.err.println("DAO Error: Failed to save task to Firebase. " + e.getMessage());
                e.printStackTrace();
            }
        }, Executors.newSingleThreadExecutor()); // Corrected: Run on a background thread
    }

    // NEW METHOD: Update the status of a task
    public void updateTaskStatus(String taskId, String newStatus) {
        if (db == null) {
            System.err.println("Firestore is not initialized. Cannot update task status.");
            return;
        }
        // Execute the listener on a separate single-thread executor to prevent blocking the UI.
        db.collection("tasks").document(taskId).update("status", newStatus)
            .addListener(() -> {
                System.out.println("Task " + taskId + " status updated to " + newStatus);
            }, Executors.newSingleThreadExecutor()); // Corrected: Run on a background thread
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
                    // Process only the changes
                    for (DocumentChange dc : snapshots.getDocumentChanges()) {
                        Task task = dc.getDocument().toObject(Task.class);
                        task.setFirebaseDocId(dc.getDocument().getId()); // Set Firebase document ID

                        switch (dc.getType()) {
                            case ADDED:
                                // Add at the correct index for ordered lists, or just add if order doesn't strictly matter here
                                targetList.add(task);
                                System.out.println("DEBUG DAO: Added task: " + task.getDescription() + " (Status: " + task.getStatus() + ", AssignedTo: " + task.getAssignedTo() + ", ID: " + task.getFirebaseDocId() + ")");
                                break;
                            case MODIFIED:
                                // Find and replace the modified item
                                for (int i = 0; i < targetList.size(); i++) {
                                    if (targetList.get(i).getFirebaseDocId().equals(task.getFirebaseDocId())) {
                                        targetList.set(i, task);
                                        System.out.println("DEBUG DAO: Modified task: " + task.getDescription() + " (Status: " + task.getStatus() + ", AssignedTo: " + task.getAssignedTo() + ", ID: " + task.getFirebaseDocId() + ")");
                                        break;
                                    }
                                }
                                break;
                            case REMOVED:
                                // Find and remove the deleted item
                                targetList.removeIf(t -> t.getFirebaseDocId().equals(task.getFirebaseDocId()));
                                System.out.println("DEBUG DAO: Removed task: " + task.getDescription() + " (Status: " + task.getStatus() + ", AssignedTo: " + task.getAssignedTo() + ", ID: " + task.getFirebaseDocId() + ")");
                                break;
                        }
                    }
                    System.out.println("DEBUG DAO: targetList size after update: " + targetList.size());
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
                    // Process only the changes
                    for (DocumentChange dc : snapshots.getDocumentChanges()) {
                        Task task = dc.getDocument().toObject(Task.class);
                        task.setFirebaseDocId(dc.getDocument().getId()); // Set Firebase document ID
                        task.setStatus(dc.getDocument().getString("status")); // Ensure status is explicitly set from document

                        switch (dc.getType()) {
                            case ADDED:
                                targetList.add(task);
                                System.out.println("DEBUG DAO (All Tasks): Added task: " + task.getDescription() + " (Status: " + task.getStatus() + ", AssignedTo: " + task.getAssignedTo() + ", ID: " + task.getFirebaseDocId() + ")");
                                break;
                            case MODIFIED:
                                for (int i = 0; i < targetList.size(); i++) {
                                    if (targetList.get(i).getFirebaseDocId().equals(task.getFirebaseDocId())) {
                                        targetList.set(i, task);
                                        System.out.println("DEBUG DAO (All Tasks): Modified task: " + task.getDescription() + " (Status: " + task.getStatus() + ", AssignedTo: " + task.getAssignedTo() + ", ID: " + task.getFirebaseDocId() + ")");
                                        break;
                                    }
                                }
                                break;
                            case REMOVED:
                                targetList.removeIf(t -> t.getFirebaseDocId().equals(task.getFirebaseDocId()));
                                System.out.println("DEBUG DAO (All Tasks): Removed task: " + task.getDescription() + " (Status: " + task.getStatus() + ", AssignedTo: " + task.getAssignedTo() + ", ID: " + task.getFirebaseDocId() + ")");
                                break;
                        }
                    }
                    System.out.println("DEBUG DAO (All Tasks): targetList size after update: " + targetList.size());
                });
            });
    }
}