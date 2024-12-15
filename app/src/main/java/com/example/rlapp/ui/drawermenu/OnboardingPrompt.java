package com.example.rlapp.ui.drawermenu;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class OnboardingPrompt implements Serializable {

    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("options")
    @Expose
    private List<PreferenceOption> options;
    @SerializedName("desc")
    @Expose
    private String desc;
    @SerializedName("order")
    @Expose
    private Integer order;
    @SerializedName("isMultiSelect")
    @Expose
    private Boolean isMultiSelect;
    @SerializedName("appId")
    @Expose
    private String appId;
    @SerializedName("appName")
    @Expose
    private String appName;
    @SerializedName("userAnswerId")
    @Expose
    private String userAnswerId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<PreferenceOption> getOptions() {
        return options;
    }

    public void setOptions(List<PreferenceOption> options) {
        this.options = options;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public Boolean getIsMultiSelect() {
        return isMultiSelect;
    }

    public void setIsMultiSelect(Boolean isMultiSelect) {
        this.isMultiSelect = isMultiSelect;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getUserAnswerId() {
        return userAnswerId;
    }

    public void setUserAnswerId(String userAnswerId) {
        this.userAnswerId = userAnswerId;
    }

}