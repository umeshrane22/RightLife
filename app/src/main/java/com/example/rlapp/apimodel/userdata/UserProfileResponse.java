package com.example.rlapp.apimodel.userdata;


import java.util.List;

import com.example.rlapp.apimodel.servicepane.HomeService;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import javax.annotation.processing.Generated;

@Generated("jsonschema2pojo")
public class UserProfileResponse {

    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("statusCode")
    @Expose
    private Integer statusCode;

    public Userdata getUserdata() {
        return userdata;
    }

    public void setUserdata(Userdata userdata) {
        this.userdata = userdata;
    }

    @SerializedName("data")
    @Expose
    private Userdata userdata;
    @SerializedName("homeServices")
    @Expose
    private List<HomeService> homeServices;
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



    public List<HomeService> getHomeServices() {
        return homeServices;
    }

    public void setHomeServices(List<HomeService> homeServices) {
        this.homeServices = homeServices;
    }

    public String getSuccessMessage() {
        return successMessage;
    }

    public void setSuccessMessage(String successMessage) {
        this.successMessage = successMessage;
    }

}