package com.jetsynthesys.rightlife.ai_package.model

import com.google.gson.annotations.SerializedName

data class ToolsData(
    @SerializedName("userId"       )
    var userId       : String?,
    @SerializedName("_id"       )
    var toolsId       : String?,
    @SerializedName("categoryId"       )
    var categoryId       : String?,
    @SerializedName("moduleId"         )
    var moduleId         : String?,
    @SerializedName("moduleName"       )
    var moduleName       : String?,
    @SerializedName("subtitle"         )
    var subtitle         : String?,
    @SerializedName("moduleType"       )
    var moduleType       : String?,
    @SerializedName("isSelectedModule" )
    var isSelectedModule : Boolean? ,
    @SerializedName("createdAt"        )
    var createdAt        : String?,
    @SerializedName("updatedAt"        )
    var updatedAt        : String?,
)
