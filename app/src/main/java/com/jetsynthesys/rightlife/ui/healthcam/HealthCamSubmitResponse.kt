package com.jetsynthesys.rightlife.ui.healthcam;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class HealthCamSubmitResponse {

    @SerializedName("statusCode")
    @Expose
    private Integer statusCode;
    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("data")
    @Expose
    private ReportIdData data;

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ReportIdData getData() {
        return data;
    }

    public void setData(ReportIdData data) {
        this.data = data;
    }

}