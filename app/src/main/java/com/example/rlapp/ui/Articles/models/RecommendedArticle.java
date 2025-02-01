package com.example.rlapp.ui.Articles.models;

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
    private List<String> subCategories; // Or List<SubCategory> if you have a SubCategory class
    @SerializedName("artist")
    @Expose
    private List<String> artist;       // Or List<Artist> if you have an Artist class
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("pricing")
    @Expose
    private String pricing;
    @SerializedName("thumbnail")
    @Expose
    private Thumbnail thumbnail; // Assuming you have a Thumbnail class (see below)

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

    public List<String> getArtist() { return artist; }
    public void setArtist(List<String> artist) { this.artist = artist; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getPricing() { return pricing; }
    public void setPricing(String pricing) { this.pricing = pricing; }

    public Thumbnail getThumbnail() { return thumbnail; }
    public void setThumbnail(Thumbnail thumbnail) { this.thumbnail = thumbnail; }


}