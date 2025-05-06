package com.jetsynthesys.rightlife.ai_package.ui.eatright.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import java.time.LocalDate

@Parcelize
data class CalendarDateModel(
    @SerializedName("date")
    val date: Int,
    @SerializedName("month")
    val month: String,
    @SerializedName("year")
    val year: Int,
    @SerializedName("dayOfWeek")
    val dayOfWeek: Int,
    @SerializedName("surplus")
    val surplus: Int,
    @SerializedName("currentDate")
    val currentDate: String,
    @SerializedName("currentMonth")
    val currentMonth: String,
    @SerializedName("isSelected")
    val isSelected: Boolean = false,
    @SerializedName("fullDate")
    val fullDate: String,
    @SerializedName("is_available")
    var is_available: Boolean = false
): Parcelable