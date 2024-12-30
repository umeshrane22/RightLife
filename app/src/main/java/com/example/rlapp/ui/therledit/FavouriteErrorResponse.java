package com.example.rlapp.ui.therledit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FavouriteErrorResponse {

    @SerializedName("statusCode")
    @Expose
    private Integer statusCode;
    @SerializedName("displayMessage")
    @Expose
    private String displayMessage;
    @SerializedName("errorCode")
    @Expose
    private String errorCode;
    @SerializedName("errorMessage")
    @Expose
    private String errorMessage;


    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public String getDisplayMessage() {
        return displayMessage;
    }

    public void setDisplayMessage(String displayMessage) {
        this.displayMessage = displayMessage;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

}