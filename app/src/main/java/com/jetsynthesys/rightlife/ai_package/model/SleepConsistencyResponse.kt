package com.jetsynthesys.rightlife.ai_package.model

import com.google.gson.annotations.SerializedName

data class SleepConsistencyResponse(
    @SerializedName("message")
    var message               : String?,
    @SerializedName("sleep_consistency_entry")
    var sleepConsistencyEntry : SleepConsistencyEntry?
)

data class SleepConsistencyEntry (
    @SerializedName("user_id")
    var userId                 : String?,
    @SerializedName("source")
    var source                 : String?,
    @SerializedName("start_date")
    var startDate              : String?,
    @SerializedName("end_date")
    var endDate                : String?,
    @SerializedName("sleep_details")
    var sleepDetails           : ArrayList<SleepDetails>           = arrayListOf(),
    @SerializedName("sleep_consistency_detail" )
    var sleepConsistencyDetail : ArrayList<SleepConsistencyDetail> = arrayListOf()
)

data class SleepDetails (
    @SerializedName("record_type")
    var recordType       : String? ,
    @SerializedName("unit")
    var unit             : String?,
    @SerializedName("value")
    var value            : String? ,
    @SerializedName("source_version")
    var sourceVersion    : String?,
    @SerializedName("start_datetime")
    var startDatetime    : String?,
    @SerializedName("end_datetime")
    var endDatetime      : String?,
    @SerializedName("creation_datetime" )
    var creationDatetime : String?
)