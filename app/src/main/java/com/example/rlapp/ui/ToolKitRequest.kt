package com.example.rlapp.ui

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ToolKitRequest {
    @SerializedName("userId")
    @Expose
    var userId: String? = null

    @SerializedName("moduleName")
    @Expose
    var moduleName: String? = null

    @SerializedName("moduleId")
    @Expose
    var moduleId: String? = null

    @SerializedName("subtitle")
    @Expose
    var subtitle: String? = null

    @SerializedName("moduleType")
    @Expose
    var moduleType: String? = null

    @SerializedName("categoryId")
    @Expose
    var categoryId: String? = null
}