package com.thinkspark.Controller;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import com.thinkspark.model.FirebaseConfig;

import java.io.IOException;

public class DeleteAccountController {

    /**
     * Deletes a user's account from Firebase Authentication using their email.
     *
     * @param email The email of the user to delete.
     * @return true if successful, false otherwise.
     */
    public boolean deleteAccount(String email) {
        if (email == null || email.trim().isEmpty()) {
            System.err.println("Email cannot be null or empty.");
            return false;
        }

        try {
            // Initialize Firebase
            FirebaseConfig.initialize();

            // Get the user record by email to find their UID
            UserRecord userRecord = FirebaseAuth.getInstance().getUserByEmail(email);
            String uid = userRecord.getUid();

            // Use the UID to delete the user
            FirebaseAuth.getInstance().deleteUser(uid);

            System.out.println("Successfully deleted user: " + email);
            return true;
        } catch (FirebaseAuthException e) {
            // This exception is thrown for Firebase-specific errors,
            // like USER_NOT_FOUND
            System.err.println("Firebase Auth error while deleting account: " + e.getMessage());
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            // This is for the FirebaseConfig.initialize() call
            System.err.println("Error initializing Firebase: " + e.getMessage());
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            // Catch any other unexpected exceptions
            System.err.println("An unexpected error occurred while deleting the account.");
            e.printStackTrace();
            return false;
        }
    }
}