package com.jetsynthesys.rightlife.ui.therledit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MoreContentList {

    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("contentType")
    @Expose
    private String contentType;
    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("previewUrl")
    @Expose
    private String previewUrl;
    @SerializedName("moduleId")
    @Expose
    private String moduleId;
    @SerializedName("categoryId")
    @Expose
    private String categoryId;
    @SerializedName("subCategories")
    @Expose
    private java.util.List<String> subCategories;
    @SerializedName("artist")
    @Expose
    private java.util.List<Artist> artist;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("pricing")
    @Expose
    private String pricing;
    @SerializedName("thumbnail")
    @Expose
    private Thumbnail thumbnail;
    @SerializedName("desc")
    @Expose
    private String desc;
    @SerializedName("episodeCount")
    @Expose
    private Integer episodeCount;
    @SerializedName("isPromoted")
    @Expose
    private Boolean isPromoted;
    @SerializedName("youtubeUrl")
    @Expose
    private String youtubeUrl;
    @SerializedName("seriesType")
    @Expose
    private String seriesType;
    @SerializedName("meta")
    @Expose
    private Meta meta;
    @SerializedName("viewCount")
    @Expose
    private Integer viewCount;
    @SerializedName("createdAt")
    @Expose
    private String createdAt;
    @SerializedName("updatedAt")
    @Expose
    private String updatedAt;
    @SerializedName("shareUrl")
    @Expose
    private String shareUrl;
    @SerializedName("isActive")
    @Expose
    private Boolean isActive;
    @SerializedName("order")
    @Expose
    private Integer order;
    @SerializedName("isWatched")
    @Expose
    private Boolean isWatched;
    @SerializedName("isAffirmated")
    @Expose
    private Boolean isAffirmated;
    @SerializedName("isFavourited")
    @Expose
    private Boolean isFavourited;
    @SerializedName("isAlarm")
    @Expose
    private Boolean isAlarm;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPreviewUrl() {
        return previewUrl;
    }

    public void setPreviewUrl(String previewUrl) {
        this.previewUrl = previewUrl;
    }

    public String getModuleId() {
        return moduleId;
    }

    public void setModuleId(String moduleId) {
        this.moduleId = moduleId;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public java.util.List<String> getSubCategories() {
        return subCategories;
    }

    public void setSubCategories(java.util.List<String> subCategories) {
        this.subCategories = subCategories;
    }

    public java.util.List<Artist> getArtist() {
        return artist;
    }

    public void setArtist(java.util.List<Artist> artist) {
        this.artist = artist;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPricing() {
        return pricing;
    }

    public void setPricing(String pricing) {
        this.pricing = pricing;
    }

    public Thumbnail getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(Thumbnail thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Integer getEpisodeCount() {
        return episodeCount;
    }

    public void setEpisodeCount(Integer episodeCount) {
        this.episodeCount = episodeCount;
    }

    public Boolean getIsPromoted() {
        return isPromoted;
    }

    public void setIsPromoted(Boolean isPromoted) {
        this.isPromoted = isPromoted;
    }

    public String getYoutubeUrl() {
        return youtubeUrl;
    }

    public void setYoutubeUrl(String youtubeUrl) {
        this.youtubeUrl = youtubeUrl;
    }

    public String getSeriesType() {
        return seriesType;
    }

    public void setSeriesType(String seriesType) {
        this.seriesType = seriesType;
    }

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }

    public Integer getViewCount() {
        return viewCount;
    }

    public void setViewCount(Integer viewCount) {
        this.viewCount = viewCount;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getShareUrl() {
        return shareUrl;
    }

    public void setShareUrl(String shareUrl) {
        this.shareUrl = shareUrl;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public Boolean getIsWatched() {
        return isWatched;
    }

    public void setIsWatched(Boolean isWatched) {
        this.isWatched = isWatched;
    }

    public Boolean getIsAffirmated() {
        return isAffirmated;
    }

    public void setIsAffirmated(Boolean isAffirmated) {
        this.isAffirmated = isAffirmated;
    }

    public Boolean getIsFavourited() {
        return isFavourited;
    }

    public void setIsFavourited(Boolean isFavourited) {
        this.isFavourited = isFavourited;
    }

    public Boolean getIsAlarm() {
        return isAlarm;
    }

    public void setIsAlarm(Boolean isAlarm) {
        this.isAlarm = isAlarm;
    }

}
