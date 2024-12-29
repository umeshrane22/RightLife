package com.example.rlapp.ui.therledit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Content {

    @SerializedName("count")
    @Expose
    private Integer count;
    @SerializedName("list")
    @Expose
    private java.util.List<com.example.rlapp.ui.therledit.List> list;

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public java.util.List<com.example.rlapp.ui.therledit.List> getList() {
        return list;
    }

    public void setList(java.util.List<com.example.rlapp.ui.therledit.List> list) {
        this.list = list;
    }

}