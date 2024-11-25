package com.example.rlapp.apimodel.auditanswer;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Answer1 {
    @SerializedName("question")
    private String question;

    @SerializedName("answer")
    private List<Option1> answer;

    @SerializedName("subQuestions")
    private List<SubQuestion> subQuestions;

    // Constructor, getters, and setters
    public Answer1(String question, List<Option1> answer, List<SubQuestion> subQuestions) {
        this.question = question;
        this.answer = answer;
        this.subQuestions = subQuestions;
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

    public List<SubQuestion> getSubQuestions() {
        return subQuestions;
    }

    public void setSubQuestions(List<SubQuestion> subQuestions) {
        this.subQuestions = subQuestions;
    }
}