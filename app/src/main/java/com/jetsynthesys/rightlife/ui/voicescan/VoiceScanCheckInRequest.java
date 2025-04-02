package com.jetsynthesys.rightlife.ui.voicescan;



import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import kotlin.Pair;

public class VoiceScanCheckInRequest {

@SerializedName("score")
@Expose
private Integer score;
@SerializedName("answerId")
@Expose
private String answerId;
    @SerializedName("report")
    @Expose
    private ArrayList<Pair<String, String>> reportList;

public Integer getScore() {
return score;
}

public void setScore(Integer score) {
this.score = score;
}

public String getAnswerId() {
return answerId;
}

public void setAnswerId(String answerId) {
this.answerId = answerId;
}

    public ArrayList<Pair<String, String>> getReportList() {
        return reportList;
    }

    public void setReportList(ArrayList<Pair<String, String>> reportList) {
        this.reportList = reportList;
    }
}