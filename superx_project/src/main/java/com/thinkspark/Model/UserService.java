package com.thinkspark.model;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.WriteResult;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class UserService {

    public UserService() {
        try {
            FirebaseConfig.initialize();
        } catch (IOException e) {
            System.err.println("Error initializing Firebase: " + e.getMessage());
        }
    }

    public boolean registerUser(User user) {
        if (FirebaseConfig.getDb() == null) {
            System.err.println("Firestore database is not initialized.");
            return false;
        }

        // Create a map to store user data
        Map<String, Object> userData = new HashMap<>();
        userData.put("name", user.getName());
        userData.put("email", user.getEmail());
        userData.put("password", user.getPassword()); // Remember to hash passwords in a real app
        userData.put("role", user.getRole());

        // Add a new document with the user's email as the ID
        ApiFuture<WriteResult> future = FirebaseConfig.getDb().collection("users").document(user.getEmail()).set(userData);

        try {
            // Block and wait for the write to complete
            System.out.println("Update time : " + future.get().getUpdateTime());
            return true;
        } catch (InterruptedException | ExecutionException e) {
            System.err.println("Error writing to Firestore: " + e.getMessage());
            return false;
        }
    }

    /**
     * Validates a user's login credentials against Firestore.
     * @param email The user's email.
     * @param password The user's password.
     * @param role The user's selected role.
     * @return true if the credentials are valid, false otherwise.
     */
    public boolean validateLogin(String email, String password, String role) {
        if (FirebaseConfig.getDb() == null) {
            System.err.println("Firestore database is not initialized.");
            return false;
        }

        DocumentReference docRef = FirebaseConfig.getDb().collection("users").document(email);
        ApiFuture<DocumentSnapshot> future = docRef.get();

        try {
            DocumentSnapshot document = future.get();
            if (document.exists()) {
                String storedPassword = document.getString("password");
                String storedRole = document.getString("role");

                // IMPORTANT: In a real application, you would compare hashed passwords.
                // This is a simplified check for demonstration purposes.
                return password.equals(storedPassword) && role.equals(storedRole);
            } else {
                System.out.println("No such document!");
                return false;
            }
        } catch (InterruptedException | ExecutionException e) {
            System.err.println("Error retrieving user data: " + e.getMessage());
            return false;
        }
    }
}
