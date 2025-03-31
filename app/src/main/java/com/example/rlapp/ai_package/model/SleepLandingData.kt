package com.example.rlapp.ai_package.model

import com.google.gson.annotations.SerializedName

data class SleepLandingData(
    @SerializedName("user_id")
    var userId                 : String?,
    @SerializedName("source")
    var source                 : String?,
    @SerializedName("starttime")
    var starttime              : String?,
    @SerializedName("endtime")
    var endtime                : String?,
    @SerializedName("sleep_stages_data")
    var sleepStagesData        : ArrayList<SleepStageData>        = arrayListOf(),
    @SerializedName("sleep_performance_detail")
    var sleepPerformanceDetail : ArrayList<SleepPerformanceDetail> = arrayListOf(),
    @SerializedName("sleep_restorative_detail")
    var sleepRestorativeDetail : ArrayList<RestorativeSleepDetail> = arrayListOf(),
    @SerializedName("recommended_sound")
    var recommendedSound       : String?

)
