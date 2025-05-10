package com.jetsynthesys.rightlife.ai_package.model

import com.google.gson.annotations.SerializedName

data class SleepLandingResponse(
    @SerializedName("status_code")
    var statusCode : Int? ,
    @SerializedName("message")
    var message    : String?,
    @SerializedName("data")
    var sleepLandingAllData       : SleepLandingAllData?
)

data class SleepLandingAllData (
    @SerializedName("user_id")
    var userId                    : String? ,
    @SerializedName("source")
    var source                    : String? ,
    @SerializedName("date")
    var date                      : String? ,
    @SerializedName("start_date")
    var startDate                 : String? ,
    @SerializedName("end_date")
    var endDate                   : String? ,
    @SerializedName("sleep_stages_detail")
    var sleepStagesDetail         : SleepStagesDetail?,
    @SerializedName("sleep_performance_detail")
    var sleepPerformanceDetail    : SleepPerformanceDetail?,
    @SerializedName("sleep_restorative_detail")
    var sleepRestorativeDetail    : SleepRestorativeDetail?,
    @SerializedName("ideal_vs_actual_sleep_time")
    var idealVsActualSleepTime    : ArrayList<IdealVsActualSleepTime> = arrayListOf(),
    @SerializedName("ideal_sleep_requirement_data")
    var idealSleepRequirementData : IdealSleepRequirementData?,
    @SerializedName("sleep_consistency")
    var sleepConsistency          : SleepConsistency?,
    @SerializedName("recommended_sound")
    var recommendedSound          : String?
)

data class SleepConsistency (
    @SerializedName("start_date")
    var startDate              : String?   ,
    @SerializedName("end_date")
    var endDate                : String?   ,
    @SerializedName("sleep_details" )
    var sleepDetails           : ArrayList<SleepDetails> = arrayListOf(),
    @SerializedName("sleep_consistency_detail" )
    var sleepConsistencyDetail : SleepConsistencyDetail?
)

data class IdealSleepRequirementData (
    @SerializedName("_id")
    var Id                 : String?,
    @SerializedName("sleep_type")
    var sleepType          : String?,
    @SerializedName("current_requirement")
    var currentRequirement : Double?,
    @SerializedName("sleep_datetime")
    var sleepDatetime      : String?,
    @SerializedName("wakeup_datetime")
    var wakeupDatetime     : String?,
    @SerializedName("is_default")
    var isDefault          : Boolean?,
    @SerializedName("date")
    var date               : String?
)

data class IdealVsActualSleepTime (
    @SerializedName("date")
    var date             : String?,
    @SerializedName("actual_sleep_hours")
    var actualSleepHours : Double?,
    @SerializedName("ideal_sleep_hours")
    var idealSleepHours  : Double?
)

data class SleepRestorativeDetail (
    @SerializedName("sleep_start_time")
    var sleepStartTime             : String?,
    @SerializedName("sleep_end_time")
    var sleepEndTime               : String?,
    @SerializedName("sleep_stages_data")
    var sleepStagesData            : ArrayList<SleepStagesData>  = arrayListOf(),
    @SerializedName("calculated_restorative_sleep" )
    var calculatedRestorativeSleep : CalculatedRestorativeSleepData?
)

data class CalculatedRestorativeSleepData (
    @SerializedName("restorative_sleep_percentage")
    var restorativeSleepPercentage : Double? ,
    @SerializedName("restorative_sleep_message")
    var restorativeSleepMessage    : String?,
    @SerializedName("restorative_action_step")
    var restorativeActionStep      : String?
)

data class SleepStagesData (
    @SerializedName("stage")
    var stage           : String?,
    @SerializedName("start_datetime")
    var startDatetime   : String?,
    @SerializedName("end_datetime")
    var endDatetime     : String?,
    @SerializedName("duration_minutes")
    var durationMinutes : Double?
)

data class SleepPerformanceDetail (
    @SerializedName("ideal_sleep_duration")
    var idealSleepDuration   : Double? ,
    @SerializedName("actual_sleep_data")
    var actualSleepData      : ActualSleepLandingData?,
    @SerializedName("sleep_performance_data" )
    var sleepPerformanceData : SleepPerformanceLandingData?
)

data class SleepPerformanceLandingData (
    @SerializedName("sleep_performance" )
    var sleepPerformance : Double? ,
    @SerializedName("message")
    var message          : String?,
    @SerializedName("action_step")
    var actionStep       : String?
)

data class ActualSleepLandingData (
    @SerializedName("actual_sleep_duration_hours" )
    var actualSleepDurationHours : Double?,
    @SerializedName("sleep_start_time")
    var sleepStartTime           : String?,
    @SerializedName("sleep_end_time")
    var sleepEndTime             : String?
)

data class SleepStagesDetail (
    @SerializedName("sleep_stage")
    var sleepStage                : ArrayList<SleepStagesData> = arrayListOf(),
    @SerializedName("sleep_start_time")
    var sleepStartTime            : String?,
    @SerializedName("sleep_end_time")
    var sleepEndTime              : String?,
    @SerializedName("total_sleep_duration_minutes" )
    var totalSleepDurationMinutes : Double?
)