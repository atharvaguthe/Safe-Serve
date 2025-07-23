package com.thinkspark.model;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;

import java.io.FileInputStream;
import java.io.IOException;

public class FirebaseConfig {

    private static Firestore db = null;

    public static Firestore initialize() throws IOException {
        if (db == null) {
            // IMPORTANT: Replace this path with the actual path to your Firebase service account key JSON file.
            String serviceAccountKeyPath = "superx_project\\src\\main\\resources\\safe-serve-private.json";

            FileInputStream serviceAccount = new FileInputStream(serviceAccountKeyPath);

            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();

            FirebaseApp.initializeApp(options);
            System.out.println("Firebase is initialized");
            db = FirestoreClient.getFirestore();
        }
        return db;
    }

    public static Firestore getDb() {
        return db;
    }
}
