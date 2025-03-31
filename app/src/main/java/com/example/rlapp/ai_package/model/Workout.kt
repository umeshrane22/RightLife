package com.example.rlapp.ai_package.model

import com.google.gson.annotations.SerializedName

data class Workout(
    @SerializedName("creation_datetime") val creationDatetime: String,
    @SerializedName("start_datetime") val startDatetime: String,
    @SerializedName("end_datetime") val endDatetime: String,
    @SerializedName("source_version") val sourceVersion: String,
    @SerializedName("record_type") val recordType: String,
    @SerializedName("workout_type") val workoutType: String,
    val duration: String,
    @SerializedName("duration_unit") val durationUnit: String,
    @SerializedName("calories_burned") val caloriesBurned: String,
    @SerializedName("calories_unit") val caloriesUnit: String,
    val distance: String,
    @SerializedName("distance_unit") val distanceUnit: String,
    @SerializedName("heart_rate_data") val heartRateData: List<HeartRateDataRoutine>,
    @SerializedName("_id") val id: String,
    val routine: String
)

