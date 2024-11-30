package com.example.rlapp.ui.mindaudit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class SuggestedAssessments implements Serializable{

    @SerializedName("CAS")
    @Expose
    private Object cas;
    @SerializedName("GAD-7")
    @Expose
    private Object gad7;
    @SerializedName("OHQ")
    @Expose
    private Object ohq;
    @SerializedName("PHQ-9")
    @Expose
    private Object phq9;
    @SerializedName("DASS-21")
    @Expose
    private Object dass21;
    @SerializedName("Sleep Audit")
    @Expose
    private Object sleepAudit;

    public Object getDass21() {
        return dass21;
    }

    public void setDass21(Object dass21) {
        this.dass21 = dass21;
    }

    public Object getSleepAudit() {
        return sleepAudit;
    }

    public void setSleepAudit(Object sleepAudit) {
        this.sleepAudit = sleepAudit;
    }

    public Object getCas() {
        return cas;
    }

    public void setCas(Object cas) {
        this.cas = cas;
    }

    public Object getGad7() {
        return gad7;
    }

    public void setGad7(Object gad7) {
        this.gad7 = gad7;
    }

    public Object getOhq() {
        return ohq;
    }

    public void setOhq(Object ohq) {
        this.ohq = ohq;
    }

    public Object getPhq9() {
        return phq9;
    }

    public void setPhq9(Object phq9) {
        this.phq9 = phq9;
    }

}