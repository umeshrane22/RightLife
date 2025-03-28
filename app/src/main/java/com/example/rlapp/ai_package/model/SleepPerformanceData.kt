package com.example.rlapp.ai_package.model

import com.google.gson.annotations.SerializedName

data class SleepPerformanceData(

    @SerializedName("ideal_sleep_duration" )
    var idealSleepDuration : Double?,
    @SerializedName("sleep_performance"    )
    var sleepPerformance   : Double?,
    @SerializedName("message"              )
    var message            : String? ,
    @SerializedName("action_step"          )
    var actionStep         : String?

)
