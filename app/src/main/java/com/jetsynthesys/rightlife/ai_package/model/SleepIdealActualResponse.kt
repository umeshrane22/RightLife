package com.jetsynthesys.rightlife.ai_package.model

import com.google.gson.annotations.SerializedName

data class SleepIdealActualResponse(
    @SerializedName("status_code")
    var statusCode : Int? ,
    @SerializedName("message")
    var message    : String? ,
    @SerializedName("data")
    var data       : SleepData?
)

data class SleepData(
    @SerializedName("user_id")
    var userId            : String?,
    @SerializedName("source")
    var source            : String?,
    @SerializedName("start_date")
    var startDate         : String?,
    @SerializedName("end_date")
    var endDate           : String?,
    @SerializedName("average_sleep")
    var averageSleep      : Double?,
    @SerializedName("average_needed")
    var averageNeeded     : Double?,
    @SerializedName("sleep_insight_detail" )
    var sleepInsightDetail              : IdealActualInsightDetail?,
    @SerializedName("time_data_breakdown" )
    var timeDataBreakdown : ArrayList<TimeDataBreakdown> = arrayListOf()
)

data class IdealActualInsightDetail(
    @SerializedName("message")
    var message : String? ,
    @SerializedName("tip")
    var tip  : String?
)

data class TimeDataBreakdown (
    @SerializedName("date")
    var date             : String? ,
    @SerializedName("actual_sleep_hours")
    var actualSleepHours : Double? ,
    @SerializedName("ideal_sleep_hours")
    var idealSleepHours  : Double?
)