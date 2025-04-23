package com.jetsynthesys.rightlife.ai_package.model

import com.google.gson.annotations.SerializedName

data class UnsyncedWorkout(
    @SerializedName("_id")
    val _id: String,

    @SerializedName("user_id")
    val user_id: String,

    @SerializedName("record_type")
    val record_type: String,

    @SerializedName("workout_type")
    val workout_type: String,

    @SerializedName("duration")
    val duration: Int,

    @SerializedName("weight_kg")
    val weight_kg: Int,

    @SerializedName("age")
    val age: Int,

    @SerializedName("gender")
    val gender: String,

    @SerializedName("activity_factor")
    val activity_factor: Float,

    @SerializedName("calculated_activity")
    val calculated_activity: Double,

    @SerializedName("calories_burned")
    val calories_burned: Double,

    @SerializedName("timestamp")
    val timestamp: String,

    @SerializedName("created_at")
    val created_at: String,
    @SerializedName("source")
    val source: String
)
