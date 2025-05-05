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
    @SerializedName("sleep_stage_data")
    var sleepStageData   : ArrayList<SleepStageData> = arrayListOf(),
    @SerializedName("sleep_summary")
    var sleepSummary     : SleepSummary?,
    @SerializedName("sleep_percentages")
    var sleepPercentages : SleepPercentages?
)
