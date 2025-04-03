package com.jetsynthesys.rightlife.apimodel.newreportfacescan;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UserAnswers {
    @SerializedName("_id")
    public String id;
    public String userId;
    public String questionId;
    public String status;
    public String type;
    public List<Answer> answers;
    public List<Object> error; // Or a specific error class
    public String createdAt;
    public String updatedAt;
    @SerializedName("__v")
    public int v;
    public String subscriptionId;
}