package com.example.rlapp.ui.healthaudit.questionlist;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Question implements Serializable {

    @SerializedName("question")
    @Expose
    private String question;
    @SerializedName("active")
    @Expose
    private Boolean active;
    @SerializedName("inputType")
    @Expose
    private String inputType;
    @SerializedName("question_txt")
    @Expose
    private String questionTxt;
    @SerializedName("options")
    @Expose
    private List<Option> options;
    @SerializedName("multiple")
    @Expose
    private Boolean multiple;
    @SerializedName("mandatory")
    @Expose
    private Boolean mandatory;
    @SerializedName("help_text")
    @Expose
    private String helpText;

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getInputType() {
        return inputType;
    }

    public void setInputType(String inputType) {
        this.inputType = inputType;
    }

    public String getQuestionTxt() {
        return questionTxt;
    }

    public void setQuestionTxt(String questionTxt) {
        this.questionTxt = questionTxt;
    }

    public List<Option> getOptions() {
        return options;
    }

    public void setOptions(List<Option> options) {
        this.options = options;
    }

    public Boolean getMultiple() {
        return multiple;
    }

    public void setMultiple(Boolean multiple) {
        this.multiple = multiple;
    }

    public Boolean getMandatory() {
        return mandatory;
    }

    public void setMandatory(Boolean mandatory) {
        this.mandatory = mandatory;
    }

    public String getHelpText() {
        return helpText;
    }

    public void setHelpText(String helpText) {
        this.helpText = helpText;
    }

}