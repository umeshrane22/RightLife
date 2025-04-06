package com.jetsynthesys.rightlife.ai_package.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class AssignRoutineRequest(
    @SerializedName("routine_name") val routineName: String,
    @SerializedName("workout_ids") val workoutIds: List<String>,
    val date: String
) : Parcelable

@Parcelize
data class AssignRoutineResponse(
    val message: String,
    @SerializedName("document_id") val documentId: String
) : Parcelable
