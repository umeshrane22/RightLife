package com.example.rlapp.ui.Articles.models;

import com.example.rlapp.apimodel.liveevents.CoverImage;
import com.example.rlapp.ui.search.IntroVideo;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;


public class RecommendedLive {

    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("desc")
    @Expose
    private String desc;
    @SerializedName("appId")
    @Expose
    private String appId;
    @SerializedName("eventType")
    @Expose
    private String eventType;
    @SerializedName("introVideo")
    @Expose
    private IntroVideo introVideo;
    @SerializedName("coverImage")
    @Expose
    private CoverImage coverImage;
    @SerializedName("thumbnail")
    @Expose
    private Thumbnail__1 thumbnail;
    @SerializedName("startDateTime")
    @Expose
    private String startDateTime;
    @SerializedName("endDateTime")
    @Expose
    private String endDateTime;
    @SerializedName("lastScheduleStartDateTime")
    @Expose
    private String lastScheduleStartDateTime;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("priceInINR")
    @Expose
    private Integer priceInINR;
    @SerializedName("priceInUSD")
    @Expose
    private Integer priceInUSD;
    @SerializedName("trailPriceInINR")
    @Expose
    private Integer trailPriceInINR;
    @SerializedName("trailPriceInUSD")
    @Expose
    private Integer trailPriceInUSD;
    @SerializedName("liveClassCount")
    @Expose
    private Integer liveClassCount;
    @SerializedName("isFeatured")
    @Expose
    private Boolean isFeatured;
    @SerializedName("isReposted")
    @Expose
    private Boolean isReposted;
    @SerializedName("instructorId")
    @Expose
    private String instructorId;
    @SerializedName("handlerUrl")
    @Expose
    private String handlerUrl;
    @SerializedName("eventCategory")
    @Expose
    private List<String> eventCategory;
    @SerializedName("eventLevel")
    @Expose
    private String eventLevel;
    @SerializedName("tags")
    @Expose
    private List<String> tags;
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
    @SerializedName("ctaText")
    @Expose
    private String ctaText;
    @SerializedName("currency")
    @Expose
    private String currency;
    @SerializedName("offerText")
    @Expose
    private String offerText;
    @SerializedName("originalPriceInINR")
    @Expose
    private Integer originalPriceInINR;
    @SerializedName("originalPriceInUSD")
    @Expose
    private Integer originalPriceInUSD;
    @SerializedName("rating")
    @Expose
    private Integer rating;
    @SerializedName("sectionSubtitle")
    @Expose
    private String sectionSubtitle;
    @SerializedName("sectionTitle")
    @Expose
    private String sectionTitle;

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

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public IntroVideo getIntroVideo() {
        return introVideo;
    }

    public void setIntroVideo(IntroVideo introVideo) {
        this.introVideo = introVideo;
    }

    public CoverImage getCoverImage() {
        return coverImage;
    }

    public void setCoverImage(CoverImage coverImage) {
        this.coverImage = coverImage;
    }

