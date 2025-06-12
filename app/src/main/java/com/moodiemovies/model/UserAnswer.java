package com.moodiemovies.model;

import com.google.gson.annotations.SerializedName;

public class UserAnswer {

    @SerializedName("questionId")
    private String questionId;

    @SerializedName("point")
    private int point; // Backend'e point gönderiyoruz.

    public UserAnswer(String questionId, int point) {
        this.questionId = questionId;
        this.point = point;
    }

    // Getter ve Setter'lar
    public String getQuestionId() { return questionId; }
    public void setQuestionId(String questionId) { this.questionId = questionId; }

    public int getPoint() { return point; }
    public void setPoint(int point) { this.point = point; }
}