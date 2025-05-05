package com.jetsynthesys.rightlife.ai_package.model

import com.google.gson.annotations.SerializedName

data class WakeupTimeResponse(
    @SerializedName("message")
    var message : String? ,
    @SerializedName("data")
    var data    : ArrayList<WakeupData> = arrayListOf()
)

data class WakeupData (
    @SerializedName("_id")
    var Id                 : String?,
    @SerializedName("current_requirement")
    var currentRequirement : Double?,
    @SerializedName("wakeup_datetime")
    var wakeupDatetime     : String? ,
    @SerializedName("sleep_type")
    var sleepType          : String?,
    @SerializedName("sleep_datetime")
    var sleepDatetime      : String?,
    @SerializedName("source")
    var source             : String?,
    @SerializedName("user_id")
    var userId             : String?,
    @SerializedName("created_at")
    var createdAt          : String?,
    @SerializedName("is_default")
    var isDefault          : Boolean?,
    @SerializedName("updated_at")
    var updatedAt          : String?
)