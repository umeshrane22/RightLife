package com.jetsynthesys.rightlife.ai_package.model

import com.google.gson.annotations.SerializedName

data class UpdateCalorieRequest(
    @SerializedName("user_id")
    val userId: String,
    @SerializedName("activity")
    val activity: String,
    @SerializedName("duration_min")
    val durationMin: Int,
    @SerializedName("intensity")
    val intensity: String,
    @SerializedName("activity_factor")
    val activityFactor: Double,
    @SerializedName("sessions")
    val sessions: Int
)