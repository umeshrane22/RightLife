package com.jetsynthesys.rightlife.ai_package.model.request

import com.google.gson.annotations.SerializedName

data class WaterIntakeRequest(
    @SerializedName("user_id") val userId: String,
    @SerializedName("source") val source: String,
    @SerializedName("water_ml") val waterMl: Int,
    @SerializedName("date") val date: String
)