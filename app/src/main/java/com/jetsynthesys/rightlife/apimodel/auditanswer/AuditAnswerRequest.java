package com.jetsynthesys.rightlife.apimodel.auditanswer;

import java.util.List;



public class AuditAnswerRequest {
    private String questionId;
    private List<Answer1> answers;


    // Constructor, getters, and setters
    public AuditAnswerRequest(String questionId, List<Answer1> answers) {
        this.questionId = questionId;
        this.answers = answers;
    }


    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public List<Answer1> getAnswers() {
        return answers;
    }

    public void setAnswers(List<Answer1> answers) {
        this.answers = answers;
    }

    // Getters and Setters
}

