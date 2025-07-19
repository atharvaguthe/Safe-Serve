package com.thinkspark.Controller;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONObject;

public class SignUpController {

    public Result signUp(String email, String password){
        if(email == null || email.isEmpty() || password == null || password.isEmpty()){
            return new Result(false, "Email and password cannot be empty");
        }
        try {
            String apiKey = "AIzaSyAZ0tYGpqtLb3OvVNj0RROf4eDMx5Uk330";
            URL url = new URL("https://identitytoolkit.googleapis.com/v1/accounts:signUp?key=" + apiKey);
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            JSONObject jsonRequest = new JSONObject();
            jsonRequest.put("email", email);
            jsonRequest.put("password", password);
            jsonRequest.put("returnSecureToken", true);

            try(OutputStream os = connection.getOutputStream()) {
                os.write(jsonRequest.toString().getBytes());
            }

            if(connection.getResponseCode() == 200) {
                return new Result(true, "SignUp Successful");
            } else {
                return new Result(false, "SignUp Unsuccessful");
            }
        } catch (Exception e){
            e.printStackTrace();
            return new Result(false, "SignUp Unsuccessful" + e.getMessage());

        }
    }

    

    public static class Result {
        public Boolean success;
        public String message;
        public Result(Boolean success, String message){
            this.success = success;
            this.message = message;
        }
    }
}