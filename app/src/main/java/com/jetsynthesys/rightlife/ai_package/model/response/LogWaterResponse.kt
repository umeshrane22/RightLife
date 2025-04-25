package com.jetsynthesys.rightlife.ai_package.model.response

import com.google.gson.annotations.SerializedName

data class LogWaterResponse(
    @SerializedName("user_id") val userId: String,
    @SerializedName("source") val source: String,
    @SerializedName("water_ml") val waterMl: Double,
    @SerializedName("date") val date: String,
    @SerializedName("created_at") val createdAt: String,
    @SerializedName("updated_at") val updatedAt: String
)