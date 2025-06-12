package com.moodiemovies.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class TestSubmissionRequest {

    // Backend DTO'su `userId` beklediği için bu alanı ekliyoruz.
    @SerializedName("userId")
    private String userId;

    @SerializedName("answers")
    private List<UserAnswer> answers;

    public TestSubmissionRequest(String userId, List<UserAnswer> answers) {
        this.userId = userId;
        this.answers = answers;
    }

    // Getter ve Setter'lar
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public List<UserAnswer> getAnswers() { return answers; }
    public void setAnswers(List<UserAnswer> answers) { this.answers = answers; }
}