    public Thumbnail__1 getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(Thumbnail__1 thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(String startDateTime) {
        this.startDateTime = startDateTime;
    }

    public String getEndDateTime() {
        return endDateTime;
    }

    public void setEndDateTime(String endDateTime) {
        this.endDateTime = endDateTime;
    }

    public String getLastScheduleStartDateTime() {
        return lastScheduleStartDateTime;
    }

    public void setLastScheduleStartDateTime(String lastScheduleStartDateTime) {
        this.lastScheduleStartDateTime = lastScheduleStartDateTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getPriceInINR() {
        return priceInINR;
    }

    public void setPriceInINR(Integer priceInINR) {
        this.priceInINR = priceInINR;
    }

    public Integer getPriceInUSD() {
        return priceInUSD;
    }

    public void setPriceInUSD(Integer priceInUSD) {
        this.priceInUSD = priceInUSD;
    }

    public Integer getTrailPriceInINR() {
        return trailPriceInINR;
    }

    public void setTrailPriceInINR(Integer trailPriceInINR) {
        this.trailPriceInINR = trailPriceInINR;
    }

    public Integer getTrailPriceInUSD() {
        return trailPriceInUSD;
    }

    public void setTrailPriceInUSD(Integer trailPriceInUSD) {
        this.trailPriceInUSD = trailPriceInUSD;
    }

    public Integer getLiveClassCount() {
        return liveClassCount;
    }

    public void setLiveClassCount(Integer liveClassCount) {
        this.liveClassCount = liveClassCount;
    }

    public Boolean getIsFeatured() {
        return isFeatured;
    }

    public void setIsFeatured(Boolean isFeatured) {
        this.isFeatured = isFeatured;
    }

    public Boolean getIsReposted() {
        return isReposted;
    }

    public void setIsReposted(Boolean isReposted) {
        this.isReposted = isReposted;
    }

    public String getInstructorId() {
        return instructorId;
    }

    public void setInstructorId(String instructorId) {
        this.instructorId = instructorId;
    }

    public String getHandlerUrl() {
        return handlerUrl;
    }

    public void setHandlerUrl(String handlerUrl) {
        this.handlerUrl = handlerUrl;
    }

    public List<String> getEventCategory() {
        return eventCategory;
    }

    public void setEventCategory(List<String> eventCategory) {
        this.eventCategory = eventCategory;
    }

    public String getEventLevel() {
        return eventLevel;
    }

    public void setEventLevel(String eventLevel) {
        this.eventLevel = eventLevel;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
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

    public String getCtaText() {
        return ctaText;
    }

    public void setCtaText(String ctaText) {
        this.ctaText = ctaText;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getOfferText() {
        return offerText;
    }

    public void setOfferText(String offerText) {
        this.offerText = offerText;
    }

    public Integer getOriginalPriceInINR() {
        return originalPriceInINR;
    }

    public void setOriginalPriceInINR(Integer originalPriceInINR) {
        this.originalPriceInINR = originalPriceInINR;
    }

    public Integer getOriginalPriceInUSD() {
        return originalPriceInUSD;
    }

    public void setOriginalPriceInUSD(Integer originalPriceInUSD) {
        this.originalPriceInUSD = originalPriceInUSD;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getSectionSubtitle() {
        return sectionSubtitle;
    }

    public void setSectionSubtitle(String sectionSubtitle) {
        this.sectionSubtitle = sectionSubtitle;
    }

    public String getSectionTitle() {
        return sectionTitle;
    }

    public void setSectionTitle(String sectionTitle) {
        this.sectionTitle = sectionTitle;
    }

}
/*
public class RecommendedLive {

    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("desc")
    @Expose
    private String desc;
    @SerializedName("appId")
    @Expose
    private String appId;
    @SerializedName("eventType")
    @Expose
    private String eventType;
    @SerializedName("introVideo")
    @Expose
    private IntroVideo introVideo;
    @SerializedName("coverImage")
    @Expose
    private CoverImage coverImage;
    @SerializedName("thumbnail")
    @Expose
    private Thumbnail thumbnail;
    @SerializedName("startDateTime")
    @Expose
    private String startDateTime;
    @SerializedName("endDateTime")
    @Expose
    private String endDateTime;
    @SerializedName("lastScheduleStartDateTime")
    @Expose
    private String lastScheduleStartDateTime;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("priceInINR")
    @Expose
    private Integer priceInINR;
    @SerializedName("priceInUSD")
    @Expose
    private Integer priceInUSD;
    @SerializedName("trailPriceInINR")
    @Expose
    private Integer trailPriceInINR;
    @SerializedName("trailPriceInUSD")
    @Expose
    private Integer trailPriceInUSD;
    @SerializedName("liveClassCount")
    @Expose
    private Integer liveClassCount;
    @SerializedName("isFeatured")
    @Expose
    private Boolean isFeatured;
    @SerializedName("isReposted")
    @Expose
    private Boolean isReposted;
    @SerializedName("instructorId")
    @Expose
    private String instructorId;
    @SerializedName("handlerUrl")
    @Expose
    private String handlerUrl;
    @SerializedName("eventCategory")
    @Expose
    private List<String> eventCategory;
    @SerializedName("eventLevel")
    @Expose
    private String eventLevel;
    @SerializedName("tags")
    @Expose
    private List<String> tags;
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
    @SerializedName("ctaText")
    @Expose
    private String ctaText;
    @SerializedName("currency")
    @Expose
    private String currency;
    @SerializedName("offerText")
    @Expose
    private String offerText;
    @SerializedName("originalPriceInINR")
    @Expose
    private Integer originalPriceInINR;
    @SerializedName("originalPriceInUSD")
    @Expose
    private Integer originalPriceInUSD;
    @SerializedName("rating")
    @Expose
    private Integer rating;
    @SerializedName("sectionSubtitle")
    @Expose
    private String sectionSubtitle;
    @SerializedName("sectionTitle")
    @Expose
    private String sectionTitle;

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

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public IntroVideo getIntroVideo() {
        return introVideo;
    }

    public void setIntroVideo(IntroVideo introVideo) {
        this.introVideo = introVideo;
    }

    public CoverImage getCoverImage() {
        return coverImage;
    }

    public void setCoverImage(CoverImage coverImage) {
        this.coverImage = coverImage;
    }

    public Thumbnail getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(Thumbnail thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(String startDateTime) {
        this.startDateTime = startDateTime;
    }

    public String getEndDateTime() {
        return endDateTime;
    }

    public void setEndDateTime(String endDateTime) {
        this.endDateTime = endDateTime;
    }

    public String getLastScheduleStartDateTime() {
        return lastScheduleStartDateTime;
    }

    public void setLastScheduleStartDateTime(String lastScheduleStartDateTime) {
        this.lastScheduleStartDateTime = lastScheduleStartDateTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getPriceInINR() {
        return priceInINR;
    }

    public void setPriceInINR(Integer priceInINR) {
        this.priceInINR = priceInINR;
    }

    public Integer getPriceInUSD() {
        return priceInUSD;
    }

    public void setPriceInUSD(Integer priceInUSD) {
        this.priceInUSD = priceInUSD;
    }

    public Integer getTrailPriceInINR() {
        return trailPriceInINR;
    }

    public void setTrailPriceInINR(Integer trailPriceInINR) {
        this.trailPriceInINR = trailPriceInINR;
    }

    public Integer getTrailPriceInUSD() {
        return trailPriceInUSD;
    }

    public void setTrailPriceInUSD(Integer trailPriceInUSD) {
        this.trailPriceInUSD = trailPriceInUSD;
    }

    public Integer getLiveClassCount() {
        return liveClassCount;
    }

    public void setLiveClassCount(Integer liveClassCount) {
        this.liveClassCount = liveClassCount;
    }

    public Boolean getIsFeatured() {
        return isFeatured;
    }

    public void setIsFeatured(Boolean isFeatured) {
        this.isFeatured = isFeatured;
    }

    public Boolean getIsReposted() {
        return isReposted;
    }

    public void setIsReposted(Boolean isReposted) {
        this.isReposted = isReposted;
    }

    public String getInstructorId() {
        return instructorId;
    }

    public void setInstructorId(String instructorId) {
        this.instructorId = instructorId;
    }

    public String getHandlerUrl() {
        return handlerUrl;
    }

    public void setHandlerUrl(String handlerUrl) {
        this.handlerUrl = handlerUrl;
    }

    public List<String> getEventCategory() {
        return eventCategory;
    }

    public void setEventCategory(List<String> eventCategory) {
        this.eventCategory = eventCategory;
    }

    public String getEventLevel() {
        return eventLevel;
    }

    public void setEventLevel(String eventLevel) {
        this.eventLevel = eventLevel;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
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

    public String getCtaText() {
        return ctaText;
    }

    public void setCtaText(String ctaText) {
        this.ctaText = ctaText;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getOfferText() {
        return offerText;
    }

    public void setOfferText(String offerText) {
        this.offerText = offerText;
    }

    public Integer getOriginalPriceInINR() {
        return originalPriceInINR;
    }

    public void setOriginalPriceInINR(Integer originalPriceInINR) {
        this.originalPriceInINR = originalPriceInINR;
    }

    public Integer getOriginalPriceInUSD() {
        return originalPriceInUSD;
    }

    public void setOriginalPriceInUSD(Integer originalPriceInUSD) {
        this.originalPriceInUSD = originalPriceInUSD;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getSectionSubtitle() {
        return sectionSubtitle;
    }

    public void setSectionSubtitle(String sectionSubtitle) {
        this.sectionSubtitle = sectionSubtitle;
    }

    public String getSectionTitle() {
        return sectionTitle;
    }

    public void setSectionTitle(String sectionTitle) {
        this.sectionTitle = sectionTitle;
    }

}
*/
