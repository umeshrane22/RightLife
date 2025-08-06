package com.jetsynthesys.rightlife.ai_package.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.jetsynthesys.rightlife.ai_package.model.response.PlanExercise
import kotlinx.parcelize.Parcelize

@Parcelize
data class WorkoutRoutineItem(
    val routineId: String,
    val routineName: String,
    val activityName: String,
    val duration: String,
    val caloriesBurned: String,
    val intensity: String,
    val activityId: String,
    val userId: String,
    val workoutList: List<PlanExerciseWorkout>,
    val isSelected: Boolean = false ):Parcelable

@Parcelize
data class PlanExerciseWorkout( val activityId: String,  val activityName: String, @SerializedName("icon") val icon: String, val intensity: String,  val durationMin: Double, val caloriesBurned: Double ):
    Parcelable
