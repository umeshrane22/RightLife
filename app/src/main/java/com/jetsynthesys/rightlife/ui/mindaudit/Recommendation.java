package com.jetsynthesys.rightlife.ui.mindaudit;

import com.jetsynthesys.rightlife.apimodel.Episodes.EpisodeDetail.Artist;
import com.jetsynthesys.rightlife.apimodel.affirmations.thoughtoftheday.Meta;
import com.jetsynthesys.rightlife.apimodel.affirmations.thoughtoftheday.SubCategory;
import com.jetsynthesys.rightlife.apimodel.affirmations.thoughtoftheday.Thumbnail;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Recommendation {

@SerializedName("_id")
@Expose
private String id;
@SerializedName("contentType")
@Expose
private String contentType;
@SerializedName("moduleId")
@Expose
private String moduleId;
@SerializedName("categoryId")
@Expose
private String categoryId;
@SerializedName("subCategories")
@Expose
private List<SubCategory> subCategories;
@SerializedName("artist")
@Expose
private List<Artist> artist;
@SerializedName("title")
@Expose
private String title;
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
@SerializedName("pricing")
@Expose
private String pricing;
@SerializedName("moduleName")
@Expose
private String moduleName;
@SerializedName("categoryName")
@Expose
private String categoryName;
@SerializedName("tagsAsStrings")
@Expose
private List<TagsAsString> tagsAsStrings;
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
@SerializedName("url")
@Expose
private String url;
@SerializedName("previewUrl")
@Expose
private String previewUrl;
@SerializedName("youtubeUrl")
@Expose
private String youtubeUrl;
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

public List<SubCategory> getSubCategories() {
return subCategories;
}

public void setSubCategories(List<SubCategory> subCategories) {
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

public String getPricing() {
return pricing;
}

public void setPricing(String pricing) {
this.pricing = pricing;
}

public String getModuleName() {
return moduleName;
}

public void setModuleName(String moduleName) {
this.moduleName = moduleName;
}

public String getCategoryName() {
return categoryName;
}

public void setCategoryName(String categoryName) {
this.categoryName = categoryName;
}

public List<TagsAsString> getTagsAsStrings() {
return tagsAsStrings;
}

public void setTagsAsStrings(List<TagsAsString> tagsAsStrings) {
this.tagsAsStrings = tagsAsStrings;
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

public String getYoutubeUrl() {
return youtubeUrl;
}

public void setYoutubeUrl(String youtubeUrl) {
this.youtubeUrl = youtubeUrl;
}

public String getPromotedAt() {
return promotedAt;
}

public void setPromotedAt(String promotedAt) {
this.promotedAt = promotedAt;
}

}