package com.jetsynthesys.rightlife.ai_package.model.request

import com.google.gson.annotations.SerializedName

data class WeightIntakeRequest(
    @SerializedName("user_id") val userId: String,
    @SerializedName("source") val source: String,
    @SerializedName("weight") val waterMl: Float,
    @SerializedName("type") val type: String,
    @SerializedName("date") val date: String
)
