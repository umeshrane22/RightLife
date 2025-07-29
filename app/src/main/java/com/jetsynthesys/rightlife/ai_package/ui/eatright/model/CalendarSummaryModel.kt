package com.jetsynthesys.rightlife.ai_package.ui.eatright.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class CalendarSummaryModel(
    @SerializedName("isAvailable")
    val isAvailable: Boolean = false,
    @SerializedName("weekNumber")
    val weekNumber: Int,
    val weekDays: List<CalendarDateModel>,
    @SerializedName("totalWeekCaloriesBurned")
    val totalWeekCaloriesBurned : Double,
    @SerializedName("weekStartDate")
    val weekStartDate: String,
    @SerializedName("sign")
    val sign : String,
    @SerializedName("user_goal")
    val userGoal: String
): Parcelable