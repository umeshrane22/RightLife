package com.jetsynthesys.rightlife.ai_package.model

import com.google.gson.annotations.SerializedName

data class SleepConsistencyData(
    @SerializedName("user_id")
    var userId                 : String?,
    @SerializedName("source")
    var source                 : String?,
    @SerializedName("start_date")
    var startDate              : String?,
    @SerializedName("end_date")
    var endDate                : String?,
    @SerializedName("sleep_details")
    var sleepDetails           : ArrayList<SleepDurationData>           = arrayListOf(),
    @SerializedName("sleep_consistency_detail")
    var sleepConsistencyDetail : ArrayList<SleepConsistencyDetail> = arrayListOf()
)
