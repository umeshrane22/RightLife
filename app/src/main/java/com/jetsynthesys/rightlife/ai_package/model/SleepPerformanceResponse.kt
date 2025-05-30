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
    var sleepPerformanceAverage : Double?,
    @SerializedName("progress_detail")
    var progress_detail : PerformanceProgressDetail?
)

data class PerformanceProgressDetail(
    @SerializedName("progress_percentage")
    var progress_percentage              : Double?,
    @SerializedName("progress_sign")
    var progress_sign                : String?,
)

data class PerformanceInsightDetail(
    @SerializedName("message")
    var message : String? ,
    @SerializedName("title")
    var title  : String?,
    @SerializedName("subtitle")
    var subtitle  : String?
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