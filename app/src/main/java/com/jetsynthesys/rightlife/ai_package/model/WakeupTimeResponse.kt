package com.jetsynthesys.rightlife.ai_package.model

import com.google.gson.annotations.SerializedName

data class WakeupTimeResponse(
    @SerializedName("status_code")
    var statusCode : Int?  ,
    @SerializedName("message")
    var message    : String?,
    @SerializedName("data")
    var data       : ArrayList<WakeupData> = arrayListOf()
)

data class WakeupData (
    @SerializedName("_id")
    var Id  : String? ,
    @SerializedName("user_id" )
    var userId             : String?,
    @SerializedName("source")
    var source             : String?,
    @SerializedName("sleep_type")
    var sleepType          : String?,
    @SerializedName("current_requirement" )
    var currentRequirement : Double?  ,
    @SerializedName("sleep_datetime" )
    var sleepDatetime      : String?  ,
    @SerializedName("wakeup_datetime" )
    var wakeupDatetime     : String? ,
    @SerializedName("is_default")
    var isDefault          : Boolean?,
    @SerializedName("loggedAt" )
    var loggedAt           : String? ,
    @SerializedName("createdAt")
    var createdAt          : String? ,
    @SerializedName("updatedAt")
    var updatedAt          : String?
)