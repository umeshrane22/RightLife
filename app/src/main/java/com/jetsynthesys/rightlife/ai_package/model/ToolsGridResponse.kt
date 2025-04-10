package com.jetsynthesys.rightlife.ai_package.model

import com.google.gson.annotations.SerializedName

data class ToolsGridResponse(
    @SerializedName("success"    )
    var success    : Boolean? ,
    @SerializedName("statusCode" )
    var statusCode : Int?,
    @SerializedName("data"       )
    var data       : ArrayList<ToolGridData> = arrayListOf()
)
