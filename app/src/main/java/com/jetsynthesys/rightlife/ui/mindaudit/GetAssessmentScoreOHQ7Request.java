package com.jetsynthesys.rightlife.ui.mindaudit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetAssessmentScoreOHQ7Request {

    @SerializedName("assessment")
    @Expose
    private String assessment;

    @SerializedName("score")
    @Expose
    private Double score = 0.0;

    public String getAssessment() {
        return assessment;
    }

    public void setAssessment(String assessment) {
        this.assessment = assessment;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }
}