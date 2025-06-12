package com.moodiemovies.model;

public class TestAnswer {

    private String answerId;
    private String answerText;
    private int point;

    public TestAnswer(String answerId, String answerText, int point) {
        this.answerId = answerId;
        this.answerText = answerText;
        this.point = point;
    }

    // Getter ve Setter'lar
    public String getAnswerId() { return answerId; }
    public void setAnswerId(String answerId) { this.answerId = answerId; }

    public String getAnswerText() { return answerText; }
    public void setAnswerText(String answerText) { this.answerText = answerText; }

    public int getPoint() { return point; }
    public void setPoint(int point) { this.point = point; }
}