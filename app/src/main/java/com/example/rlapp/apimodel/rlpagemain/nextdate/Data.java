package com.example.rlapp.apimodel.rlpagemain.nextdate;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {

    @SerializedName("MindAuditDateCount")
    @Expose
    private Integer mindAuditDateCount;
    @SerializedName("MindAuditBasicAssesmentDate")
    @Expose
    private String mindAuditBasicAssesmentDate;

    public Integer getMindAuditDateCount() {
        return mindAuditDateCount;
    }

    public void setMindAuditDateCount(Integer mindAuditDateCount) {
        this.mindAuditDateCount = mindAuditDateCount;
    }

    public String getMindAuditBasicAssesmentDate() {
        return mindAuditBasicAssesmentDate;
    }

    public void setMindAuditBasicAssesmentDate(String mindAuditBasicAssesmentDate) {
        this.mindAuditBasicAssesmentDate = mindAuditBasicAssesmentDate;
    }

}