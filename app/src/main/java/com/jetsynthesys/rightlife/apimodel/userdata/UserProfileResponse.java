package com.jetsynthesys.rightlife.apimodel.userdata;


import java.util.List;

import com.jetsynthesys.rightlife.apimodel.servicepane.HomeService;
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

    @SerializedName("daysCount")
    @Expose
    private Integer daysCount;
    @SerializedName("profileCompletion")
    @Expose
    private Integer profileCompletion;
    @SerializedName("wellnessStreak")
    @Expose
    private Integer wellnessStreak;

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

    public Integer getDaysCount() {
        return daysCount;
    }

    public void setDaysCount(Integer daysCount) {
        this.daysCount = daysCount;
    }

    public Integer getProfileCompletion() {
        return profileCompletion;
    }

    public void setProfileCompletion(Integer profileCompletion) {
        this.profileCompletion = profileCompletion;
    }

    public Integer getWellnessStreak() {
        return wellnessStreak;
    }

    public void setWellnessStreak(Integer wellnessStreak) {
        this.wellnessStreak = wellnessStreak;
    }

}