package com.jetsynthesys.rightlife.apimodel.rlpagemodels.continuemodela;


import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class EpisodeDetails {

    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("contentId")
    @Expose
    private String contentId;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("episodeNumber")
    @Expose
    private Integer episodeNumber;
    @SerializedName("desc")
    @Expose
    private String desc;
    @SerializedName("artist")
    @Expose
    private List<String> artist;
    @SerializedName("thumbnail")
    @Expose
    private ThumbnailEpisodeDetails thumbnail;
    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("previewUrl")
    @Expose
    private String previewUrl;
    @SerializedName("pricingTier")
    @Expose
    private String pricingTier;
    @SerializedName("tags")
    @Expose
    private List<String> tags;
    @SerializedName("isDeleted")
    @Expose
    private Boolean isDeleted;
    @SerializedName("meta")
    @Expose
    private MetaEpisodeDetails meta;
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
    @SerializedName("tagDetails")
    @Expose
    private List<TagDetail> tagDetails;
    @SerializedName("artistDetails")
    @Expose
    private List<ArtistDetail> artistDetails;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContentId() {
        return contentId;
    }

    public void setContentId(String contentId) {
        this.contentId = contentId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getEpisodeNumber() {
        return episodeNumber;
    }

    public void setEpisodeNumber(Integer episodeNumber) {
        this.episodeNumber = episodeNumber;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public List<String> getArtist() {
        return artist;
    }

    public void setArtist(List<String> artist) {
        this.artist = artist;
    }

    public ThumbnailEpisodeDetails getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(ThumbnailEpisodeDetails thumbnail) {
        this.thumbnail = thumbnail;
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

    public String getPricingTier() {
        return pricingTier;
    }

    public void setPricingTier(String pricingTier) {
        this.pricingTier = pricingTier;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public Boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public MetaEpisodeDetails getMeta() {
        return meta;
    }

    public void setMeta(MetaEpisodeDetails meta) {
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

    public List<TagDetail> getTagDetails() {
        return tagDetails;
    }

    public void setTagDetails(List<TagDetail> tagDetails) {
        this.tagDetails = tagDetails;
    }

    public List<ArtistDetail> getArtistDetails() {
        return artistDetails;
    }

    public void setArtistDetails(List<ArtistDetail> artistDetails) {
        this.artistDetails = artistDetails;
    }

}
