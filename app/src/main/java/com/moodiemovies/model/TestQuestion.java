package com.moodiemovies.model;

import java.util.List;

public class TestQuestion {

    private String questionId;
    private String questionText;
    private List<TestAnswer> answers;

    public TestQuestion(String questionId, String questionText, List<TestAnswer> answers) {
        this.questionId = questionId;
        this.questionText = questionText;
        this.answers = answers;
    }

    // Getter ve Setter'lar
    public String getQuestionId() { return questionId; }
    public void setQuestionId(String questionId) { this.questionId = questionId; }

    public String getQuestionText() { return questionText; }
    public void setQuestionText(String questionText) { this.questionText = questionText; }

    public List<TestAnswer> getAnswers() { return answers; }
    public void setAnswers(List<TestAnswer> answers) { this.answers = answers; }
}