package com.jetsynthesys.rightlife.ui.mindaudit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Interpretation {

    @SerializedName("anger")
    @Expose
    private Anger anger;
    @SerializedName("anxiety")
    @Expose
    private Anxiety anxiety;
    @SerializedName("happiness")
    @Expose
    private Happiness happiness;
    @SerializedName("depression")
    @Expose
    private Depression depression;

    public Anger getAnger() {
        return anger;
    }

    public void setAnger(Anger anger) {
        this.anger = anger;
    }

    public Anxiety getAnxiety() {
        return anxiety;
    }

    public void setAnxiety(Anxiety anxiety) {
        this.anxiety = anxiety;
    }

    public Happiness getHappiness() {
        return happiness;
    }

    public void setHappiness(Happiness happiness) {
        this.happiness = happiness;
    }

    public Depression getDepression() {
        return depression;
    }

    public void setDepression(Depression depression) {
        this.depression = depression;
    }

}