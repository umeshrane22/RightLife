package com.example.rlapp.ai_package.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AvgSteps(
    @SerialName("date") val date: String,
    @SerialName("avg_steps") val avgSteps: Int
)
