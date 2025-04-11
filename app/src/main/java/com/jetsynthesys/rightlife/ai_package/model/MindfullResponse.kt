package com.jetsynthesys.rightlife.ai_package.model

import com.google.gson.annotations.SerializedName

data class MindfullResponse(
    @SerializedName("success"    ) var success    : Boolean?        = null,
    @SerializedName("statusCode" ) var statusCode : Int?            = null,
    @SerializedName("data"       ) var data       : ArrayList<MindfullData> = arrayListOf()

)

data class MindfullData (
    @SerializedName("date")
    var date     : String?,
    @SerializedName("day")
    var day      : String? ,
    @SerializedName("duration")
    var duration : Int?
)