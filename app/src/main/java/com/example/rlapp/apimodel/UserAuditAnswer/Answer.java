package com.example.rlapp.apimodel.UserAuditAnswer;

import java.util.List;

public class Answer {
    private String question;
    private List<Option> answer;

    // Constructor
    public Answer(String question, List<Option> answer) {
        this.question = question;
        this.answer = answer;
    }

    // Getters and Setters
    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public List<Option> getAnswer() {
        return answer;
    }

    public void setAnswer(List<Option> answer) {
        this.answer = answer;
    }
}
