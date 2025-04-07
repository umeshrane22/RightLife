package com.jetsynthesys.rightlife.ai_package.model

import com.google.gson.annotations.SerializedName

data class ToolGridData(

    @SerializedName("moduleName")
    var moduleName       : String?,
    @SerializedName("subtitle")
    var subtitle         : String?,
    @SerializedName("moduleType")
    var moduleType       : String?,
    @SerializedName("isSelectedModule")
    var isSelectedModule : Boolean? ,
    @SerializedName("createdAt")
    var createdAt        : String?,
    @SerializedName("updatedAt")
    var updatedAt        : String?
)
