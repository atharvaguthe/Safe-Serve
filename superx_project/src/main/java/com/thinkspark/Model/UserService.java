package com.thinkspark.model;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.WriteResult;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class UserService {

    public UserService() {
        try {
            // Ensure Firebase is initialized before using the service
            FirebaseConfig.initialize();
        } catch (IOException e) {
            // Using System.err for logging errors is a common practice
            System.err.println("Error initializing Firebase: " + e.getMessage());
        }
    }

    /**
     * Registers user details (name, email, role) in Firestore.
     * This method no longer handles passwords.
     * @param user The user object containing name, email, and role.
     * @return true if the data was successfully written, false otherwise.
     */
    public boolean registerUser(User user) {
        if (FirebaseConfig.getDb() == null) {
            System.err.println("Firestore database is not initialized.");
            return false;
        }

        // Create a map to store user data, excluding the password.
        Map<String, Object> userData = new HashMap<>();
        userData.put("name", user.getName());
        userData.put("email", user.getEmail());
        userData.put("role", user.getRole());

        // Use the user's email as the document ID for easy lookup.
        ApiFuture<WriteResult> future = FirebaseConfig.getDb().collection("users").document(user.getEmail()).set(userData);

        try {
            // Block and wait for the write to complete to confirm success.
            future.get();
            System.out.println("User data successfully saved to Firestore for: " + user.getEmail());
            return true;
        } catch (InterruptedException | ExecutionException e) {
            System.err.println("Error writing user data to Firestore: " + e.getMessage());
            return false;
        }
    }

}