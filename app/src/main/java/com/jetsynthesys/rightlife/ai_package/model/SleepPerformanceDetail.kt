package com.jetsynthesys.rightlife.ai_package.model

import com.google.gson.annotations.SerializedName

data class SleepPerformanceDetail(
    @SerializedName("ideal_sleep_duration")
    var idealSleepDuration   : Double?,
    @SerializedName("actual_sleep_data")
    var actualSleepData      : ActualSleepData?,
    @SerializedName("sleep_performance_data")
    var sleepPerformanceData : SleepPerformanceData?
)
