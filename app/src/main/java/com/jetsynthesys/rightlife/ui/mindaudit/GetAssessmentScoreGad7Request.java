package com.jetsynthesys.rightlife.ui.mindaudit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetAssessmentScoreGad7Request {

    @SerializedName("assessment")
    @Expose
    private String assessment;

    @SerializedName("anxietyLevel")
    @Expose
    private Double anxietyLevel = 0.0;

    public String getAssessment() {
        return assessment;
    }

    public void setAssessment(String assessment) {
        this.assessment = assessment;
    }


    public Double getAnxietyLevel() {
        return anxietyLevel;
    }

    public void setAnxietyLevel(Double anxietyLevel) {
        this.anxietyLevel = anxietyLevel;
    }

}