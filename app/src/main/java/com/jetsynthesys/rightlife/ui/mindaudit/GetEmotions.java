package com.jetsynthesys.rightlife.ui.mindaudit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetEmotions {

    @SerializedName("emotions")
    @Expose
    private List<String> emotions;
    @SerializedName("takenAssessment")
    @Expose
    private List<String> takenAssessment;
    @SerializedName("selectedEmotion")
    @Expose
    private List<String> selectedEmotion;
    @SerializedName("basicAssesmentTaken")
    @Expose
    private Boolean basicAssesmentTaken;
    @SerializedName("selectedBasicEmotions")
    @Expose
    private List<String> selectedBasicEmotions;

    public List<String> getEmotions() {
        return emotions;
    }

    public void setEmotions(List<String> emotions) {
        this.emotions = emotions;
    }

    public List<String> getTakenAssessment() {
        return takenAssessment;
    }

    public void setTakenAssessment(List<String> takenAssessment) {
        this.takenAssessment = takenAssessment;
    }

    public List<String> getSelectedEmotion() {
        return selectedEmotion;
    }

    public void setSelectedEmotion(List<String> selectedEmotion) {
        this.selectedEmotion = selectedEmotion;
    }

    public Boolean getBasicAssesmentTaken() {
        return basicAssesmentTaken;
    }

    public void setBasicAssesmentTaken(Boolean basicAssesmentTaken) {
        this.basicAssesmentTaken = basicAssesmentTaken;
    }

    public List<String> getSelectedBasicEmotions() {
        return selectedBasicEmotions;
    }

    public void setSelectedBasicEmotions(List<String> selectedBasicEmotions) {
        this.selectedBasicEmotions = selectedBasicEmotions;
    }

}