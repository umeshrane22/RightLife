package com.example.rlapp.apimodel.liveevents;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UpcomingEvent {

        @SerializedName("_id")
        @Expose
        private String id;
        @SerializedName("eventTitle")
        @Expose
        private String eventTitle;
        @SerializedName("eventTime")
        @Expose
        private String eventTime;
        @SerializedName("eventDate")
        @Expose
        private String eventDate;
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

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getEventTitle() {
            return eventTitle;
        }

        public void setEventTitle(String eventTitle) {
            this.eventTitle = eventTitle;
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

    }