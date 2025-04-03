package com.jetsynthesys.rightlife.ui.mindaudit.questions;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Question implements Serializable {

    @SerializedName("question")
    @Expose
    private String question;
    @SerializedName("scale")
    @Expose
    private String scale;
    @SerializedName("scoringPattern")
    @Expose
    private List<ScoringPattern> scoringPattern;
    @SerializedName("continueFurtherIfTrue")
    @Expose
    private boolean continueFurtherIfTrue;

    public boolean isContinueFurtherIfTrue() {
        return continueFurtherIfTrue;
    }

    public void setContinueFurtherIfTrue(boolean continueFurtherIfTrue) {
        this.continueFurtherIfTrue = continueFurtherIfTrue;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getScale() {
        return scale;
    }

    public void setScale(String scale) {
        this.scale = scale;
    }

    public List<ScoringPattern> getScoringPattern() {
        return scoringPattern;
    }

    public void setScoringPattern(List<ScoringPattern> scoringPattern) {
        this.scoringPattern = scoringPattern;
    }

}