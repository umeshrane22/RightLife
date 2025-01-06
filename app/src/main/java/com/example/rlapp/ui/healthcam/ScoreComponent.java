package com.example.rlapp.ui.healthcam;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ScoreComponent {

    @SerializedName("categoryName")
    @Expose
    private String categoryName;
    @SerializedName("displayName")
    @Expose
    private String displayName;
    @SerializedName("score")
    @Expose
    private Object score;

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public Object getScore() {
        return score;
    }

    public void setScore(Object score) {
        this.score = score;
    }

}