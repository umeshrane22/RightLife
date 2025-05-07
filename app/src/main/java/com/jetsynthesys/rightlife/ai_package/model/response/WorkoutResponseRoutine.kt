package com.jetsynthesys.rightlife.ai_package.model.response

import com.google.gson.annotations.SerializedName


// Top-level response containing a list of move routines
data class MoveRoutineResponse(
    @SerializedName("routines")
    val routines: List<MoveRoutineEntry>
)

// Represents a single move routine entry
data class MoveRoutineEntry(
    @SerializedName("routine_id")
    val routineId: String,

    @SerializedName("user_id")
    val userId: String,

    @SerializedName("routine_name")
    val routineName: String,

    @SerializedName("workouts")
    val workouts: List<MoveRoutineWorkout>,

    @SerializedName("createdAt")
    val createdAt: String,

    @SerializedName("updatedAt")
    val updatedAt: String?
)

// Represents a single workout within a move routine
data class MoveRoutineWorkout(
    @SerializedName("activityId")
    val activityId: String,

    @SerializedName("activity_name")
    val activityName: String,

    @SerializedName("intensity")
    val intensity: String,

    @SerializedName("duration_min")
    val durationMin: Double,

    @SerializedName("calories_burned")
    val caloriesBurned: Double
)
