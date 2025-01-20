package com.example.rlapp.ui.new_design.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/*
class OnboardingModuleData {
    @SerializedName("_id")
    @Expose
    var id: String? = null

    @SerializedName("moduleName")
    @Expose
    var moduleName: String? = null

    @SerializedName("thumbnail")
    @Expose
    var thumbnail: String? = null

    @SerializedName("title")
    @Expose
    var title: String? = null

    @SerializedName("subtitle")
    @Expose
    var subtitle: String? = null

    @SerializedName("topics")
    @Expose
    var topics: List<ModuleTopic>? = null

    @SerializedName("createdAt")
    @Expose
    var createdAt: String? = null

    @SerializedName("updatedAt")
    @Expose
    var updatedAt: String? = null

    @SerializedName("__v")
    @Expose
    var v: Int? = null
}*/

class OnboardingModuleData {
    @SerializedName("_id")
    @Expose
    var id: String? = null

    @SerializedName("moduleName")
    @Expose
    var moduleName: String? = null

    @SerializedName("moduleTopic")
    @Expose
    var moduleTopic: String? = null

    @SerializedName("moduleThumbnail")
    @Expose
    var moduleThumbnail: String? = null

    @SerializedName("moduleDarkThemeThumbnail")
    @Expose
    var moduleDarkThemeThumbnail: String? = null

    @SerializedName("order")
    @Expose
    var order: Int? = null

    @SerializedName("isActive")
    @Expose
    var isActive: Boolean? = null

    @SerializedName("createdAt")
    @Expose
    var createdAt: String? = null

    @SerializedName("updatedAt")
    @Expose
    var updatedAt: String? = null

    @SerializedName("__v")
    @Expose
    var v: Int? = null
}
