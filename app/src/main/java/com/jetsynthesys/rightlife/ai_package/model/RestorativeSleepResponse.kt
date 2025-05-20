package com.jetsynthesys.rightlife.ai_package.model

import com.google.gson.annotations.SerializedName

data class RestorativeSleepResponse(
    @SerializedName("status_code")
    var statusCode : Int?,
    @SerializedName("message")
    var message    : String?,
    @SerializedName("data")
    var data       : RestorativeSleepAllData?
)

data class RestorativeSleepAllData (
    @SerializedName("user_id")
    var userId : String?   ,
    @SerializedName("source")
    var source  : String?  ,
    @SerializedName("start_date")
    var startDate    : String?,
    @SerializedName("end_date" )
    var endDate      : String? ,
    @SerializedName("average_restorative_sleep_percentage" )
    var averageRestorativeSleepPercentage : Double?,
    @SerializedName("average_sleep_duration" )
    var averageSleepDuration              : Double?,
    @SerializedName("sleep_insight_detail" )
    var sleepInsightDetail              : RestorativeInsightDetail?,
    @SerializedName("restorative_sleep_details")
    var restorativeSleepDetails           : ArrayList<RestorativeSleepData> = arrayListOf()
)

data class RestorativeInsightDetail(
    @SerializedName("message")
    var message : String? ,
    @SerializedName("tip")
    var tip  : String?
)

data class RestorativeSleepData (
    @SerializedName("date")
    var date                       : String?,
    @SerializedName("sleep_start_time")
    var sleepStartTime             : String?,
    @SerializedName("sleep_end_time")
    var sleepEndTime               : String?,
    @SerializedName("total_sleep_duration_minutes")
    var totalSleepDurationMinutes  : Double?,
    @SerializedName("sleep_stages")
    var sleepStages                : SleepStages?,
    @SerializedName("calculated_restorative_sleep")
    var calculatedRestorativeSleep : CalculatedRestorativeSleep?
)

data class CalculatedRestorativeSleep(
    @SerializedName("restorative_sleep_percentage" )
    var restorativeSleepPercentage : Double?,
    @SerializedName("message")
    var message     : String? ,
    @SerializedName("action_step" )
    var actionStep  : String?
)

data class SleepStages(
    @SerializedName("REM_Sleep")
    var remSleep   : Double? ,
    @SerializedName("Deep_Sleep")
    var deepSleep  : Double? ,
    @SerializedName("Light_Sleep")
    var lightSleep : Double? ,
    @SerializedName("Awake")
    var awake      : Double? ,
    @SerializedName("In_Bed")
    var inBed      : Double?,
    @SerializedName("Asleep")
    var asleep     : Double?
)