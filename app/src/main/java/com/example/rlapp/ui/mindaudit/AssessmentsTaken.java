package com.example.rlapp.ui.mindaudit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AssessmentsTaken {

    @SerializedName("assessment")
    @Expose
    private String assessment;
    @SerializedName("interpretations")
    @Expose
    private Interpretations interpretations;

    public String getAssessment() {
        return assessment;
    }

    public void setAssessment(String assessment) {
        this.assessment = assessment;
    }

    public Interpretations getInterpretations() {
        return interpretations;
    }

    public void setInterpretations(Interpretations interpretations) {
        this.interpretations = interpretations;
    }

}