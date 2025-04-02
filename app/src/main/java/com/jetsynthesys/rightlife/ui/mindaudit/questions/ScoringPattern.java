package com.jetsynthesys.rightlife.ui.mindaudit.questions;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ScoringPattern implements Serializable {

    @SerializedName("option")
    @Expose
    private String option;
    @SerializedName("score")
    @Expose
    private Double score;

    public String getOption() {
        return option;
    }

    public void setOption(String option) {
        this.option = option;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

}