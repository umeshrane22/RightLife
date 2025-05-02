package com.jetsynthesys.rightlife.ai_package.model

import com.google.gson.annotations.SerializedName

data class SleepPerformanceList(
    @SerializedName("sleep_duration")
    var sleepDuration        : ArrayList<SleepDuration> = arrayListOf(),
    @SerializedName("sleep_performance_data" )
    var sleepPerformanceData : SleepPerformanceData?
)


data class SleepDuration (
    @SerializedName("record_type")
    var recordType       : String?,
    @SerializedName("unit")
    var unit             : String?,
    @SerializedName("value")
    var value            : String?,
    @SerializedName("source_version")
    var sourceVersion    : String?,
    @SerializedName("start_datetime")
    var startDatetime    : String?,
    @SerializedName("end_datetime")
    var endDatetime      : String?,
    @SerializedName("creation_datetime")
    var creationDatetime : String?
)

data class SleepPerformanceData (
    @SerializedName("ideal_sleep_duration")
    var idealSleepDuration : Double?,
    @SerializedName("sleep_performance")
    var sleepPerformance   : Double?,
    @SerializedName("message")
    var message            : String?,
    @SerializedName("action_step")
    var actionStep         : String?
)