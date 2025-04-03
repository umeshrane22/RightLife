package com.jetsynthesys.rightlife.ui.payment;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Service {

   @SerializedName("_id")
   @Expose
   private String id;
   @SerializedName("moduleId")
   @Expose
   private String moduleId;
   @SerializedName("title")
   @Expose
   private String title;
   @SerializedName("moduleImageUrl")
   @Expose
   private String moduleImageUrl;

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

   public String getTitle() {
      return title;
   }

   public void setTitle(String title) {
      this.title = title;
   }

   public String getModuleImageUrl() {
      return moduleImageUrl;
   }

   public void setModuleImageUrl(String moduleImageUrl) {
      this.moduleImageUrl = moduleImageUrl;
   }

}