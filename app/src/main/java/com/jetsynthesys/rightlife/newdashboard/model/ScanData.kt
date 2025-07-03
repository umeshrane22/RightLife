package com.jetsynthesys.rightlife.newdashboard.model

import com.google.gson.annotations.SerializedName

data class ScanData(
    @SerializedName("createdAt") val createdAt: String?,
    @SerializedName("key") val key: String?,
    @SerializedName("value") val value: Double?,
    @SerializedName("indicator") val indicator: String?,
    @SerializedName("unit") val unit: String?,
    @SerializedName("colour") val colour: String?,
    @SerializedName("parameter") val parameter: String?
)