package com.jetsynthesys.rightlife.apimodel;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PromotionResponse {
    @Expose
    @SerializedName("success")
    private Boolean success;
    @SerializedName("statusCode")
    @Expose
    private Integer statusCode;
    @SerializedName("data")
    @Expose
    private Promotiondata promotiondata;


    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public Promotiondata getPromotiondata() {
        return promotiondata;
    }

    public void setPromotiondata(Promotiondata promotiondata) {
        this.promotiondata = promotiondata;
    }

    public class Promotiondata {

        @SerializedName("promotionList")
        @Expose
        private List<Promotion> promotionList;
        @SerializedName("scrollTime")
        @Expose
        private Integer scrollTime;
        @SerializedName("isScrollReverse")
        @Expose
        private Boolean isScrollReverse;

        public List<Promotion> getPromotionList() {
            return promotionList;
        }

        public void setPromotionList(List<Promotion> promotionList) {
            this.promotionList = promotionList;
        }

        public Integer getScrollTime() {
            return scrollTime;
        }

        public void setScrollTime(Integer scrollTime) {
            this.scrollTime = scrollTime;
        }

        public Boolean getIsScrollReverse() {
            return isScrollReverse;
        }

        public void setIsScrollReverse(Boolean isScrollReverse) {
            this.isScrollReverse = isScrollReverse;
        }

        public class Promotion {

            @SerializedName("_id")
            @Expose
            private String id;
            @SerializedName("name")
            @Expose
            private String name;
            @SerializedName("contentType")
            @Expose
            private String contentType;
            @SerializedName("contentUrl")
            @Expose
            private String contentUrl;
            @SerializedName("desktopContentUrl")
            @Expose
            private String desktopContentUrl;
            @SerializedName("thumbnail")
            @Expose
            private Thumbnail thumbnail;
            @SerializedName("navigationModule")
            @Expose
            private String navigationModule;
            @SerializedName("navigationScreen")
            @Expose
            private String navigationScreen;
            @SerializedName("eventId")
            @Expose
            private String eventId;
            @SerializedName("category")
            @Expose
            private String category;
            @SerializedName("content")
            @Expose
            private String content;
            @SerializedName("views")
            @Expose
            private Integer views;
            @SerializedName("order")
            @Expose
            private Integer order;
            @SerializedName("buttonName")
            @Expose
            private String buttonName;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getContentType() {
                return contentType;
            }

            public void setContentType(String contentType) {
                this.contentType = contentType;
            }

            public String getContentUrl() {
                return contentUrl;
            }

            public void setContentUrl(String contentUrl) {
                this.contentUrl = contentUrl;
            }

            public String getDesktopContentUrl() {
                return desktopContentUrl;
            }

            public void setDesktopContentUrl(String desktopContentUrl) {
                this.desktopContentUrl = desktopContentUrl;
            }

            public Thumbnail getThumbnail() {
                return thumbnail;
            }

            public void setThumbnail(Thumbnail thumbnail) {
                this.thumbnail = thumbnail;
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

            public String getEventId() {
                return eventId;
            }

            public void setEventId(String eventId) {
                this.eventId = eventId;
            }

            public String getCategory() {
                return category;
            }

            public void setCategory(String category) {
                this.category = category;
            }

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public Integer getViews() {
                return views;
            }

            public void setViews(Integer views) {
                this.views = views;
            }

            public Integer getOrder() {
                return order;
            }

            public void setOrder(Integer order) {
                this.order = order;
            }

            public String getButtonName() {
                return buttonName;
            }

            public void setButtonName(String buttonName) {
                this.buttonName = buttonName;
            }

            public class Thumbnail {

                @SerializedName("url")
                @Expose
                private String url;

                public String getUrl() {
                    return url;
                }

                public void setUrl(String url) {
                    this.url = url;
                }

            }
        }
    }
}
