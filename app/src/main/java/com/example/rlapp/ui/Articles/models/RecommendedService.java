package com.example.rlapp.ui.Articles.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RecommendedService {

    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("contentType")
    @Expose
    private String contentType;
    @SerializedName("category")
    @Expose
    private String category;
    @SerializedName("content")
    @Expose
    private String content;
    @SerializedName("contentUrl")
    @Expose
    private String contentUrl;
    @SerializedName("desktopContentUrl")
    @Expose
    private String desktopContentUrl;
    @SerializedName("thumbnail")
    @Expose
    private Thumbnail thumbnail;
    @SerializedName("videoThumbnail")
    @Expose
    private VideoThumbnail videoThumbnail;
    @SerializedName("isActive")
    @Expose
    private Boolean isActive;
    @SerializedName("isDeleted")
    @Expose
    private Boolean isDeleted;
    @SerializedName("navigationModule")
    @Expose
    private String navigationModule;
    @SerializedName("navigationScreen")
    @Expose
    private String navigationScreen;
    @SerializedName("views")
    @Expose
    private Integer views;
    @SerializedName("order")
    @Expose
    private Integer order;
    @SerializedName("deviceType")
    @Expose
    private DeviceType deviceType;
    @SerializedName("buttonName")
    @Expose
    private String buttonName;
    @SerializedName("titleImage")
    @Expose
    private String titleImage;
    @SerializedName("buttonImage")
    @Expose
    private String buttonImage;
    @SerializedName("seriesType")
    @Expose
    private String seriesType;
    @SerializedName("seriesId")
    @Expose
    private String seriesId;
    @SerializedName("createdAt")
    @Expose
    private Long createdAt;
    @SerializedName("updatedAt")
    @Expose
    private Long updatedAt;
    @SerializedName("__v")
    @Expose
    private Integer v;
    @SerializedName("selectedContentName")
    @Expose
    private String selectedContentName;
    @SerializedName("selectedContentType")
    @Expose
    private String selectedContentType;

    // Getters and setters for all fields (generate these in your IDE)

    // Example Getter
    public String getId() { return id; }

    // Example Setter
    public void setId(String id) { this.id = id; }

    // ... (Getters and setters for all other fields)

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContentUrl() {
        return contentUrl;
    }

    public void setContentUrl(String contentUrl) {
        this.contentUrl = contentUrl;
    }

    public String getDesktopContentUrl() {
        return desktopContentUrl;
    }

    public void setDesktopContentUrl(String desktopContentUrl) {
        this.desktopContentUrl = desktopContentUrl;
    }

    public Thumbnail getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(Thumbnail thumbnail) {
        this.thumbnail = thumbnail;
    }

    public VideoThumbnail getVideoThumbnail() {
        return videoThumbnail;
    }

    public void setVideoThumbnail(VideoThumbnail videoThumbnail) {
        this.videoThumbnail = videoThumbnail;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public Boolean getDeleted() {
        return isDeleted;
    }

    public void setDeleted(Boolean deleted) {
        isDeleted = deleted;
    }

    public String getNavigationModule() {
        return navigationModule;
    }

    public void setNavigationModule(String navigationModule) {
        this.navigationModule = navigationModule;
    }

    public String getNavigationScreen() {
        return navigationScreen;
    }

    public void setNavigationScreen(String navigationScreen) {
        this.navigationScreen = navigationScreen;
    }

    public Integer getViews() {
        return views;
    }

    public void setViews(Integer views) {
        this.views = views;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public DeviceType getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(DeviceType deviceType) {
        this.deviceType = deviceType;
    }

    public String getButtonName() {
        return buttonName;
    }

    public void setButtonName(String buttonName) {
        this.buttonName = buttonName;
    }

    public String getTitleImage() {
        return titleImage;
    }

    public void setTitleImage(String titleImage) {
        this.titleImage = titleImage;
    }

    public String getButtonImage() {
        return buttonImage;
    }

    public void setButtonImage(String buttonImage) {
        this.buttonImage = buttonImage;
    }

    public String getSeriesType() {
        return seriesType;
    }

    public void setSeriesType(String seriesType) {
        this.seriesType = seriesType;
    }

    public String getSeriesId() {
        return seriesId;
    }

    public void setSeriesId(String seriesId) {
        this.seriesId = seriesId;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }

    public Long getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Long updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Integer getV() {
        return v;
    }

    public void setV(Integer v) {
        this.v = v;
    }

    public String getSelectedContentName() {
        return selectedContentName;
    }

    public void setSelectedContentName(String selectedContentName) {
        this.selectedContentName = selectedContentName;
    }

    public String getSelectedContentType() {
        return selectedContentType;
    }

    public void setSelectedContentType(String selectedContentType) {
        this.selectedContentType = selectedContentType;
    }
}
