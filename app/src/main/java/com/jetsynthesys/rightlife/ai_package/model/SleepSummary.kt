package com.jetsynthesys.rightlife.ai_package.model

import com.google.gson.annotations.SerializedName

data class SleepSummary(
    @SerializedName("InBed" )
    var    inBed     : Double? ,
    @SerializedName("Deep"   )
    var deep   :  Double?,
    @SerializedName("Light"  )
    var light  :  Double? ,
    @SerializedName("REM"    )
    var rem    :  Double? ,
    @SerializedName("Awake"  )
    var awake  :  Double? ,
    @SerializedName("Asleep" )
    var asleep :  Double?
)
