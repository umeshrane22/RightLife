package com.example.rlapp.apimodel.curatedcontent;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SpotlightContent {


        @SerializedName("thumbnail")
        @Expose
        private String thumbnail;
        @SerializedName("content_play_icon")
        @Expose
        private String contentPlayIcon;
        @SerializedName("content_title")
        @Expose
        private String contentTitle;
        @SerializedName("content_author_name")
        @Expose
        private List<String> contentAuthorName;
        @SerializedName("content_view_count")
        @Expose
        private Integer contentViewCount;
        @SerializedName("content_type")
        @Expose
        private String contentType;
        @SerializedName("moduleName")
        @Expose
        private String moduleName;
        @SerializedName("masterId")
        @Expose
        private String masterId;
        @SerializedName("navigationModule")
        @Expose
        private String navigationModule;
        @SerializedName("navigationScreen")
        @Expose
        private String navigationScreen;
        @SerializedName("category_id")
        @Expose
        private String categoryId;
        @SerializedName("tag")
        @Expose
        private List<String> tag;
        @SerializedName("sub_category")
        @Expose
        private List<String> subCategory;
        @SerializedName("article")
        @Expose
        private String article;
        @SerializedName("pricing")
        @Expose
        private String pricing;
        @SerializedName("desktopImage")
        @Expose
        private String desktopImage;
        @SerializedName("youtubeUrl")
        @Expose
        private String youtubeUrl;
        @SerializedName("description")
        @Expose
        private String description;
        @SerializedName("isActive")
        @Expose
        private Boolean isActive;
        @SerializedName("isDeleted")
        @Expose
        private Boolean isDeleted;
        @SerializedName("_id")
        @Expose
        private String id;

        public String getThumbnail() {
            return thumbnail;
        }

        public void setThumbnail(String thumbnail) {
            this.thumbnail = thumbnail;
        }

        public String getContentPlayIcon() {
            return contentPlayIcon;
        }

        public void setContentPlayIcon(String contentPlayIcon) {
            this.contentPlayIcon = contentPlayIcon;
        }

        public String getContentTitle() {
            return contentTitle;
        }

        public void setContentTitle(String contentTitle) {
            this.contentTitle = contentTitle;
        }

        public List<String> getContentAuthorName() {
            return contentAuthorName;
        }

        public void setContentAuthorName(List<String> contentAuthorName) {
            this.contentAuthorName = contentAuthorName;
        }

        public Integer getContentViewCount() {
            return contentViewCount;
        }

        public void setContentViewCount(Integer contentViewCount) {
            this.contentViewCount = contentViewCount;
        }

        public String getContentType() {
            return contentType;
        }

        public void setContentType(String contentType) {
            this.contentType = contentType;
        }

        public String getModuleName() {
            return moduleName;
        }

        public void setModuleName(String moduleName) {
            this.moduleName = moduleName;
        }

        public String getMasterId() {
            return masterId;
        }

        public void setMasterId(String masterId) {
            this.masterId = masterId;
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

        public String getCategoryId() {
            return categoryId;
        }

        public void setCategoryId(String categoryId) {
            this.categoryId = categoryId;
        }

        public List<String> getTag() {
            return tag;
        }

        public void setTag(List<String> tag) {
            this.tag = tag;
        }

        public List<String> getSubCategory() {
            return subCategory;
        }

        public void setSubCategory(List<String> subCategory) {
            this.subCategory = subCategory;
        }

        public String getArticle() {
            return article;
        }

        public void setArticle(String article) {
            this.article = article;
        }

        public String getPricing() {
            return pricing;
        }

        public void setPricing(String pricing) {
            this.pricing = pricing;
        }

        public String getDesktopImage() {
            return desktopImage;
        }

        public void setDesktopImage(String desktopImage) {
            this.desktopImage = desktopImage;
        }

        public String getYoutubeUrl() {
            return youtubeUrl;
        }

        public void setYoutubeUrl(String youtubeUrl) {
            this.youtubeUrl = youtubeUrl;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
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

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

    }
