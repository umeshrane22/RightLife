package com.example.rlapp.ui.mindaudit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetEmotions {

    @SerializedName("emotions")
    @Expose
    private List<String> emotions;
    @SerializedName("takenAssessment")
    @Expose
    private List<Object> takenAssessment;
    @SerializedName("selectedEmotion")
    @Expose
    private List<Object> selectedEmotion;
    @SerializedName("basicAssesmentTaken")
    @Expose
    private Boolean basicAssesmentTaken;
    @SerializedName("selectedBasicEmotions")
    @Expose
    private List<Object> selectedBasicEmotions;

    public List<String> getEmotions() {
        return emotions;
    }

    public void setEmotions(List<String> emotions) {
        this.emotions = emotions;
    }

    public List<Object> getTakenAssessment() {
        return takenAssessment;
    }

    public void setTakenAssessment(List<Object> takenAssessment) {
        this.takenAssessment = takenAssessment;
    }

    public List<Object> getSelectedEmotion() {
        return selectedEmotion;
    }

    public void setSelectedEmotion(List<Object> selectedEmotion) {
        this.selectedEmotion = selectedEmotion;
    }

    public Boolean getBasicAssesmentTaken() {
        return basicAssesmentTaken;
    }

    public void setBasicAssesmentTaken(Boolean basicAssesmentTaken) {
        this.basicAssesmentTaken = basicAssesmentTaken;
    }

    public List<Object> getSelectedBasicEmotions() {
        return selectedBasicEmotions;
    }

    public void setSelectedBasicEmotions(List<Object> selectedBasicEmotions) {
        this.selectedBasicEmotions = selectedBasicEmotions;
    }

}