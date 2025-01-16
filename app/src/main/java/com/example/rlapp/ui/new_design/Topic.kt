package com.example.rlapp.ui.new_design

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Topic {
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

    @SerializedName("_id")
    @Expose
    var id: String? = null

    var isSelected = false
}