package com.example.rlapp.ui.mindaudit.curated;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Context {

    @SerializedName("assessmentUndertaken")
    @Expose
    private AssessmentUndertaken assessmentUndertaken;
    @SerializedName("module")
    @Expose
    private String module;

    public AssessmentUndertaken getAssessmentUndertaken() {
        return assessmentUndertaken;
    }

    public void setAssessmentUndertaken(AssessmentUndertaken assessmentUndertaken) {
        this.assessmentUndertaken = assessmentUndertaken;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

}