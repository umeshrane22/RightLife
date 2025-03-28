package com.example.rlapp.ai_package.model

import com.google.gson.annotations.SerializedName

data class WorkoutData(
    @SerializedName("_id") val id: String,
    @SerializedName("user_id") val userId: String,
    @SerializedName("record_type") val recordType: String,
    @SerializedName("workout_type") val workoutType: String,
    @SerializedName("duration") val duration: Int,
    @SerializedName("weight_kg") val weightKg: Int,
    @SerializedName("age") val age: Int,
    @SerializedName("gender") val gender: String,
    @SerializedName("activity_factor") val activityFactor: Double,
    @SerializedName("calculated_activity") val calculatedActivity: Double?,
    @SerializedName("calories_burned") val caloriesBurned: Double,
    @SerializedName("timestamp") val timestamp: String,
    @SerializedName("created_at") val createdAt: String
)

