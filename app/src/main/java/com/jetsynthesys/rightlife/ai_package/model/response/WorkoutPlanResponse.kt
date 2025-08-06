package com.jetsynthesys.rightlife.ai_package.model.response

import com.google.gson.annotations.SerializedName

data class WorkoutPlanResponse( @SerializedName("routines") val routines: List<WorkoutPlan> )

data class WorkoutPlan( @SerializedName("routine_id") val routineId: String, @SerializedName("user_id") val userId: String, @SerializedName("routine_name") val routineName: String, @SerializedName("workouts") val workouts: List<PlanExercise>, @SerializedName("createdAt") val createdAt: String, @SerializedName("updatedAt") val updatedAt: String? )

data class PlanExercise( @SerializedName("activityId") val activityId: String, @SerializedName("activity_name") val activityName: String, @SerializedName("intensity") val intensity: String, @SerializedName("icon") val icon: String, @SerializedName("duration_min") val durationMin: Double, @SerializedName("calories_burned") val caloriesBurned: Double )