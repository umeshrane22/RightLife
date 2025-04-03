package com.jetsynthesys.rightlife.ai_package.model

import com.google.gson.annotations.SerializedName

data class SleepPercentages(

    @SerializedName("Core"  )
    var Core  : Double?,
    @SerializedName("Deep"  )
    var Deep  : Double?,
    @SerializedName("REM"   )
    var REM   : Double?,
    @SerializedName("Awake" )
    var Awake : Int?
)
