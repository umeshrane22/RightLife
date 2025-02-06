package com.example.rlapp.ui.payment;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Price {

    @SerializedName("INR")
    @Expose
    private Integer inr;
    @SerializedName("USD")
    @Expose
    private Integer usd;

    public Integer getInr() {
        return inr;
    }

    public void setInr(Integer inr) {
        this.inr = inr;
    }

    public Integer getUsd() {
        return usd;
    }

    public void setUsd(Integer usd) {
        this.usd = usd;
    }

}