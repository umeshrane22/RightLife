package com.example.rlapp.apimodel.welnessresponse;


import java.util.List;

public class ContentWellness {
        private String _id;
        private String contentType;
        private String moduleId;
        private String categoryId;

        public String get_id() {
                return _id;
        }

        public void set_id(String _id) {
                this._id = _id;
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

        public int getEpisodeCount() {
                return episodeCount;
        }

        public void setEpisodeCount(int episodeCount) {
                this.episodeCount = episodeCount;
        }

        public boolean isPromoted() {
                return isPromoted;
        }

        public void setPromoted(boolean promoted) {
                isPromoted = promoted;
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

        public int getViewCount() {
                return viewCount;
        }

        public void setViewCount(int viewCount) {
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

        public List<Artist> getArtist() {
                return artist;
        }

        public void setArtist(List<Artist> artist) {
                this.artist = artist;
        }

        public String getPricing() {
                return pricing;
        }

        public void setPricing(String pricing) {
                this.pricing = pricing;
        }

        public boolean isActive() {
                return isActive;
        }

        public void setActive(boolean active) {
                isActive = active;
        }

        public String getPromotedAt() {
                return promotedAt;
        }

        public void setPromotedAt(String promotedAt) {
                this.promotedAt = promotedAt;
        }

        public int getOrder() {
                return order;
        }

        public void setOrder(int order) {
                this.order = order;
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

        public boolean isWatched() {
                return isWatched;
        }

        public void setWatched(boolean watched) {
                isWatched = watched;
        }

        public boolean isAffirmated() {
                return isAffirmated;
        }

        public void setAffirmated(boolean affirmated) {
                isAffirmated = affirmated;
        }

        public boolean isFavourited() {
                return isFavourited;
        }

        public void setFavourited(boolean favourited) {
                isFavourited = favourited;
        }

        public boolean isAlarm() {
                return isAlarm;
        }

        public void setAlarm(boolean alarm) {
                isAlarm = alarm;
        }

        private List<SubCategory> subCategories;
        private String title;
        private Thumbnail thumbnail;
        private String desc;
        private int episodeCount;
        private boolean isPromoted;
        private String seriesType;
        private Meta meta;
        private int viewCount;
        private String createdAt;
        private String updatedAt;
        private String shareUrl;
        private List<Artist> artist;
        private String pricing;
        private boolean isActive;
        private String promotedAt;
        private int order;
        private String moduleName;
        private String categoryName;
        private boolean isWatched;
        private boolean isAffirmated;
        private boolean isFavourited;
        private boolean isAlarm;

        // Getters and setters
        // Add all getters and setters for each field here
    }

