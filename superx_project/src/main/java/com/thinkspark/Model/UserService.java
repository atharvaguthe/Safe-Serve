package com.thinkspark.model;

import com.google.api.core.ApiFuture;
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
        // Add imageUrl if it exists
        if (user.getImageUrl() != null) {
            userData.put("imageUrl", user.getImageUrl());
        }

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

    /**
     * Fetches a user's details from Firestore by their email.
     * @param email The email of the user to fetch.
     * @return The User object if found, null otherwise.
     */
    public User getUserByEmail(String email) {
        if (FirebaseConfig.getDb() == null) {
            System.err.println("Firestore database is not initialized.");
            return null;
        }
        try {
            DocumentSnapshot document = FirebaseConfig.getDb().collection("users").document(email).get().get();
            if (document.exists()) {
                return document.toObject(User.class);
            }
        } catch (InterruptedException | ExecutionException e) {
            System.err.println("Error fetching user data: " + e.getMessage());
        }
        return null;
    }

    /**
     * Updates a user's profile image URL in Firestore.
     * @param email The email of the user whose profile image to update.
     * @param imageUrl The new URL of the profile image.
     * @return true if the update was successful, false otherwise.
     */
    public boolean updateUserProfileImage(String email, String imageUrl) {
        if (FirebaseConfig.getDb() == null) {
            System.err.println("Firestore database is not initialized.");
            return false;
        }
        try {
            Map<String, Object> updates = new HashMap<>();
            updates.put("imageUrl", imageUrl);
            FirebaseConfig.getDb().collection("users").document(email).update(updates).get();
            System.out.println("User profile image updated for: " + email);
            return true;
        } catch (InterruptedException | ExecutionException e) {
            System.err.println("Error updating user profile image: " + e.getMessage());
            return false;
        }
    }
}