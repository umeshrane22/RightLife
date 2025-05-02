package com.jetsynthesys.rightlife.ai_package.model

import com.google.gson.annotations.SerializedName

data class RestorativeSleepResponse(
    @SerializedName("message" ) var message : String?,
    @SerializedName("data"    ) var data    : RestorativeSleepData?
)

data class RestorativeSleepData (
    @SerializedName("user_id")
    var userId                 : String?,
    @SerializedName("source")
    var source                 : String?,
    @SerializedName("starttime")
    var starttime              : String?,
    @SerializedName("endtime")
    var endtime                : String?,
    @SerializedName("restorative_sleep_detail")
    var restorativeSleepDetail : ArrayList<RestorativeSleepDetail> = arrayListOf()
)