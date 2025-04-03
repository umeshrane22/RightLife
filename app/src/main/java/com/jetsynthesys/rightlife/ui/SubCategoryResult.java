package com.jetsynthesys.rightlife.ui;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SubCategoryResult {

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
    @SerializedName("darkThemeThumbnail")
    @Expose
    private String darkThemeThumbnail;
    @SerializedName("isExist")
    @Expose
    private Boolean isExist;

    private Boolean isSelected = false;

    public Boolean getExist() {
        return isExist;
    }

    public void setExist(Boolean exist) {
        isExist = exist;
    }

    public Boolean getSelected() {
        return isSelected;
    }

    public void setSelected(Boolean selected) {
        isSelected = selected;
    }

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

    public String getDarkThemeThumbnail() {
        return darkThemeThumbnail;
    }

    public void setDarkThemeThumbnail(String darkThemeThumbnail) {
        this.darkThemeThumbnail = darkThemeThumbnail;
    }

    public Boolean getIsExist() {
        return isExist;
    }

    public void setIsExist(Boolean isExist) {
        this.isExist = isExist;
    }

}