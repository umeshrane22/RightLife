package com.jetsynthesys.rightlife.ui.Articles.requestmodels;

public class ArticleLikeRequest {
    private String contentId;
    private boolean isLike;

    public ArticleLikeRequest(String contentId, boolean isLike) {
        this.contentId = contentId;
        this.isLike = isLike;
    }

    public String getContentId() {
        return contentId;
    }

    public void setContentId(String contentId) {
        this.contentId = contentId;
    }

    public boolean isLike() {
        return isLike;
    }

    public void setLike(boolean like) {
        isLike = like;
    }
}
