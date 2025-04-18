package com.jetsynthesys.rightlife.ai_package.model

import com.google.gson.annotations.SerializedName

data class MoodTrackerWeeklyResponse(
    @SerializedName("success"    ) var success    : Boolean?        = null,
    @SerializedName("statusCode" ) var statusCode : Int?            = null,
    @SerializedName("data"       ) var data       : ArrayList<MoodTrackerPercent> = arrayListOf()
)

data class MoodTrackerPercent(
    @SerializedName("happy")
    var happy    : Double?,
    @SerializedName("relaxed")
    var relaxed  : Double?,
    @SerializedName("unsure")
    var unsure   : Double?,
    @SerializedName("stressed")
    var stressed : Double?,
    @SerializedName("sad")
    var sad      : Double?
)