package com.jetsynthesys.rightlife.ai_package.model.response

import com.google.gson.annotations.SerializedName

data class SleepSoundResponse(
    @SerializedName("success")
    var success    : Boolean?,
    @SerializedName("statusCode")
    var statusCode : Int? ,
    @SerializedName("data")
    var sleepSoundData     : SleepSoundData?
)

data class Tags (
    @SerializedName("name")
    var name : String?,
    @SerializedName("_id")
    var Id   : String?
)

data class Meta (
    @SerializedName("duration")
    var duration  : Int?,
    @SerializedName("size")
    var size      : String?,
    @SerializedName("sizeBytes")
    var sizeBytes : Int?
)

data class Services (
    @SerializedName("_id")
    var Id         : String? ,
    @SerializedName("catagoryId")
    var catagoryId : String? ,
    @SerializedName("title")
    var title      : String? ,
    @SerializedName("url" )
    var url        : String? ,
    @SerializedName("desc" )
    var desc       : String? ,
    @SerializedName("image" )
    var image      : String? ,
    @SerializedName("tags" )
    var tags       : ArrayList<Tags> = arrayListOf(),
    @SerializedName("meta")
    var meta       : Meta?,
    @SerializedName("isActive")
    var isActive   : Boolean?
)

data class SleepSoundData (
    @SerializedName("title" )
    var title    : String? ,
    @SerializedName("services")
    var services : ArrayList<Services> = arrayListOf()
)