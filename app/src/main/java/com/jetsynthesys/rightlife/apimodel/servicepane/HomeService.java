package com.jetsynthesys.rightlife.apimodel.servicepane;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class HomeService {

    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("category_type")
    @Expose
    private String categoryType;
    @SerializedName("contentUrl")
    @Expose
    private String contentUrl;
    @SerializedName("desktopContentUrl")
    @Expose
    private String desktopContentUrl;
    @SerializedName("thumbnail")
    @Expose
    private Thumbnail thumbnail;
    @SerializedName("isFree")
    @Expose
    private Boolean isFree;
    @SerializedName("order")
    @Expose
    private Integer order;
    @SerializedName("navigationModule")
    @Expose
    private String navigationModule;
    @SerializedName("navigationScreen")
    @Expose
    private String navigationScreen;
    @SerializedName("isActive")
    @Expose
    private Boolean isActive;
    @SerializedName("isDeleted")
    @Expose
    private Boolean isDeleted;
    @SerializedName("_id")
    @Expose
    private String id;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCategoryType() {
        return categoryType;
    }

    public void setCategoryType(String categoryType) {
        this.categoryType = categoryType;
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

    public Boolean getIsFree() {
        return isFree;
    }

    public void setIsFree(Boolean isFree) {
        this.isFree = isFree;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
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

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
