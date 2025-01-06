package com.example.rlapp.ui.healthcam;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class HealthCamFacialScanRequest {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("reportId")
    @Expose
    private String reportId;
    @SerializedName("reportData")
    @Expose
    private ReportData reportData;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getReportId() {
        return reportId;
    }

    public void setReportId(String reportId) {
        this.reportId = reportId;
    }

    public ReportData getReportData() {
        return reportData;
    }

    public void setReportData(ReportData reportData) {
        this.reportData = reportData;
    }

}