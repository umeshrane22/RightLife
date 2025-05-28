package com.jetsynthesys.rightlife.ai_package.model

import com.google.gson.annotations.SerializedName

data class FrequentlyLoggedResponse(
    @SerializedName("status_code")
    val statusCode: Int,

    @SerializedName("message")
    val message: String,

    @SerializedName("synced_workouts")
    val syncedWorkouts: List<WorkoutItem>,

    @SerializedName("unsynced_workouts")
    val unsyncedWorkouts: List<WorkoutItem>,

    @SerializedName("total_synced")
    val totalSynced: Int,

    @SerializedName("total_unsynced")
    val totalUnsynced: Int,

    @SerializedName("page")
    val page: Int,

    @SerializedName("limit")
    val limit: Int
)

data class WorkoutItem(
    @SerializedName("_id")
    val id: String,

    @SerializedName("user_id")
    val userId: String,

    @SerializedName("activity_id")
    val activityId: String,

    @SerializedName("record_type")
    val recordType: String,

    @SerializedName("workout_type")
    val workoutType: String,

    @SerializedName("duration")
    val duration: Float,

    @SerializedName("weight_kg")
    val weightKg: Float,

    @SerializedName("age")
    val age: Int,

    @SerializedName("gender")
    val gender: String,

    @SerializedName("intensity")
    val intensity: String,

    @SerializedName("activity_factor")
    val activityFactor: Float,

    @SerializedName("calculated_activity")
    val calculatedActivity: Float,

    @SerializedName("calories_burned")
    val caloriesBurned: Float,

    @SerializedName("timestamp")
    val timestamp: String,

    @SerializedName("createdAt")
    val createdAt: String,

    @SerializedName("updatedAt")
    val updatedAt: String,

    @SerializedName("source")
    val source: String,

    @SerializedName("routine_id")
    val routineId: String? = null // Optional field, nullable in Kotlin
)