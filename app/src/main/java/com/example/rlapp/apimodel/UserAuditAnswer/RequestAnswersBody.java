package com.example.rlapp.apimodel.UserAuditAnswer;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RequestAnswersBody {
    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("body")
    @Expose
    private Body body;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Body getBody() {
        return body;
    }

    public void setBody(Body body) {
        this.body = body;
    }
}
