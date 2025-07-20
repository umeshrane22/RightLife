package com.jetsynthesys.rightlife.ai_package.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.jetsynthesys.rightlife.ui.breathwork.pojo.BreathingData
import java.io.Serializable

data class BreathingResponse(
    @SerializedName("success")
    var success    : Boolean?,
    @SerializedName("statusCode")
    var statusCode : Int?,
    @SerializedName("data")
    var data       : ArrayList<BreathingData> = arrayListOf()
)




/*
data class BreathingData(
    @SerializedName("_id")
    var Id               : String? ,
    @SerializedName("title")
    var title            : String?,
    @SerializedName("subTitle")
    var subTitle         : String?,
    @SerializedName("thumbnail")
    var thumbnail        : String?,
    @SerializedName("breathInhaleTime")
    var breathInhaleTime : String? ,
    @SerializedName("breathExhaleTime")
    var breathExhaleTime : String?,
    @SerializedName("breathHoldTime" )
    var breathHoldTime   : String? ,
    @SerializedName("createdAt")
    var createdAt        : String?,
    @SerializedName("updatedAt")
    var updatedAt        : String?,
    @SerializedName("__v")
    var _v               : Int? ,
    @SerializedName("duration")
    var duration         : Int?
)*/
