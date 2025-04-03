package com.jetsynthesys.rightlife.ui.healthcam;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ReportIdData {

    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("userId")
    @Expose
    private String userId;
    @SerializedName("answerId")
    @Expose
    private String answerId;
    @SerializedName("service")
    @Expose
    private Service service;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("createdAt")
    @Expose
    private String createdAt;
    @SerializedName("updatedAt")
    @Expose
    private String updatedAt;
    @SerializedName("__v")
    @Expose
    private Integer v;
    @SerializedName("count")
    @Expose
    private Integer count;
    @SerializedName("healthCamReportByCategory")
    @Expose
    private Object healthCamReportByCategory;
    @SerializedName("userAnswers")
    @Expose
    private UserAnswers userAnswers;
    @SerializedName("scoreComponents")
    @Expose
    private List<ScoreComponent> scoreComponents;
    @SerializedName("recommendation")
    @Expose
    private List<Object> recommendation;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAnswerId() {
        return answerId;
    }

    public void setAnswerId(String answerId) {
        this.answerId = answerId;
    }

    public Service getService() {
        return service;
    }

    public void setService(Service service) {
        this.service = service;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Integer getV() {
        return v;
    }

    public void setV(Integer v) {
        this.v = v;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Object getHealthCamReportByCategory() {
        return healthCamReportByCategory;
    }

    public void setHealthCamReportByCategory(Object healthCamReportByCategory) {
        this.healthCamReportByCategory = healthCamReportByCategory;
    }

    public UserAnswers getUserAnswers() {
        return userAnswers;
    }

    public void setUserAnswers(UserAnswers userAnswers) {
        this.userAnswers = userAnswers;
    }

    public List<ScoreComponent> getScoreComponents() {
        return scoreComponents;
    }

    public void setScoreComponents(List<ScoreComponent> scoreComponents) {
        this.scoreComponents = scoreComponents;
    }

    public List<Object> getRecommendation() {
        return recommendation;
    }

    public void setRecommendation(List<Object> recommendation) {
        this.recommendation = recommendation;
    }

}
