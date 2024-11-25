package com.example.rlapp.apimodel.Welness;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WellnessContentPlay {

        @SerializedName("contentThumbnail")
        @Expose
        private String contentThumbnail;
        @SerializedName("contentDescription")
        @Expose
        private String contentDescription;
        @SerializedName("contentViews")
        @Expose
        private Integer contentViews;
        @SerializedName("contentCategory")
        @Expose
        private String contentCategory;
        @SerializedName("_id")
        @Expose
        private String id;

        public String getContentThumbnail() {
            return contentThumbnail;
        }

        public void setContentThumbnail(String contentThumbnail) {
            this.contentThumbnail = contentThumbnail;
        }

        public String getContentDescription() {
            return contentDescription;
        }

        public void setContentDescription(String contentDescription) {
            this.contentDescription = contentDescription;
        }

        public Integer getContentViews() {
            return contentViews;
        }

        public void setContentViews(Integer contentViews) {
            this.contentViews = contentViews;
        }

        public String getContentCategory() {
            return contentCategory;
        }

        public void setContentCategory(String contentCategory) {
            this.contentCategory = contentCategory;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

    }
