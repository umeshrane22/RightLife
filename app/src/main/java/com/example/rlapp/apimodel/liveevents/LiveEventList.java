package com.example.rlapp.apimodel.liveevents;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LiveEventList {

        @SerializedName("attendance")
        @Expose
        private Attendance attendance;
        @SerializedName("_id")
        @Expose
        private String id;
        @SerializedName("sectionTitle")
        @Expose
        private String sectionTitle;
        @SerializedName("sectionSubtitle")
        @Expose
        private String sectionSubtitle;
        @SerializedName("eventTitle")
        @Expose
        private String eventTitle;
        @SerializedName("discountedPrice")
        @Expose
        private String discountedPrice;
        @SerializedName("originalPrice")
        @Expose
        private String originalPrice;
        @SerializedName("currency")
        @Expose
        private String currency;
        @SerializedName("ctaText")
        @Expose
        private String ctaText;
        @SerializedName("offerText")
        @Expose
        private String offerText;
        @SerializedName("authorName")
        @Expose
        private String authorName;
        @SerializedName("authorPicture")
        @Expose
        private String authorPicture;
        @SerializedName("rating")
        @Expose
        private Integer rating;
        @SerializedName("eventTime")
        @Expose
        private String eventTime;
        @SerializedName("eventDate")
        @Expose
        private String eventDate;
        @SerializedName("desktopImage")
        @Expose
        private String desktopImage;
        @SerializedName("mobileImage")
        @Expose
        private String mobileImage;
        @SerializedName("eventType")
        @Expose
        private String eventType;
        @SerializedName("disappear")
        @Expose
        private Boolean disappear;
        @SerializedName("approval")
        @Expose
        private Boolean approval;
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
        @SerializedName("priority")
        @Expose
        private Integer priority;
        @SerializedName("order")
        @Expose
        private Integer order;
        @SerializedName("createdAt")
        @Expose
        private String createdAt;
        @SerializedName("updatedAt")
        @Expose
        private String updatedAt;
        @SerializedName("__v")
        @Expose
        private Integer v;
        @SerializedName("upcoming_event")
        @Expose
        private UpcomingEvent upcomingEvent;

        public Attendance getAttendance() {
            return attendance;
        }

        public void setAttendance(Attendance attendance) {
            this.attendance = attendance;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getSectionTitle() {
            return sectionTitle;
        }

        public void setSectionTitle(String sectionTitle) {
            this.sectionTitle = sectionTitle;
        }

        public String getSectionSubtitle() {
            return sectionSubtitle;
        }

        public void setSectionSubtitle(String sectionSubtitle) {
            this.sectionSubtitle = sectionSubtitle;
        }

        public String getEventTitle() {
            return eventTitle;
        }

        public void setEventTitle(String eventTitle) {
            this.eventTitle = eventTitle;
        }

        public String getDiscountedPrice() {
            return discountedPrice;
        }

        public void setDiscountedPrice(String discountedPrice) {
            this.discountedPrice = discountedPrice;
        }

        public String getOriginalPrice() {
            return originalPrice;
        }

        public void setOriginalPrice(String originalPrice) {
            this.originalPrice = originalPrice;
        }

        public String getCurrency() {
            return currency;
        }

        public void setCurrency(String currency) {
            this.currency = currency;
        }

        public String getCtaText() {
            return ctaText;
        }

        public void setCtaText(String ctaText) {
            this.ctaText = ctaText;
        }

        public String getOfferText() {
            return offerText;
        }

        public void setOfferText(String offerText) {
            this.offerText = offerText;
        }

        public String getAuthorName() {
            return authorName;
        }

        public void setAuthorName(String authorName) {
            this.authorName = authorName;
        }

        public String getAuthorPicture() {
            return authorPicture;
        }

        public void setAuthorPicture(String authorPicture) {
            this.authorPicture = authorPicture;
        }

        public Integer getRating() {
            return rating;
        }

        public void setRating(Integer rating) {
            this.rating = rating;
        }

        public String getEventTime() {
            return eventTime;
        }

        public void setEventTime(String eventTime) {
            this.eventTime = eventTime;
        }

        public String getEventDate() {
            return eventDate;
        }

        public void setEventDate(String eventDate) {
            this.eventDate = eventDate;
        }

        public String getDesktopImage() {
            return desktopImage;
        }

        public void setDesktopImage(String desktopImage) {
            this.desktopImage = desktopImage;
        }

        public String getMobileImage() {
            return mobileImage;
        }

        public void setMobileImage(String mobileImage) {
            this.mobileImage = mobileImage;
        }

        public String getEventType() {
            return eventType;
        }

        public void setEventType(String eventType) {
            this.eventType = eventType;
        }

        public Boolean getDisappear() {
            return disappear;
        }

        public void setDisappear(Boolean disappear) {
            this.disappear = disappear;
        }

        public Boolean getApproval() {
            return approval;
        }

        public void setApproval(Boolean approval) {
            this.approval = approval;
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

        public Integer getPriority() {
            return priority;
        }

        public void setPriority(Integer priority) {
            this.priority = priority;
        }

        public Integer getOrder() {
            return order;
        }

        public void setOrder(Integer order) {
            this.order = order;
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

        public UpcomingEvent getUpcomingEvent() {
            return upcomingEvent;
        }

        public void setUpcomingEvent(UpcomingEvent upcomingEvent) {
            this.upcomingEvent = upcomingEvent;
        }

    }
