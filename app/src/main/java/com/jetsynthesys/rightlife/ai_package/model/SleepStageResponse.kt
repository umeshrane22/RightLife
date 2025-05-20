package com.jetsynthesys.rightlife.ai_package.model

import com.google.gson.annotations.SerializedName

data class SleepStageResponse(
    @SerializedName("status_code" )
    var statusCode : Int?,
    @SerializedName("message" )
    var message    : String?,
    @SerializedName("data")
    val sleepStageAllData:  SleepStageAllData?
)

data class SleepStageAllData (
    @SerializedName("date")
    var date             : String?,
    @SerializedName("start_time")
    var start_time             : String?,
    @SerializedName("end_time")
    var end_time             : String?,
    @SerializedName("total_sleep_hours")
    var total_sleep_hours    : Double?,
    @SerializedName("sleep_stage_data")
    var sleepStageData   : ArrayList<SleepStageData> = arrayListOf(),
    @SerializedName("sleep_summary")
    var sleepSummary     : SleepSummary?,
    @SerializedName("sleep_percentages")
    var sleepPercentages : SleepPercentages?,
    @SerializedName("sleep_insight_detail" )
    var sleepInsightDetail              : StageInsightDetail?
)

data class StageInsightDetail(
    @SerializedName("message")
    var message : String? ,
    @SerializedName("tip")
    var tip  : String?
)