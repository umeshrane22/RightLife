package com.jetsynthesys.rightlife.ai_package.model

import com.google.gson.annotations.SerializedName

data class MindfullResponse(
    @SerializedName("success" )
    var success    : Boolean?  ,
    @SerializedName("statusCode" )
    var statusCode : Int?  ,
    @SerializedName("data" )
    var data       : MindfullData?

)

data class MindfullData (
    @SerializedName("formattedData"    )
    var formattedData    : ArrayList<FormattedData> = arrayListOf(),
    @SerializedName("journalCount")
    var journalCount     : Int? ,
    @SerializedName("affirmationCount")
    var affirmationCount : Int? ,
    @SerializedName("title" )
    var title            : String? ,
    @SerializedName("description")
    var description      : String?
)

data class FormattedData (
    @SerializedName("date"     )
    var date     : String?,
    @SerializedName("day"      )
    var day      : String? ,
    @SerializedName("duration" )
    var duration : Int?
)