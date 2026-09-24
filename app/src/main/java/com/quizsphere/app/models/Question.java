package com.quizsphere.app.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * ==============================================================================
 * Model: Question
 * Description: Represents a multiple-choice question belonging to a Category (Slide 7).
 * Holds question text, assigned score marks, explanation text, and 4 choices.
 * ==============================================================================
 */
public class Question implements Serializable {

    // Primary Key uniquely identifying the question
    private int id;

    // Foreign Key linking this question to its parent Category
    private int categoryId;

    // The question prompt presented to the student
    private String text;

    // Total points / marks awarded when answered correctly
    private int marks;

    // Optional explanation revealed on result review explaining the correct answer
    private String explanation;

    // List of multiple choice options linked to this question
    private List<Option> options = new ArrayList<>();

    // Tracks user's selected option ID during an active exam attempt
    private int selectedOptionId = -1;

    // Default constructor
    public Question() {
    }

    // Full constructor
    public Question(int id, int categoryId, String text, int marks, String explanation) {
        this.id = id;
        this.categoryId = categoryId;
        this.text = text;
        this.marks = marks;
        this.explanation = explanation;
    }

    // Constructor without ID for inserting new questions
    public Question(int categoryId, String text, int marks, String explanation) {
        this.categoryId = categoryId;
        this.text = text;
        this.marks = marks;
        this.explanation = explanation;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getMarks() {
        return marks;
    }

    public void setMarks(int marks) {
        this.marks = marks;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }

    public List<Option> getOptions() {
        return options;
    }

    public void setOptions(List<Option> options) {
        this.options = options;
    }

    public int getSelectedOptionId() {
        return selectedOptionId;
    }

    public void setSelectedOptionId(int selectedOptionId) {
        this.selectedOptionId = selectedOptionId;
    }

    // Helper method to retrieve the single correct option
    public Option getCorrectOption() {
        for (Option opt : options) {
            if (opt.isCorrect()) {
                return opt;
            }
        }
        return null;
    }
}
