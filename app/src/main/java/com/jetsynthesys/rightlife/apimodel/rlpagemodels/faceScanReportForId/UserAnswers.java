package com.jetsynthesys.rightlife.apimodel.rlpagemodels.faceScanReportForId;

import java.util.List;

public class UserAnswers {
    private String _id;
    private String userId;
    private String questionId;
    private String status;
    private String type;
    private List<Answer> answers;
    private List<String> error; // Assuming error is a list of strings
    private String createdAt;
    private String updatedAt;
    private int __v;
    private String subscriptionId;

    public String get_id() {
        return _id;
    }

    public String getUserId() {
        return userId;
    }

    public String getQuestionId() {
        return questionId;
    }

    public String getStatus() {
        return status;
    }

    public String getType() {
        return type;
    }

    public List<Answer> getAnswers() {
        return answers;
    }

    public List<String> getError() {
        return error;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public int get__v() {
        return __v;
    }

    public String getSubscriptionId() {
        return subscriptionId;
    }
}
