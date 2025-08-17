package com.jetsynthesys.rightlife.ai_package.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class RoutineWorkoutDisplayModel(
    val id :String,
    val name: String,
    val icon: String,// Maps to moduleName in WorkoutSessionRecord
    val duration: String,      // Maps to durationMin (formatted as "X min")
    val caloriesBurned: String,// Maps to caloriesBurned (formatted as String, handles null)
    val intensity: String      // Maps to intensity
) : Parcelable