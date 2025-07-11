package com.jetsynthesys.rightlife.ai_package.model.response

import com.google.gson.annotations.SerializedName

data class LogWeightResponse(
    @SerializedName("user_id") val userId: String,
    @SerializedName("source") val source: String,
    @SerializedName("weight") val weight: Double,
    @SerializedName("date") val date: String,
    @SerializedName("type") val type: String,
    @SerializedName("created_at") val createdAt: String,
    @SerializedName("updated_at") val updatedAt: String
)
