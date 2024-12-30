package com.example.rlapp.ui.therledit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FavouriteResponse {

    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("statusCode")
    @Expose
    private Integer statusCode;
    @SerializedName("successMessage")
    @Expose
    private String successMessage;

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

    public String getSuccessMessage() {
        return successMessage;
    }

    public void setSuccessMessage(String successMessage) {
        this.successMessage = successMessage;
    }

}