package com.jetsynthesys.rightlife.ui.mindaudit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MindAuditAssessmentSaveRequest {

    /*@SerializedName("userId")
    @Expose
    private String userId;
    @SerializedName("date")
    @Expose
    private String date;*/
    @SerializedName("assessmentsTaken")
    @Expose
    private List<AssessmentsTaken> assessmentsTaken;
    @SerializedName("emotionalState")
    @Expose
    private List<String> emotionalState;

    public List<String> getEmotionalState() {
        return emotionalState;
    }

    public void setEmotionalState(List<String> emotionalState) {
        this.emotionalState = emotionalState;
    }

    /*public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }*/

    public List<AssessmentsTaken> getAssessmentsTaken() {
        return assessmentsTaken;
    }

    public void setAssessmentsTaken(List<AssessmentsTaken> assessmentsTaken) {
        this.assessmentsTaken = assessmentsTaken;
    }

}