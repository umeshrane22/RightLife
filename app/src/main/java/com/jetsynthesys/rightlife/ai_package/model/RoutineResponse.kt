package com.jetsynthesys.rightlife.ai_package.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@kotlinx.serialization.Serializable
data class RoutineResponse(
    val user_id: String,
    val routine_name: String,
    val workouts: List<Workout>,
    val created_at: String,
    val updated_at: String?
) {
    @Serializable
    data class Workout(
        @SerialName("activityId")
        val activityId: String,
        @SerialName("activity_name")
        val activity_name: String,
        val intensity: String,
        @SerialName("duration_min")
        val duration_min: Double
    )
}