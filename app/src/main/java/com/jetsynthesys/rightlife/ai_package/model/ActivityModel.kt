package com.jetsynthesys.rightlife.ai_package.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ActivityModel(
    val activityType: String,    // Changed from workoutType
    val duration: String,
    val caloriesBurned: String,
    val intensity: String,
    val calorieId: String,
    val userId :String
) : Parcelable