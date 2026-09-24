package com.quizsphere.app.models;

import java.io.Serializable;

/**
 * ==============================================================================
 * Model: User
 * Description: Represents a registered QuizSphere user.
 * Supports Role-Based Access Control (Slide 3 & 7):
 *   - "student": standard learner attempting quizzes and viewing history
 *   - "admin": educator/creator adding categories, questions, and managing quizzes
 * ==============================================================================
 */
public class User implements Serializable {

    // Primary Key uniquely identifying the user
    private int id;

    // Unique login name chosen by the user
    private String username;

    // Email address for notifications and account verification
    private String email;

    // Hashed or secured password string
    private String password;

    // Role indicating access privileges: "student" or "admin"
    private String role;

    // Default no-argument constructor required for serialization
    public User() {
    }

    // Full constructor initializing all fields
    public User(int id, String username, String email, String password, String role) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    // Constructor for registering a new user before primary key assignment
    public User(String username, String email, String password, String role) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    // Getter and Setter for ID
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    // Getter and Setter for Username
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    // Getter and Setter for Email
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    // Getter and Setter for Password
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    // Getter and Setter for User Role ("student" or "admin")
    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    // Helper method to check if user has admin/educator privileges
    public boolean isAdmin() {
        return "admin".equalsIgnoreCase(this.role);
    }
}
