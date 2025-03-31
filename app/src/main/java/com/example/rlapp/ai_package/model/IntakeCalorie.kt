package com.example.rlapp.ai_package.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Parcelize
data class IntakeCalorie(
    @SerialName("_id") val id: String?,
    @SerialName("user_id") val userId: String?,
    @SerialName("calories") val calories: Int
) :Parcelable
