package com.jetsynthesys.rightlife.ai_package.model.request

import com.google.gson.annotations.SerializedName

data class UpdateCaloriesRequest(
    @SerializedName("user_id")
    val userId: String,

    @SerializedName("duration_min")
    val durationMin: Int,

    @SerializedName("intensity")
    val intensity: String,

    @SerializedName("sessions")
    val sessions: Int,

    @SerializedName("date")
    val date: String
)
