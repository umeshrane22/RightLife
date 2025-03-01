package com.example.rlapp.ui.mindaudit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetAssessmentScorePHQ9Request {

    @SerializedName("assessment")
    @Expose
    private String assessment;

    @SerializedName("depressionSeverity")
    @Expose
    private Double depressionSeverity = 0.0;

    public String getAssessment() {
        return assessment;
    }

    public void setAssessment(String assessment) {
        this.assessment = assessment;
    }

    public Double getDepressionSeverity() {
        return depressionSeverity;
    }

    public void setDepressionSeverity(Double depressionSeverity) {
        this.depressionSeverity = depressionSeverity;
    }

}