package com.example.rlapp.apimodel.rledit;

import com.example.rlapp.ui.Articles.models.Article;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Top {

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
    private List<String> subCategories;
    @SerializedName("artist")
    @Expose
    private List<Artist> artist;
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
    /*@SerializedName("article")
    @Expose
    private String article;*/
    /*@SerializedName("article")
    @Expose
    private List<Article> article;*/
    @SerializedName("tags")
    @Expose
    private List<String> tags;
    @SerializedName("episodeCount")
    @Expose
    private Integer episodeCount;
    @SerializedName("isPromoted")
    @Expose
    private Boolean isPromoted;
    @SerializedName("isDeleted")
    @Expose
    private Boolean isDeleted;
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
    @SerializedName("__v")
    @Expose
    private Integer v;
    @SerializedName("shareUrl")
    @Expose
    private String shareUrl;
    @SerializedName("isActive")
    @Expose
    private Boolean isActive;
    @SerializedName("order")
    @Expose
    private Integer order;
    @SerializedName("promotedAt")
    @Expose
    private String promotedAt;
    @SerializedName("watchProgress")
    @Expose
    private List<Object> watchProgress;
    @SerializedName("isWatchedOver60")
    @Expose
    private Boolean isWatchedOver60;
    @SerializedName("contentTypeOrder")
    @Expose
    private Integer contentTypeOrder;

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

    public List<String> getSubCategories() {
        return subCategories;
    }

    public void setSubCategories(List<String> subCategories) {
        this.subCategories = subCategories;
    }

    public List<Artist> getArtist() {
        return artist;
    }

    public void setArtist(List<Artist> artist) {
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

    /*public String getArticle() {
        return article;
    }

    public void setArticle(String article) {
        this.article = article;
    }*/

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
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

    public Boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
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

    public Integer getV() {
        return v;
    }

    public void setV(Integer v) {
        this.v = v;
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

    public String getPromotedAt() {
        return promotedAt;
    }

    public void setPromotedAt(String promotedAt) {
        this.promotedAt = promotedAt;
    }

    public List<Object> getWatchProgress() {
        return watchProgress;
    }

    public void setWatchProgress(List<Object> watchProgress) {
        this.watchProgress = watchProgress;
    }

    public Boolean getIsWatchedOver60() {
        return isWatchedOver60;
    }

    public void setIsWatchedOver60(Boolean isWatchedOver60) {
        this.isWatchedOver60 = isWatchedOver60;
    }

    public Integer getContentTypeOrder() {
        return contentTypeOrder;
    }

    public void setContentTypeOrder(Integer contentTypeOrder) {
        this.contentTypeOrder = contentTypeOrder;
    }
/*
    public List<Article> getArticle() {
        return article;
    }

    public void setArticle(List<Article> article) {
        this.article = article;
    }*/
}
