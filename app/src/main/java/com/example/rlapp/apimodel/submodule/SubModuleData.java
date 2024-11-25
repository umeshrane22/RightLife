package com.example.rlapp.apimodel.submodule;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SubModuleData {
    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("categoryId")
    @Expose
    private String categoryId;
    @SerializedName("forAffirmation")
    @Expose
    private Boolean forAffirmation;
    @SerializedName("forMaster")
    @Expose
    private Boolean forMaster;
    @SerializedName("imageUrl")
    @Expose
    private String imageUrl;
    @SerializedName("moduleId")
    @Expose
    private String moduleId;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("order")
    @Expose
    private Integer order;
    @SerializedName("service")
    @Expose
    private String service;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("isExist")
    @Expose
    private Boolean isExist;
    @SerializedName("navigationModule")
    @Expose
    private String navigationModule;
    @SerializedName("navigationScreen")
    @Expose
    private String navigationScreen;
    @SerializedName("darkThemeThumbnail")
    @Expose
    private String darkThemeThumbnail;
    @SerializedName("isActive")
    @Expose
    private Boolean isActive;
    @SerializedName("isDeleted")
    @Expose
    private Boolean isDeleted;
    @SerializedName("createdAt")
    @Expose
    private String createdAt;
    @SerializedName("updatedAt")
    @Expose
    private String updatedAt;
    @SerializedName("__v")
    @Expose
    private Integer v;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public Boolean getForAffirmation() {
        return forAffirmation;
    }

    public void setForAffirmation(Boolean forAffirmation) {
        this.forAffirmation = forAffirmation;
    }

    public Boolean getForMaster() {
        return forMaster;
    }

    public void setForMaster(Boolean forMaster) {
        this.forMaster = forMaster;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getModuleId() {
        return moduleId;
    }

    public void setModuleId(String moduleId) {
        this.moduleId = moduleId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Boolean getIsExist() {
        return isExist;
    }

    public void setIsExist(Boolean isExist) {
        this.isExist = isExist;
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

    public String getDarkThemeThumbnail() {
        return darkThemeThumbnail;
    }

    public void setDarkThemeThumbnail(String darkThemeThumbnail) {
        this.darkThemeThumbnail = darkThemeThumbnail;
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
