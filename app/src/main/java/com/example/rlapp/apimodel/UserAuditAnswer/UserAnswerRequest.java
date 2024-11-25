package com.example.rlapp.apimodel.UserAuditAnswer;

import java.util.List;

public class UserAnswerRequest {
    private String answerId;
    private String questionId;
    private List<Answer> answers;

    // Constructor
    public UserAnswerRequest(String answerId, String questionId, List<Answer> answers) {
        this.answerId = answerId;
        this.questionId = questionId;
        this.answers = answers;
    }

    // Getters and Setters
    public String getAnswerId() {
        return answerId;
    }

    public void setAnswerId(String answerId) {
        this.answerId = answerId;
    }

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public List<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<Answer> answers) {
        this.answers = answers;
    }
}