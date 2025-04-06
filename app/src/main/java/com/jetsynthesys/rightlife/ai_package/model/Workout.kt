package com.jetsynthesys.rightlife.ai_package.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Workout(
    @SerializedName("creation_datetime") val creationDatetime: String,
    @SerializedName("start_datetime") val startDatetime: String,
    @SerializedName("end_datetime") val endDatetime: String,
    @SerializedName("source_version") val sourceVersion: String,
    @SerializedName("record_type") val recordType: String,
    @SerializedName("workout_type") val workoutType: String,
    val duration: String, // String in the new data class
    @SerializedName("duration_unit") val durationUnit: String,
    @SerializedName("calories_burned") val caloriesBurned: String, // String in the new data class
    @SerializedName("calories_unit") val caloriesUnit: String,
    val distance: String, // String in the new data class
    @SerializedName("distance_unit") val distanceUnit: String,
    @SerializedName("heart_rate_data") val heartRateData: List<HeartRateDataRoutine>,
    @SerializedName("_id") val id: String,
    val routine: String
) : Parcelable {
    // Helper to convert duration to Int (for calculations)
    val durationInt: Int
        get() = duration.toIntOrNull() ?: 0

    // Helper to convert caloriesBurned to Int
    val caloriesBurnedInt: Int
        get() = caloriesBurned.toIntOrNull() ?: 0

    // Helper to convert distance to Int
    val distanceInt: Int
        get() = distance.toIntOrNull() ?: 0
}

