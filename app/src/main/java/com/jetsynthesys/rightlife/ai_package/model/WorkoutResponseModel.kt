package com.jetsynthesys.rightlife.ai_package.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

data class WorkoutResponseModel(
    val success: Boolean,
    val statusCode: Int,
    val data: List<WorkoutList>
)
@Parcelize
data class WorkoutList(
    val _id: String,
    val title: String,
    val iconUrl: String,
    val moduleId: String,
    val order: Int,
    val isActive: Boolean,
    val createdAt: String,
    val updatedAt: String,
    val __v: Int
):Parcelable