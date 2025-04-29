package com.jetsynthesys.rightlife.ai_package.model

import com.google.gson.annotations.SerializedName

data class AffirmationPlaylistResponse(
    @SerializedName("success")
    var success    : Boolean?  ,
    @SerializedName("statusCode")
    var statusCode : Int?     ,
    @SerializedName("data")
    var data       : ArrayList<AffirmationPlaylistData> = arrayListOf()
)

data class AffirmationPlaylistData (
    @SerializedName("_id")
    var Id           : String? ,
    @SerializedName("title")
    var title        : String? ,
    @SerializedName("image")
    var image        : String? ,
    @SerializedName("textColor")
    var textColor    : String? ,
    @SerializedName("bgColor")
    var bgColor      : String? ,
    @SerializedName("artist" )
    var artist       : String? ,
    @SerializedName("catagoryId")
    var catagoryId   : String? ,
    @SerializedName("isActive")
    var isActive     : Boolean?,
    @SerializedName("createdAt")
    var createdAt    : String?,
    @SerializedName("updatedAt")
    var updatedAt    : String?,
    @SerializedName("__v")
    var _v           : Int? ,
    @SerializedName("categoryName")
    var categoryName : String?
)