package com.jetsynthesys.rightlife.ui.Articles.models;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class RecommendedArticle {

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
    private Thumbnail__1 thumbnail;
    @SerializedName("desc")
    @Expose
    private String desc;
    @SerializedName("article")
    @Expose
    private List<Artist> article;
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
    @SerializedName("wordCount")
    @Expose
    private String wordCount;
    @SerializedName("order")
    @Expose
    private Integer order;
    @SerializedName("isActive")
    @Expose
    private Boolean isActive;
    @SerializedName("tableOfContents")
    @Expose
    private List<String> tableOfContents;
    @SerializedName("readingTime")
    @Expose
    private String readingTime;
    @SerializedName("summary")
    @Expose
    private String summary;
    @SerializedName("createdAt")
    @Expose
    private String createdAt;
    @SerializedName("updatedAt")
    @Expose
    private String updatedAt;
    @SerializedName("__v")
    @Expose
    private Integer v;
    @SerializedName("promotedAt")
    @Expose
    private String promotedAt;

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

    public Thumbnail__1 getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(Thumbnail__1 thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public List<Artist> getArticle() {
        return article;
    }

    public void setArticle(List<Artist> article) {
        this.article = article;
    }

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

    public String getWordCount() {
        return wordCount;
    }

    public void setWordCount(String wordCount) {
        this.wordCount = wordCount;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public List<String> getTableOfContents() {
        return tableOfContents;
    }

    public void setTableOfContents(List<String> tableOfContents) {
        this.tableOfContents = tableOfContents;
    }

    public String getReadingTime() {
        return readingTime;
    }

    public void setReadingTime(String readingTime) {
        this.readingTime = readingTime;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
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

    public String getPromotedAt() {
        return promotedAt;
    }

    public void setPromotedAt(String promotedAt) {
        this.promotedAt = promotedAt;
    }

}

/*
public class RecommendedArticle {

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
    private List<String> subCategories; // Or List<SubCategory> if you have a SubCategory class
    @SerializedName("artist")
    @Expose
    private List<Artist> artist;       // Or List<Artist> if you have an Artist class
    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("desc")
    @Expose
    private String desc;
    @SerializedName("pricing")
    @Expose
    private String pricing;
    @SerializedName("thumbnail")
    @Expose
    private Thumbnail thumbnail; // Assuming you have a Thumbnail class (see below)
    @SerializedName("viewCount")
    @Expose
    private Integer viewCount;
    // Getters and setters for all fields (generate these in your IDE)

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getContentType() { return contentType; }
    public void setContentType(String contentType) { this.contentType = contentType; }

    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }

    public String getPreviewUrl() { return previewUrl; }
    public void setPreviewUrl(String previewUrl) { this.previewUrl = previewUrl; }

    public String getModuleId() { return moduleId; }
    public void setModuleId(String moduleId) { this.moduleId = moduleId; }

    public String getCategoryId() { return categoryId; }
    public void setCategoryId(String categoryId) { this.categoryId = categoryId; }

    public List<String> getSubCategories() { return subCategories; }
    public void setSubCategories(List<String> subCategories) { this.subCategories = subCategories; }

    public List<Artist> getArtist() { return artist; }
    public void setArtist(List<Artist> artist) { this.artist = artist; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getPricing() { return pricing; }
    public void setPricing(String pricing) { this.pricing = pricing; }

    public Thumbnail getThumbnail() { return thumbnail; }
    public void setThumbnail(Thumbnail thumbnail) { this.thumbnail = thumbnail; }


    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Integer getViewCount() {
        return viewCount;
    }

    public void setViewCount(Integer viewCount) {
        this.viewCount = viewCount;
    }
}*/
