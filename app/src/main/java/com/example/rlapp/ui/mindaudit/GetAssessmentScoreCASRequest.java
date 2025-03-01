package com.example.rlapp.ui.mindaudit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetAssessmentScoreCASRequest {

    @SerializedName("assessment")
    @Expose
    private String assessment;

    @SerializedName("angerLevel")
    @Expose
    private Double angerLevel = 0.0;

    public String getAssessment() {
        return assessment;
    }

    public void setAssessment(String assessment) {
        this.assessment = assessment;
    }

    public Double getAngerLevel() {
        return angerLevel;
    }

    public void setAngerLevel(Double angerLevel) {
        this.angerLevel = angerLevel;
    }
}