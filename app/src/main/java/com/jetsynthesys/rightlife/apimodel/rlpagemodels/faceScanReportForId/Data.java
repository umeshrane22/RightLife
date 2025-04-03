package com.jetsynthesys.rightlife.apimodel.rlpagemodels.faceScanReportForId;

import com.google.gson.annotations.SerializedName;

public class Data {
    @SerializedName("_id")
    private String id;
    private String userId;
    private String answerId;
    private Service service;
    private String status;
    private Report report;
    private UserAnswers userAnswers;

    public String getId() { return id; }
    public String getUserId() { return userId; }
    public String getAnswerId() { return answerId; }
    public Service getService() { return service; }
    public String getStatus() { return status; }
    public Report getReport() { return report; }
    public UserAnswers getUserAnswers() { return userAnswers; }
}
