package com.jetsynthesys.rightlife.ai_package.ui.eatright.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import java.time.LocalDate

@Parcelize
data class CalendarDateModel(
    @SerializedName("date")
    var date: Int = 0,
    @SerializedName("month")
    var month: String = "",
    @SerializedName("year")
    var year: Int = 0,
    @SerializedName("dayOfWeek")
    var dayOfWeek: Int = 0,
    @SerializedName("surplus")
    var surplus: Double = 0.0,
    @SerializedName("currentDate")
    var currentDate: String = "",
    @SerializedName("currentMonth")
    var currentMonth: String ="",
    @SerializedName("isSelected")
    var isSelected: Boolean = false,
    @SerializedName("fullDate")
    var fullDate: String = "",
    @SerializedName("is_available")
    var is_available: Boolean = false,
    @SerializedName("sign")
    var sign : String
): Parcelable