package com.jetsynthesys.rightlife.ai_package.model.response

import com.google.gson.annotations.SerializedName

data class DeleteCaloriesResponse(
    @SerializedName("message") val message: String,
    @SerializedName("routine_id") val routineId: String,
    @SerializedName("user_id") val userId: String
)