package com.jetsynthesys.rightlife.ui.mindaudit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetAssessmentScoreDass21Request {

    @SerializedName("assessment")
    @Expose
    private String assessment;
    @SerializedName("stressLevel")
    @Expose
    private Double stressLevel = 0.0;
    @SerializedName("anxietyLevel")
    @Expose
    private Double anxietyLevel = 0.0;
    @SerializedName("depressionSeverity")
    @Expose
    private Double depressionSeverity = 0.0;

    public String getAssessment() {
        return assessment;
    }

    public void setAssessment(String assessment) {
        this.assessment = assessment;
    }

    public Double getStressLevel() {
        return stressLevel;
    }

    public void setStressLevel(Double stressLevel) {
        this.stressLevel = stressLevel;
    }

    public Double getAnxietyLevel() {
        return anxietyLevel;
    }

    public void setAnxietyLevel(Double anxietyLevel) {
        this.anxietyLevel = anxietyLevel;
    }

    public Double getDepressionSeverity() {
        return depressionSeverity;
    }

    public void setDepressionSeverity(Double depressionSeverity) {
        this.depressionSeverity = depressionSeverity;
    }

}