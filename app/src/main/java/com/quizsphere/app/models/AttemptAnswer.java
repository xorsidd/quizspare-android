package com.quizsphere.app.models;

import java.io.Serializable;

/**
 * ==============================================================================
 * Model: AttemptAnswer
 * Description: Stores each question's individual response within an attempt.
 * Used for post-exam audit review to show what student picked vs correct answer.
 * ==============================================================================
 */
public class AttemptAnswer implements Serializable {

    // Primary Key for the answer record
    private int id;

    // Foreign Key to the parent QuizAttempt
    private int attemptId;

    // Foreign Key to the Question
    private int questionId;

    // Cached question prompt text
    private String questionText;

    // Option ID selected by student (-1 if left unanswered)
    private int selectedOptionId;

    // Text of option selected by student
    private String selectedOptionText;

    // Text of the correct option
    private String correctOptionText;

    // True if selected option matches correct option
    private boolean isCorrect;

    // Marks awarded for this specific question
    private int marksObtained;

    // Total maximum marks for this question
    private int maxMarks;

    // Explanation explaining the reasoning behind the correct choice
    private String explanation;

    // Default constructor
    public AttemptAnswer() {
    }

    // Full constructor
    public AttemptAnswer(int id, int attemptId, int questionId, String questionText,
                         int selectedOptionId, String selectedOptionText, String correctOptionText,
                         boolean isCorrect, int marksObtained, int maxMarks, String explanation) {
        this.id = id;
        this.attemptId = attemptId;
        this.questionId = questionId;
        this.questionText = questionText;
        this.selectedOptionId = selectedOptionId;
        this.selectedOptionText = selectedOptionText;
        this.correctOptionText = correctOptionText;
        this.isCorrect = isCorrect;
        this.marksObtained = marksObtained;
        this.maxMarks = maxMarks;
        this.explanation = explanation;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAttemptId() {
        return attemptId;
    }

    public void setAttemptId(int attemptId) {
        this.attemptId = attemptId;
    }

    public int getQuestionId() {
        return questionId;
    }

    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public int getSelectedOptionId() {
        return selectedOptionId;
    }

    public void setSelectedOptionId(int selectedOptionId) {
        this.selectedOptionId = selectedOptionId;
    }

    public String getSelectedOptionText() {
        return selectedOptionText;
    }

    public void setSelectedOptionText(String selectedOptionText) {
        this.selectedOptionText = selectedOptionText;
    }

    public String getCorrectOptionText() {
        return correctOptionText;
    }

    public void setCorrectOptionText(String correctOptionText) {
        this.correctOptionText = correctOptionText;
    }

    public boolean isCorrect() {
        return isCorrect;
    }

    public void setCorrect(boolean correct) {
        isCorrect = correct;
    }

    public int getMarksObtained() {
        return marksObtained;
    }

    public void setMarksObtained(int marksObtained) {
        this.marksObtained = marksObtained;
    }

    public int getMaxMarks() {
        return maxMarks;
    }

    public void setMaxMarks(int maxMarks) {
        this.maxMarks = maxMarks;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }
}
