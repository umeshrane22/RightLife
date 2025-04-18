package com.jetsynthesys.rightlife.newdashboard.model

import com.google.gson.annotations.SerializedName

data class DiscoverDataItem(
    @SerializedName("key") val key: String?,
    @SerializedName("parameter") val parameter: String?,
    @SerializedName("def") val def: String?
)

