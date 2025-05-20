package com.jetsynthesys.rightlife.ai_package.model

import com.google.gson.annotations.SerializedName

data class SleepPerformanceResponse(
    @SerializedName("status_code")
    var status_code              : Int?,
    @SerializedName("message")
    var message              : String?,
    @SerializedName("data" )
    var sleepPerformanceAllData : SleepPerformanceAllData?
)

data class SleepPerformanceAllData (
    @SerializedName("start_datetime")
    var startDatetime        : String?,
    @SerializedName("end_datetime")
    var endDatetime          : String?,
    @SerializedName("sleep_performance_data")
    var sleepPerformanceList : ArrayList<SleepPerformanceList> = arrayListOf(),
    @SerializedName("sleep_insight_detail" )
    var sleepInsightDetail              : PerformanceInsightDetail?,
    @SerializedName("sleep_performance_average" )
    var sleepPerformanceAverage : Double?
)

data class PerformanceInsightDetail(
    @SerializedName("message")
    var message : String? ,
    @SerializedName("tip")
    var tip  : String?
)

data class SleepPerformanceList(
    @SerializedName("date")
    var date                 : String?,
    @SerializedName("sleep_duration")
    var sleepDuration        : SleepDuration?,
    @SerializedName("sleep_performance_data" )
    var sleepPerformanceData : SleepPerformanceData?
)

data class SleepDuration (
    @SerializedName("actual_sleep_duration_hours" )
    var actualSleepDurationHours : Double? = null
)

data class SleepPerformanceData (
    @SerializedName("ideal_sleep_duration_hours")
    var idealSleepDurationHours : Double?,
    @SerializedName("sleep_performance")
    var sleepPerformance        : Double?,
    @SerializedName("message")
    var message                 : String?,
    @SerializedName("action_step")
    var actionStep              : String?
)