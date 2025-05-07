package com.jetsynthesys.rightlife.ai_package.model

data class WorkoutRoutineItem(
    val routineId: String,              // e.g., "67fd4f018d9be2bdb4796158"
    val routineName: String,            // e.g., "new_test"
    val activityNames: String,          // e.g., "American Football"
    val duration: String,               // e.g., "10 min"
    val caloriesBurned: String,         // e.g., "118.4 kcal"
    val intensity: String,              // e.g., "Low"
    val isSelected: Boolean = false     // For selection state in the adapter
)
