package com.jetsynthesys.rightlife.ui.mindaudit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Result {

    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("userId")
    @Expose
    private String userId;
    @SerializedName("emotionalState")
    @Expose
    private List<String> emotionalState;
    @SerializedName("assessmentsTaken")
    @Expose
    private List<AssessmentsTaken> assessmentsTaken;
    @SerializedName("createdAt")
    @Expose
    private String createdAt;
    @SerializedName("updatedAt")
    @Expose
    private String updatedAt;
    @SerializedName("__v")
    @Expose
    private Integer v;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<String> getEmotionalState() {
        return emotionalState;
    }

    public void setEmotionalState(List<String> emotionalState) {
        this.emotionalState = emotionalState;
    }

    public List<AssessmentsTaken> getAssessmentsTaken() {
        return assessmentsTaken;
    }

    public void setAssessmentsTaken(List<AssessmentsTaken> assessmentsTaken) {
        this.assessmentsTaken = assessmentsTaken;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Integer getV() {
        return v;
    }

    public void setV(Integer v) {
        this.v = v;
    }

}