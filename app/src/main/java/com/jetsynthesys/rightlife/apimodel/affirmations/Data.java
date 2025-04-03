package com.jetsynthesys.rightlife.apimodel.affirmations;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Data {

    @SerializedName("sectionTitle")
    @Expose
    private String sectionTitle;
    @SerializedName("sectionSubtitle")
    @Expose
    private String sectionSubtitle;
    @SerializedName("isShowGrid")
    @Expose
    private Boolean isShowGrid;
    @SerializedName("sortedServices")
    @Expose
    private List<SortedService> sortedServices;

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

    public List<SortedService> getSortedServices() {
        return sortedServices;
    }

    public void setSortedServices(List<SortedService> sortedServices) {
        this.sortedServices = sortedServices;
    }

}