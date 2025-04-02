package com.jetsynthesys.rightlife.ui.mindaudit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class BasicScreeningQuestions implements Serializable {

    @SerializedName("LOW")
    @Expose
    private List<String> low;
    @SerializedName("IRRITABLE")
    @Expose
    private List<String> irritable;
    @SerializedName("FATIGUED")
    @Expose
    private List<String> fatigued;
    @SerializedName("ANXIOUS")
    @Expose
    private List<String> anxious;
    @SerializedName("STRESSED")
    @Expose
    private List<String> stressed;
    @SerializedName("UNFULFILLED")
    @Expose
    private List<String> unFullFilled;

    public List<String> getUnFullFilled() {
        return unFullFilled;
    }

    public void setUnFullFilled(List<String> unFullFilled) {
        this.unFullFilled = unFullFilled;
    }

    public List<String> getStressed() {
        return stressed;
    }

    public void setStressed(List<String> stressed) {
        this.stressed = stressed;
    }

    public List<String> getAnxious() {
        return anxious;
    }

    public void setAnxious(List<String> anxious) {
        this.anxious = anxious;
    }

    public List<String> getLow() {
        return low;
    }

    public void setLow(List<String> low) {
        this.low = low;
    }

    public List<String> getIrritable() {
        return irritable;
    }

    public void setIrritable(List<String> irritable) {
        this.irritable = irritable;
    }

    public List<String> getFatigued() {
        return fatigued;
    }

    public void setFatigued(List<String> fatigued) {
        this.fatigued = fatigued;
    }

}