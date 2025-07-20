package com.thinkspark.Controller;

import com.thinkspark.model.User;
import com.thinkspark.model.UserService;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONObject;

public class SignUpController {

    public Result signUp(String name, String email, String password, String role) {
        if (email == null || email.isEmpty() || password == null || password.isEmpty() || name == null || name.isEmpty() || role == null || role.isEmpty()) {
            return new Result(false, "All fields are required and cannot be empty");
        }
        try {
            // This is a placeholder API key and should be kept secure in a real application
            String apiKey = "AIzaSyAZ0tYGpqtLb3OvVNj0RROf4eDMx5Uk330";
            URL url = new URL("https://identitytoolkit.googleapis.com/v1/accounts:signUp?key=" + apiKey);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            JSONObject jsonRequest = new JSONObject();
            jsonRequest.put("email", email);
            jsonRequest.put("password", password);
            jsonRequest.put("returnSecureToken", true);

            try (OutputStream os = connection.getOutputStream()) {
                os.write(jsonRequest.toString().getBytes());
            }

            if (connection.getResponseCode() == 200) {
                // If sign-up is successful, register the user in Firestore
                UserService userService = new UserService();
                User newUser = new User(name, email, password, role); // Storing the password in Firestore is not recommended for production
                boolean isRegistered = userService.registerUser(newUser);

                if (isRegistered) {
                    return new Result(true, "SignUp Successful");
                } else {
                    // This part of the logic might need refinement based on how you want to handle
                    // a failed Firestore registration after a successful authentication.
                    // For example, you might want to delete the newly created auth user.
                    return new Result(false, "SignUp successful, but failed to save user data.");
                }
            } else {
                // It's helpful to return the error message from the API
                // For a more robust solution, you would parse the error stream from the connection
                return new Result(false, "SignUp Unsuccessful: " + connection.getResponseMessage());
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "SignUp Unsuccessful: " + e.getMessage());
        }
    }

    public static class Result {
        public Boolean success;
        public String message;

        public Result(Boolean success, String message) {
            this.success = success;
            this.message = message;
        }
    }
}