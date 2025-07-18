package com.thinkspark.Controller;


import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONObject;

public class LoginController {
    
    public Result login(String email, String password){
        try {
            String apiKey = "AIzaSyC2vOi81GM5zEsvqx4H28se5LvZdkn1t-Q";
            URL url = new URL("https://identitytoolkit.googleapis.com/v1/accounts:signInWithPassword?key=" + apiKey);
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            JSONObject jsonRequest = new JSONObject();
            jsonRequest.put("email", email);
            jsonRequest.put("password", password);
            jsonRequest.put("returnSecureToken", true);

            try(OutputStream os = connection.getOutputStream()) {
                byte[] input = jsonRequest.toString().getBytes();
                os.write(input,0,input.length);
            }

            if(connection.getResponseCode() == 200) {
                return new Result(true, "Login Successful");
            } else {
                return new Result(false, "Login Unsuccessful");
            }
        } catch (Exception e){
            e.printStackTrace();
            return new Result(false, "Login Unsuccessful" + e.getMessage());

        }
    }

    public static class Result {
        public final boolean success;
        public final String message;
        public Result(boolean success, String message) {
            this.success = success;
            this.message = message;
        }

    }
}
