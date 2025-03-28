package com.example.rlapp.ai_package.model

import com.google.gson.annotations.SerializedName

data class SleepTimeDetail(
    @SerializedName("sleep_duration"  ) var sleepDuration : ArrayList<SleepDurationData>,
    @SerializedName("sleep_time_data" ) var sleepTimeData : SleepTimeData?
)
