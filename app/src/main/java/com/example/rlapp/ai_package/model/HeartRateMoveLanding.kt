package com.example.rlapp.ai_package.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class HeartRateMoveLanding(
    @SerialName("creation_datetime") val creationDatetime: String,
    @SerialName("unit") val unit: String,
    @SerialName("value") val value: Int
)
