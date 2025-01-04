package com.example.rlapp.ui.mindaudit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AssessmentsTaken {

    @SerializedName("assessment")
    @Expose
    private String assessment;
    @SerializedName("interpretation")
    @Expose
    private Interpretation interpretation;
    @SerializedName("interpretations")
    @Expose
    private Interpretations interpretations;

    public String getAssessment() {
        return assessment;
    }

    public void setAssessment(String assessment) {
        this.assessment = assessment;
    }

    public Interpretation getInterpretation() {
        return interpretation;
    }

    public void setInterpretation(Interpretation interpretation) {
        this.interpretation = interpretation;
    }

    public Interpretations getInterpretations() {
        return interpretations;
    }

    public void setInterpretations(Interpretations interpretations) {
        this.interpretations = interpretations;
    }

}