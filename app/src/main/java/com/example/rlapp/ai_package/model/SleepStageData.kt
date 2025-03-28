package com.example.rlapp.ai_package.model

import com.google.gson.annotations.SerializedName

data class SleepStageData(
    @SerializedName("record_type")
    var recordType       : String?,
    @SerializedName("unit")
    var unit             : String?,
    @SerializedName("value")
    var value            : String?,
    @SerializedName("source_version")
    var sourceVersion    : String?,
    @SerializedName("start_datetime")
    var startDatetime    : String?,
    @SerializedName("end_datetime")
    var endDatetime      : String?,
    @SerializedName("creation_datetime")
    var creationDatetime : String?,
    @SerializedName("entry_key")
    var entryKey         : String?,
    @SerializedName("entry_value")
    var entryValue       : String?
)
