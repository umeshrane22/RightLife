package com.jetsynthesys.rightlife.ai_package.model

import com.google.gson.annotations.SerializedName

data class BaseResponse(
    @SerializedName("success"    )
    var success    : Boolean? ,
    @SerializedName("statusCode" )
    var statusCode : Int?,
    @SerializedName("successMessage" )
    var successMessage : String?,
)

