package com.example.rlapp.apimodel.upcomingevents;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EventsData {


        @SerializedName("eventDate")
        @Expose
        private EventDate eventDate;
        @SerializedName("_id")
        @Expose
        private String id;
        @SerializedName("title")
        @Expose
        private String title;
        @SerializedName("eventTitle")
        @Expose
        private String eventTitle;
        @SerializedName("order")
        @Expose
        private Integer order;
        @SerializedName("navigationModule")
        @Expose
        private String navigationModule;
        @SerializedName("navigationScreen")
        @Expose
        private String navigationScreen;
        @SerializedName("authorName")
        @Expose
        private String authorName;
        @SerializedName("eventThumbnail")
        @Expose
        private String eventThumbnail;
        @SerializedName("isActive")
        @Expose
        private Boolean isActive;
        @SerializedName("isDeleted")
        @Expose
        private Boolean isDeleted;
        @SerializedName("priority")
        @Expose
        private Integer priority;
        @SerializedName("createdAt")
        @Expose
        private String createdAt;
        @SerializedName("updatedAt")
        @Expose
        private String updatedAt;
        @SerializedName("__v")
        @Expose
        private Integer v;

        public EventDate getEventDate() {
            return eventDate;
        }

        public void setEventDate(EventDate eventDate) {
            this.eventDate = eventDate;
        }

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

        public String getEventTitle() {
            return eventTitle;
        }

        public void setEventTitle(String eventTitle) {
            this.eventTitle = eventTitle;
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

        public String getAuthorName() {
            return authorName;
        }

        public void setAuthorName(String authorName) {
            this.authorName = authorName;
        }

        public String getEventThumbnail() {
            return eventThumbnail;
        }

        public void setEventThumbnail(String eventThumbnail) {
            this.eventThumbnail = eventThumbnail;
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


}
