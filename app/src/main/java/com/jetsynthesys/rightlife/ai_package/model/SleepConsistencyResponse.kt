package com.jetsynthesys.rightlife.ai_package.model

import com.google.gson.annotations.SerializedName

data class SleepConsistencyResponse(
    @SerializedName("status_code" )
    var statusCode : Int? ,
    @SerializedName("message")
    var message    : String? ,
    @SerializedName("data" )
    var data       : SleepConsistencyAllData?
)

data class SleepConsistencyAllData (
    @SerializedName("user_id")
    var userId                 : String?  ,
    @SerializedName("source")
    var source                 : String?  ,
    @SerializedName("start_date")
    var startDate              : String?  ,
    @SerializedName("end_date")
    var endDate                : String? ,
    @SerializedName("sleep_details")
    var sleepDetails           : ArrayList<SleepDetails>       = arrayListOf(),
    @SerializedName("sleep_consistency_detail" )
    var sleepConsistencyDetail : SleepConsistencyDetail?
)

data class SleepConsistencyDetail (
    @SerializedName("average_sleep_start_time")
    var averageSleepStartTime     : String?,
    @SerializedName("average_wake_time")
    var averageWakeTime           : String?,
    @SerializedName("average_sleep_duration_hours")
    var averageSleepDurationHours : Double?,
    @SerializedName("consistency_message")
    var consistencyMessage        : String?,
    @SerializedName("action_step")
    var actionStep                : String?

)

data class SleepDetails (
    @SerializedName("date")
    var date               : String? ,
    @SerializedName("sleep_duration_hours")
    var sleepDurationHours : Double? ,
    @SerializedName("sleep_start_time")
    var sleepStartTime     : String? ,
    @SerializedName("sleep_end_time")
    var sleepEndTime       : String?
)