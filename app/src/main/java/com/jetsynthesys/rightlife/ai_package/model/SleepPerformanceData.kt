package com.jetsynthesys.rightlife.ai_package.model

import com.google.gson.annotations.SerializedName

data class SleepPerformanceList(

    @SerializedName("sleep_duration"         )
    var sleepDuration        : ArrayList<SleepDuration> = arrayListOf(),
    @SerializedName("sleep_performance_data" )
    var sleepPerformanceData : SleepPerformanceData?    = SleepPerformanceData()

)


data class SleepDuration (

    @SerializedName("record_type"       ) var recordType       : String? = null,
    @SerializedName("unit"              ) var unit             : String? = null,
    @SerializedName("value"             ) var value            : String? = null,
    @SerializedName("source_version"    ) var sourceVersion    : String? = null,
    @SerializedName("start_datetime"    ) var startDatetime    : String? = null,
    @SerializedName("end_datetime"      ) var endDatetime      : String? = null,
    @SerializedName("creation_datetime" ) var creationDatetime : String? = null

)

data class SleepPerformanceData (

    @SerializedName("ideal_sleep_duration" ) var idealSleepDuration : Double? = null,
    @SerializedName("sleep_performance"    ) var sleepPerformance   : Double? = null,
    @SerializedName("message"              ) var message            : String? = null,
    @SerializedName("action_step"          ) var actionStep         : String? = null

)