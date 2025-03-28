package com.example.rlapp.ai_package.model

import com.google.gson.annotations.SerializedName

data class SleepSummary(

    @SerializedName("Core"  )
    var Core  : Int?,
    @SerializedName("Deep"  )
    var Deep  : Int?,
    @SerializedName("REM"   )
    var REM   : Double?,
    @SerializedName("Awake" )
    var Awake : Int?
)
