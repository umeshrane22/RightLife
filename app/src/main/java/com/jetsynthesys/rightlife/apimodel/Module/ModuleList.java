package com.jetsynthesys.rightlife.apimodel.Module;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ModuleList {


        @SerializedName("_id")
        @Expose
        private String id;
        @SerializedName("moduleId")
        @Expose
        private String moduleId;
        @SerializedName("moduleType")
        @Expose
        private String moduleType;
        @SerializedName("title")
        @Expose
        private String title;
        @SerializedName("subtitle")
        @Expose
        private String subtitle;
        @SerializedName("moduleThumbnail")
        @Expose
        private String moduleThumbnail;
        @SerializedName("moduleOrder")
        @Expose
        private Integer moduleOrder;
        @SerializedName("navigationModule")
        @Expose
        private String navigationModule;
        @SerializedName("navigationScreen")
        @Expose
        private String navigationScreen;
        @SerializedName("darkThemeThumbnail")
        @Expose
        private String darkThemeThumbnail;
        @SerializedName("priority")
        @Expose
        private Integer priority;
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

        public String getModuleId() {
            return moduleId;
        }

        public void setModuleId(String moduleId) {
            this.moduleId = moduleId;
        }

        public String getModuleType() {
            return moduleType;
        }

        public void setModuleType(String moduleType) {
            this.moduleType = moduleType;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getSubtitle() {
            return subtitle;
        }

        public void setSubtitle(String subtitle) {
            this.subtitle = subtitle;
        }

        public String getModuleThumbnail() {
            return moduleThumbnail;
        }

        public void setModuleThumbnail(String moduleThumbnail) {
            this.moduleThumbnail = moduleThumbnail;
        }

        public Integer getModuleOrder() {
            return moduleOrder;
        }

        public void setModuleOrder(Integer moduleOrder) {
            this.moduleOrder = moduleOrder;
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

        public Integer getPriority() {
            return priority;
        }

        public void setPriority(Integer priority) {
            this.priority = priority;
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