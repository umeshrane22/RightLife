package com.jetsynthesys.rightlife.apimodel.welnessresponse;

import java.util.List;

public class Data {
    private boolean isPreference;
    private String sectionTitle;
    private String sectionSubtitle;
    private boolean isShowGrid;
    private List<ContentWellness> contentList;

    // Getters and setters
    public boolean isPreference() {
        return isPreference;
    }

    public void setPreference(boolean preference) {
        isPreference = preference;
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

    public boolean isShowGrid() {
        return isShowGrid;
    }

    public void setShowGrid(boolean showGrid) {
        isShowGrid = showGrid;
    }

    public List<ContentWellness> getContentList() {
        return contentList;
    }

    public void setContentList(List<ContentWellness> contentList) {
        this.contentList = contentList;
    }
}

