package com.jetsynthesys.rightlife.apimodel.newreportfacescan;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FacialReportData {
    @SerializedName("_id")
    public String id;
    public String userId;
    public String answerId;
    public Service service;
    public String status;
    public String createdAt;
    public String updatedAt;
    @SerializedName("__v")
    public int v;
    public Report report;
    public int count;
    public HealthCamReportByCategory healthCamReportByCategory;
    public UserAnswers userAnswers;
    public List<ScoreComponent> scoreComponents;
    public List<Recommendation> recommendation;
    public boolean lastCheckin;
    public OverallWellnessScore overallWellnessScore;
    public String summary;
    public int usedCount;
    public int boosterUsed;
    public int limit;
    public int boosterLimit;
    public String pdf;

}