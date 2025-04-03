package com.jetsynthesys.rightlife.ui.mindaudit.curated;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AssessmentUndertaken {

    @SerializedName("assessment")
    @Expose
    private String assessment;
    @SerializedName("target")
    @Expose
    private List<Object> target;

    public String getAssessment() {
        return assessment;
    }

    public void setAssessment(String assessment) {
        this.assessment = assessment;
    }

    public List<Object> getTarget() {
        return target;
    }

    public void setTarget(List<Object> target) {
        this.target = target;
    }

}