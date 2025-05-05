package com.jetsynthesys.rightlife.ai_package.model

import com.google.gson.annotations.SerializedName

data class SleepStageData(
    @SerializedName("unit")
    var unit          : String?,
    @SerializedName("end_datetime")
    var endDatetime   : String?,
    @SerializedName("start_datetime")
    var startDatetime : String?,
    @SerializedName("record_type")
    var recordType    : String?,
    @SerializedName("value" )
    var value         : String?,
    @SerializedName("source_name")
    var sourceName    : String?,
    @SerializedName("_id")
    var Id            : String?
)
