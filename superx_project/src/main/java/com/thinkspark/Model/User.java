package com.thinkspark.model;

public class User {
    private String name;
    private String email;
    private String role;
    private String imageUrl; // New field for profile picture URL

    public User() {
        // No-argument constructor required for Firestore
    }

    public User(String name, String email, String role) {
        this.name = name;
        this.email = email;
        this.role = role;
        this.imageUrl = null; // Default to null if not provided
    }

    // New constructor to include imageUrl
    public User(String name, String email, String role, String imageUrl) {
        this.name = name;
        this.email = email;
        this.role = role;
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    // New getters and setters for imageUrl
    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}