package com.example.rlapp.ui.mindaudit.questions;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ScoringPattern implements Serializable {

    @SerializedName("option")
    @Expose
    private String option;
    @SerializedName("score")
    @Expose
    private Object score;

    public String getOption() {
        return option;
    }

    public void setOption(String option) {
        this.option = option;
    }

    public Object getScore() {
        return score;
    }

    public void setScore(Boolean score) {
        this.score = score;
    }

}