package com.example.rlapp.ui.healthcam;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {

    @SerializedName("answerId")
    @Expose
    private String answerId;
    @SerializedName("isSubscribed")
    @Expose
    private Boolean isSubscribed;
    @SerializedName("isFree")
    @Expose
    private Boolean isFree;
    @SerializedName("subscriptionId")
    @Expose
    private String subscriptionId;

    public String getAnswerId() {
        return answerId;
    }

    public void setAnswerId(String answerId) {
        this.answerId = answerId;
    }

    public Boolean getIsSubscribed() {
        return isSubscribed;
    }

    public void setIsSubscribed(Boolean isSubscribed) {
        this.isSubscribed = isSubscribed;
    }

    public Boolean getIsFree() {
        return isFree;
    }

    public void setIsFree(Boolean isFree) {
        this.isFree = isFree;
    }

    public String getSubscriptionId() {
        return subscriptionId;
    }

    public void setSubscriptionId(String subscriptionId) {
        this.subscriptionId = subscriptionId;
    }

}