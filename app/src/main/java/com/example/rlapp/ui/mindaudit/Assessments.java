package com.example.rlapp.ui.mindaudit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Assessments implements Serializable {
    @SerializedName("suggestedAssessments")
    @Expose
    private SuggestedAssessments suggestedAssessments;
    @SerializedName("allAssessment")
    @Expose
    private AllAssessment allAssessment;
    @SerializedName("savedAssessment")
    @Expose
    private List<Object> savedAssessment;

    public SuggestedAssessments getSuggestedAssessments() {
        return suggestedAssessments;
    }

    public void setSuggestedAssessments(SuggestedAssessments suggestedAssessments) {
        this.suggestedAssessments = suggestedAssessments;
    }

    public AllAssessment getAllAssessment() {
        return allAssessment;
    }

    public void setAllAssessment(AllAssessment allAssessment) {
        this.allAssessment = allAssessment;
    }

    public List<Object> getSavedAssessment() {
        return savedAssessment;
    }

    public void setSavedAssessment(List<Object> savedAssessment) {
        this.savedAssessment = savedAssessment;
    }

}