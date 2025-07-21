package com.thinkspark.model;

public class User {
    private String name;
    private String email;
    // The 'password' field has been removed.
    private String role;

    public User() {
        // Default constructor is still useful for Firestore data mapping.
    }

    // New constructor without the password field.
    public User(String name, String email, String role) {
        this.name = name;
        this.email = email;
        this.role = role;
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

    // The getPassword() and setPassword() methods have been removed.

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}