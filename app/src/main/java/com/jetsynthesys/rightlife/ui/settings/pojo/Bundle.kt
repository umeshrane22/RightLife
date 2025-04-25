package com.jetsynthesys.rightlife.ui.settings.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Bundle {
    @SerializedName("name")
    @Expose
    var name: String? = null

    @SerializedName("isLimited")
    @Expose
    var isLimited: Boolean? = null

    @SerializedName("limit")
    @Expose
    var limit: Int? = null

    @SerializedName("used")
    @Expose
    var used: Int? = null

    @SerializedName("entities")
    @Expose
    var entities: List<String>? = null

    @SerializedName("completed")
    @Expose
    var completed: Boolean? = null

    @SerializedName("bundleValue")
    @Expose
    var bundleValue: BundleValue? = null

    @SerializedName("bundleShare")
    @Expose
    var bundleShare: BundleShare? = null

    @SerializedName("_id")
    @Expose
    var id: String? = null
}