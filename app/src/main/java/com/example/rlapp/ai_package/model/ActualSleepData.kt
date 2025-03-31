package com.example.rlapp.ai_package.model

import com.google.gson.annotations.SerializedName

data class ActualSleepData(
    @SerializedName("actual_sleep_duration_hours" )
    var actualSleepDurationHours : Double?,
    @SerializedName("sleep_start_time")
    var sleepStartTime           : String?,
    @SerializedName("sleep_end_time")
    var sleepEndTime             : String?
)
