package com.jetsynthesys.rightlife.ai_package.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class WorkoutSessionRecord(
    // Request fields
    @SerializedName("user_id") val userId: String,
    @SerializedName("activity_id") val activityId: String,
    @SerializedName("duration_min") val durationMin: Int,
    @SerializedName("intensity") val intensity: String,
    @SerializedName("sessions") val sessions: Int,
    @SerializedName("moduleName") val moduleName: String,
    // Response fields (nullable to handle failures)
    @SerializedName("message") val message: String? = null,
    @SerializedName("calories_burned") val caloriesBurned: Double? = null,
    @SerializedName("activity_factor") val activityFactor: Double? = null
) : Parcelable


