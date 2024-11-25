package com.example.rlapp.apimodel.auditanswer;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SubQuestion {
    @SerializedName("question")
    private String question;

    @SerializedName("answer")
    private List<Option1> answer;

    // Constructor, getters, and setters
    public SubQuestion(String question, List<Option1> answer) {
        this.question = question;
        this.answer = answer;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public List<Option1> getAnswer() {
        return answer;
    }

    public void setAnswer(List<Option1> answer) {
        this.answer = answer;
    }
}