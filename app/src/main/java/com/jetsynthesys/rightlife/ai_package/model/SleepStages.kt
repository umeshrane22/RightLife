package com.jetsynthesys.rightlife.ai_package.model

import com.google.gson.annotations.SerializedName

data class SleepStages(
    @SerializedName("stage")
    var stage           : String? ,
    @SerializedName("start_datetime")
    var startDatetime   : String? ,
    @SerializedName("end_datetime")
    var endDatetime     : String? ,
    @SerializedName("duration_minutes")
    var durationMinutes : Int?
)
