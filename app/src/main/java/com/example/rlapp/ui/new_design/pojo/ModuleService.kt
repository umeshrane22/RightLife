package com.example.rlapp.ui.new_design.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ModuleService {
    @SerializedName("_id")
    @Expose
    var id: String? = null

    @SerializedName("moduleName")
    @Expose
    var moduleName: String? = null

    @SerializedName("moduleTitle")
    @Expose
    var moduleTitle: String? = null

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

    @SerializedName("isDeleted")
    @Expose
    var isDeleted: Boolean? = null

    @SerializedName("createdAt")
    @Expose
    var createdAt: String? = null

    @SerializedName("updatedAt")
    @Expose
    var updatedAt: String? = null

    @SerializedName("__v")
    @Expose
    var v: Int? = null

    var isSelected = false
}