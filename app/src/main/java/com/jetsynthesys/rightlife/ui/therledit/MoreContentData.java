package com.jetsynthesys.rightlife.ui.therledit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MoreContentData {

    @SerializedName("count")
    @Expose
    private Integer count;
    @SerializedName("list")
    @Expose
    private java.util.List<MoreContentList> list;

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public java.util.List<MoreContentList> getList() {
        return list;
    }

    public void setList(java.util.List<MoreContentList> list) {
        this.list = list;
    }

}