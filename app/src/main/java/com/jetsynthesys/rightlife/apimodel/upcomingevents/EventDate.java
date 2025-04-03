package com.jetsynthesys.rightlife.apimodel.upcomingevents;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EventDate {

    @SerializedName("month")
    @Expose
    private String month;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("time")
    @Expose
    private String time;

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

}
