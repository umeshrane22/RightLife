package com.jetsynthesys.rightlife.apimodel.rlpagemodels.faceScanReportForId;


public class FacialReportData {
    private String _id;
    private String userId;
    private String answerId;
    private Service service;
    private String status;
    private String createdAt;
    private String updatedAt;
    private int __v;
    private Report report;

    // Getters
    public String get_id() {
        return _id;
    }

    public String getUserId() {
        return userId;
    }

    public String getAnswerId() {
        return answerId;
    }

    public Service getService() {
        return service;
    }

    public String getStatus() {
        return status;
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

    public Report getReport() {
        return report;
    }}
