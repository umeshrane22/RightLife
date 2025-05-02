package com.jetsynthesys.rightlife.ai_package.model

import com.google.gson.annotations.SerializedName

data class SleepPerformanceResponse(
    @SerializedName("message")
    var message              : String?,
    @SerializedName("start_datetime")
    var startDatetime        : String?,
    @SerializedName("end_datetime")
    var endDatetime          : String?,
    @SerializedName("sleep_performance_data" )
    var sleepPerformanceData : ArrayList<SleepPerformanceList> = arrayListOf()
)
