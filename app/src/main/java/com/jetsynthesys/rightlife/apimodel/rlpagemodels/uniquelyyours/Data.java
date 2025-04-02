package com.jetsynthesys.rightlife.apimodel.rlpagemodels.uniquelyyours;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

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
    @SerializedName("services")
    @Expose
    private List<Service> services;

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

    public List<Service> getServices() {
        return services;
    }

    public void setServices(List<Service> services) {
        this.services = services;
    }

}
