package com.jetsynthesys.rightlife.ai_package.model

import java.io.Serializable

data class CardItem(
    val title: String, // e.g., "Running"
    val duration: String, // e.g., "1 hr 03 mins"
    val caloriesBurned: String, // e.g., "514 cal"
    val avgHeartRate: String, // e.g., "120 bpm"
    val heartRateData: List<HeartRateDataWorkout>, // Heart rate data for the graph
) : Serializable