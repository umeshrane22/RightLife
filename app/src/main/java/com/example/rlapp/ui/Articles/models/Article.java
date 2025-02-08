package com.example.rlapp.ui.Articles.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Article {

    @SerializedName("htmlContent")
    @Expose
    private String htmlContent;
    @SerializedName("thumbnail")
    @Expose
    private String thumbnail;
    @SerializedName("recommendedProduct")
    @Expose
    private RecommendedProduct recommendedProduct;
    @SerializedName("funFacts")
    @Expose
    private FunFacts funFacts;
    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("recommendedArticle")
    @Expose
    private RecommendedArticle recommendedArticle;

    @SerializedName("recommendedLive")
    @Expose
    private RecommendedLive recommendedLive;
    @SerializedName("recommendedService")
    @Expose
    private RecommendedService recommendedService;

    public String getHtmlContent() {
        return htmlContent;
    }

    public void setHtmlContent(String htmlContent) {
        this.htmlContent = htmlContent;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public RecommendedProduct getRecommendedProduct() {
        return recommendedProduct;
    }

    public void setRecommendedProduct(RecommendedProduct recommendedProduct) {
        this.recommendedProduct = recommendedProduct;
    }

    public FunFacts getFunFacts() {
        return funFacts;
    }

    public void setFunFacts(FunFacts funFacts) {
        this.funFacts = funFacts;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public RecommendedArticle getRecommendedArticle() {
        return recommendedArticle;
    }

    public void setRecommendedArticle(RecommendedArticle recommendedArticle) {
        this.recommendedArticle = recommendedArticle;
    }

    public RecommendedLive getRecommendedLive() {
        return recommendedLive;
    }

    public void setRecommendedLive(RecommendedLive recommendedLive) {
        this.recommendedLive = recommendedLive;
    }

    public RecommendedService getRecommendedService() {
        return recommendedService;
    }

    public void setRecommendedService(RecommendedService recommendedService) {
        this.recommendedService = recommendedService;
    }

}