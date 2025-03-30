package com.example.rlapp.ui.mindaudit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MindAuditResultResponse {

    @SerializedName("result")
    @Expose
    private List<Result> result;
    @SerializedName("recommendations")
    @Expose
    private List<Recommendation> recommendations;

    public List<Result> getResult() {
        return result;
    }

    public void setResult(List<Result> result) {
        this.result = result;
    }

    public List<Recommendation> getRecommendations() {
        return recommendations;
    }

    public void setRecommendations(List<Recommendation> recommendations) {
        this.recommendations = recommendations;
    }

}