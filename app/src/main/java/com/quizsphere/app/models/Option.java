package com.quizsphere.app.models;

import java.io.Serializable;

/**
 * ==============================================================================
 * Model: Option
 * Description: Represents an answer choice for a Question (Slide 7).
 * Identifies whether this choice is the single correct answer.
 * ==============================================================================
 */
public class Option implements Serializable {

    // Primary Key uniquely identifying this option
    private int id;

    // Foreign Key connecting this choice to its Question
    private int questionId;

    // The text description of the option (e.g., "Python", "Java")
    private String text;

    // Flag marking whether this option is the correct solution
    private boolean isCorrect;

    // Default constructor
    public Option() {
    }

    // Full constructor
    public Option(int id, int questionId, String text, boolean isCorrect) {
        this.id = id;
        this.questionId = questionId;
        this.text = text;
        this.isCorrect = isCorrect;
    }

    // Constructor without ID for inserting new options
    public Option(int questionId, String text, boolean isCorrect) {
        this.questionId = questionId;
        this.text = text;
        this.isCorrect = isCorrect;
    }

    // Constructor for memory creation before question ID is known
    public Option(String text, boolean isCorrect) {
        this.text = text;
        this.isCorrect = isCorrect;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getQuestionId() {
        return questionId;
    }

    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isCorrect() {
        return isCorrect;
    }

    public void setCorrect(boolean correct) {
        isCorrect = correct;
    }
}
