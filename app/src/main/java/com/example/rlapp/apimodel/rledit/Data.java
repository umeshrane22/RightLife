package com.example.rlapp.apimodel.rledit;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import javax.annotation.processing.Generated;


public class Data {

    @SerializedName("count")
    @Expose
    private Integer count;
    @SerializedName("sectionTitle")
    @Expose
    private String sectionTitle;
    @SerializedName("sectionSubtitle")
    @Expose
    private String sectionSubtitle;
    @SerializedName("isShowGrid")
    @Expose
    private Boolean isShowGrid;
    @SerializedName("topList")
    @Expose
    private List<Top> topList;

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public String getSectionTitle() {
        return sectionTitle;
    }

    public void setSectionTitle(String sectionTitle) {
        this.sectionTitle = sectionTitle;
    }

    public String getSectionSubtitle() {
        return sectionSubtitle;
    }

    public void setSectionSubtitle(String sectionSubtitle) {
        this.sectionSubtitle = sectionSubtitle;
    }

    public Boolean getIsShowGrid() {
        return isShowGrid;
    }

    public void setIsShowGrid(Boolean isShowGrid) {
        this.isShowGrid = isShowGrid;
    }

    public List<Top> getTopList() {
        return topList;
    }

    public void setTopList(List<Top> topList) {
        this.topList = topList;
    }

}