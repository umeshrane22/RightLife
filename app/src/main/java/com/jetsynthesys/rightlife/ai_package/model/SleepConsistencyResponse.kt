package com.jetsynthesys.rightlife.ai_package.model

import com.google.gson.annotations.SerializedName

data class SleepConsistencyResponse(
    @SerializedName("message"                 ) var message               : String?,
    @SerializedName("sleep_consistency_entry" ) var sleepConsistencyData : SleepConsistencyData?
)
