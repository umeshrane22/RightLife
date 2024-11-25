package com.example.rlapp.apimodel.curatedcontent;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Data {

    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("subtitle")
    @Expose
    private String subtitle;
    @SerializedName("spotlight_thumbnail")
    @Expose
    private String spotlightThumbnail;
    @SerializedName("sub_content_thumbnail")
    @Expose
    private String subContentThumbnail;
    @SerializedName("spotlight_description")
    @Expose
    private String spotlightDescription;
    @SerializedName("author_name")
    @Expose
    private String authorName;
    @SerializedName("view_count")
    @Expose
    private Integer viewCount;
    @SerializedName("priority")
    @Expose
    private Integer priority;
    @SerializedName("spotlightContentList")
    @Expose
    private List<SpotlightContent> spotlightContentList;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getSpotlightThumbnail() {
        return spotlightThumbnail;
    }

    public void setSpotlightThumbnail(String spotlightThumbnail) {
        this.spotlightThumbnail = spotlightThumbnail;
    }

    public String getSubContentThumbnail() {
        return subContentThumbnail;
    }

    public void setSubContentThumbnail(String subContentThumbnail) {
        this.subContentThumbnail = subContentThumbnail;
    }

    public String getSpotlightDescription() {
        return spotlightDescription;
    }

    public void setSpotlightDescription(String spotlightDescription) {
        this.spotlightDescription = spotlightDescription;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public Integer getViewCount() {
        return viewCount;
    }

    public void setViewCount(Integer viewCount) {
        this.viewCount = viewCount;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public List<SpotlightContent> getSpotlightContentList() {
        return spotlightContentList;
    }

    public void setSpotlightContentList(List<SpotlightContent> spotlightContentList) {
        this.spotlightContentList = spotlightContentList;
    }

}
