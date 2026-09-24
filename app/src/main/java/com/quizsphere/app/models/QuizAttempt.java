package com.quizsphere.app.models;

import java.io.Serializable;

/**
 * ==============================================================================
 * Model: QuizAttempt
 * Description: Stores a complete quiz attempt by a user (Slide 7).
 * Records score, percentage, elapsed time, pass/fail status, and timestamp.
 * ==============================================================================
 */
public class QuizAttempt implements Serializable {

    // Primary Key uniquely identifying this quiz attempt
    private int id;

    // Foreign Key identifying which student took the quiz
    private int userId;

    // Cached username for fast leaderboard and history queries
    private String username;

    // Foreign Key identifying which Category was attempted
    private int categoryId;

    // Cached category name for display
    private String categoryName;

    // Actual marks earned by the candidate
    private int score;

    // Maximum possible marks in the quiz
    private int totalMarks;

    // Percentage score (score / totalMarks * 100)
    private double percentage;

    // Boolean flag indicating if the score met or exceeded the pass percentage
    private boolean passed;

    // Elapsed duration in seconds taken by the student
    private int timeTakenSeconds;

    // Timestamp formatted string when attempt was completed
    private String dateTaken;

    // Default constructor
    public QuizAttempt() {
    }

    // Full constructor
    public QuizAttempt(int id, int userId, String username, int categoryId, String categoryName,
                       int score, int totalMarks, double percentage, boolean passed,
                       int timeTakenSeconds, String dateTaken) {
        this.id = id;
        this.userId = userId;
        this.username = username;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.score = score;
        this.totalMarks = totalMarks;
        this.percentage = percentage;
        this.passed = passed;
        this.timeTakenSeconds = timeTakenSeconds;
        this.dateTaken = dateTaken;
    }

    // Constructor without ID for inserting a newly evaluated attempt
    public QuizAttempt(int userId, String username, int categoryId, String categoryName,
                       int score, int totalMarks, double percentage, boolean passed,
                       int timeTakenSeconds, String dateTaken) {
        this.userId = userId;
        this.username = username;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.score = score;
        this.totalMarks = totalMarks;
        this.percentage = percentage;
        this.passed = passed;
        this.timeTakenSeconds = timeTakenSeconds;
        this.dateTaken = dateTaken;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getTotalMarks() {
        return totalMarks;
    }

    public void setTotalMarks(int totalMarks) {
        this.totalMarks = totalMarks;
    }

    public double getPercentage() {
        return percentage;
    }

    public void setPercentage(double percentage) {
        this.percentage = percentage;
    }

    public boolean isPassed() {
        return passed;
    }

    public void setPassed(boolean passed) {
        this.passed = passed;
    }

    public int getTimeTakenSeconds() {
        return timeTakenSeconds;
    }

    public void setTimeTakenSeconds(int timeTakenSeconds) {
        this.timeTakenSeconds = timeTakenSeconds;
    }

    public String getDateTaken() {
        return dateTaken;
    }

    public void setDateTaken(String dateTaken) {
        this.dateTaken = dateTaken;
    }
}
