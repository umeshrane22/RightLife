package com.jetsynthesys.rightlife.ai_package.model

import kotlinx.serialization.Serializable

@kotlinx.serialization.Serializable
data class CreateRoutineRequest(
    val user_id: String,
    val routine_name: String,
    val workouts: List<Workout?>
) {
    @Serializable
    data class Workout(
        val activityId: String,
        val duration: Int,
        val intensity: String
    )
}
