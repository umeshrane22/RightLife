package com.jetsynthesys.rightlife.apimodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import javax.annotation.processing.Generated;

@Generated("jsonschema2pojo")
public class CheckRegistrationResponse {

    @SerializedName("isPasswordSet")
    @Expose
    private Boolean isPasswordSet;
    @SerializedName("isRegistered")
    @Expose
    private Boolean isRegistered;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("statusCode")
    @Expose
    private Integer statusCode;
    @SerializedName("success")
    @Expose
    private Boolean success;

    public Boolean getIsPasswordSet() {
        return isPasswordSet;
    }

    public void setIsPasswordSet(Boolean isPasswordSet) {
        this.isPasswordSet = isPasswordSet;
    }

    public Boolean getIsRegistered() {
        return isRegistered;
    }

    public void setIsRegistered(Boolean isRegistered) {
        this.isRegistered = isRegistered;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

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

}