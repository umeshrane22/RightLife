package com.jetsynthesys.rightlife.ui.Articles.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

class DeviceType {
    @SerializedName("ios")
    @Expose
    private Boolean ios;
    @SerializedName("android")
    @Expose
    private Boolean android;
    @SerializedName("web")
    @Expose
    private Boolean web;

    public Boolean getIos() {
        return ios;
    }

    public void setIos(Boolean ios) {
        this.ios = ios;
    }

    public Boolean getAndroid() {
        return android;
    }

    public void setAndroid(Boolean android) {
        this.android = android;
    }

    public Boolean getWeb() {
        return web;
    }

    public void setWeb(Boolean web) {
        this.web = web;
    }
// Getters and setters
}