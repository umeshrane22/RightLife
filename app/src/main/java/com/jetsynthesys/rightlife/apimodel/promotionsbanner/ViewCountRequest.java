package com.jetsynthesys.rightlife.apimodel.promotionsbanner;

public class ViewCountRequest {
    private String id;
    private String userId;

    public ViewCountRequest(String id, String userId) {
        this.id = id;
        this.userId = userId;
    }

    public String getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }
}