package com.jetsynthesys.rightlife.ui.mindaudit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class AllAssessment implements Serializable {

    @SerializedName("DASS-21")
    @Expose
    private String dass21;
    @SerializedName("Sleep Audit")
    @Expose
    private String sleepAudit;
    @SerializedName("CAS")
    @Expose
    private String cas;
    @SerializedName("GAD-7")
    @Expose
    private String gad7;
    @SerializedName("OHQ")
    @Expose
    private String ohq;
    @SerializedName("PHQ-9")
    @Expose
    private String phq9;

    public String getCas() {
        return cas;
    }

    public void setCas(String cas) {
        this.cas = cas;
    }

    public String getGad7() {
        return gad7;
    }

    public void setGad7(String gad7) {
        this.gad7 = gad7;
    }

    public String getOhq() {
        return ohq;
    }

    public void setOhq(String ohq) {
        this.ohq = ohq;
    }

    public String getPhq9() {
        return phq9;
    }

    public void setPhq9(String phq9) {
        this.phq9 = phq9;
    }

    public String getDass21() {
        return dass21;
    }

    public void setDass21(String dass21) {
        this.dass21 = dass21;
    }

    public String getSleepAudit() {
        return sleepAudit;
    }

    public void setSleepAudit(String sleepAudit) {
        this.sleepAudit = sleepAudit;
    }

}