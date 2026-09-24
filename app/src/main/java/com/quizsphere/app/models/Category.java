package com.quizsphere.app.models;

import java.io.Serializable;

/**
 * ==============================================================================
 * Model: Category
 * Description: Represents a Quiz Subject / Category (Slide 7).
 * Stores subject details, time limit in minutes, and pass percentage threshold.
 * ==============================================================================
 */
public class Category implements Serializable {

    // Primary Key uniquely identifying the category
    private int id;

    // Human-readable category title (e.g., "Django Framework", "Python Core")
    private String name;

    // URL-friendly identifier string
    private String slug;

    // Brief overview of the topics covered in this quiz
    private String description;

    // Name of the visual icon associated with this category
    private String iconName;

    // Total exam countdown duration in minutes
    private int timeLimitMins;

    // Minimum percentage needed to achieve a passing status
    private int passPercentage;

    // Dynamically computed total number of questions inside this category
    private int questionCount;

    // Default constructor
    public Category() {
    }

    // Full constructor initializing all fields
    public Category(int id, String name, String slug, String description, String iconName, int timeLimitMins, int passPercentage) {
        this.id = id;
        this.name = name;
        this.slug = slug;
        this.description = description;
        this.iconName = iconName;
        this.timeLimitMins = timeLimitMins;
        this.passPercentage = passPercentage;
    }

    // Constructor without ID for creating new categories
    public Category(String name, String slug, String description, String iconName, int timeLimitMins, int passPercentage) {
        this.name = name;
        this.slug = slug;
        this.description = description;
        this.iconName = iconName;
        this.timeLimitMins = timeLimitMins;
        this.passPercentage = passPercentage;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIconName() {
        return iconName;
    }

    public void setIconName(String iconName) {
        this.iconName = iconName;
    }

    public int getTimeLimitMins() {
        return timeLimitMins;
    }

    public void setTimeLimitMins(int timeLimitMins) {
        this.timeLimitMins = timeLimitMins;
    }

    public int getPassPercentage() {
        return passPercentage;
    }

    public void setPassPercentage(int passPercentage) {
        this.passPercentage = passPercentage;
    }

    public int getQuestionCount() {
        return questionCount;
    }

    public void setQuestionCount(int questionCount) {
        this.questionCount = questionCount;
    }
}
