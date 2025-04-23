package com.jetsynthesys.rightlife.ai_package.model

import com.google.gson.annotations.SerializedName

data class WorkoutResponse(
    @SerializedName("message")
    val message: String,
    @SerializedName("range_type")
    val range_type: String,
    @SerializedName("start_date")
    val start_date: String,
    @SerializedName("end_date")
    val end_date: String,
    @SerializedName("synced_workouts")
    val synced_workouts: List<SyncedWorkout>,
    @SerializedName("unsynced_workouts")
    val unsynced_workouts: List<UnsyncedWorkout>
)
