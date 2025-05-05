package com.jetsynthesys.rightlife.ai_package.model.response

import com.google.gson.annotations.SerializedName

data class GetCaloriesResponse(
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: List<WorkoutData>,
    @SerializedName("pagination") val pagination: Pagination
)

data class WorkoutData(
    @SerializedName("_id") val id: String,
    @SerializedName("user_id") val userId: String,
    @SerializedName("record_type") val recordType: String,
    @SerializedName("workout_type") val workoutType: String,
    @SerializedName("duration") val duration: Int,
    @SerializedName("weight_kg") val weightKg: Float,
    @SerializedName("age") val age: Int,
    @SerializedName("intensity") val intensity: String,
    @SerializedName("gender") val gender: String,
    @SerializedName("activity_factor") val activityFactor: Float,
    @SerializedName("calculated_activity") val calculatedActivity: Float?,
    @SerializedName("calories_burned") val caloriesBurned: Float,
    @SerializedName("timestamp") val timestamp: String,
    @SerializedName("created_at") val createdAt: String
)

data class Pagination(
    @SerializedName("page") val page: Int,
    @SerializedName("limit") val limit: Int,
    @SerializedName("total_results") val totalResults: Int
)