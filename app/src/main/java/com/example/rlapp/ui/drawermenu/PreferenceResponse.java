package com.example.rlapp.ui.drawermenu;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PreferenceResponse {

    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("statusCode")
    @Expose
    private Integer statusCode;
    @SerializedName("data")
    @Expose
    private PreferenceData data;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public PreferenceData getData() {
        return data;
    }

    public void setData(PreferenceData data) {
        this.data = data;
    }

}