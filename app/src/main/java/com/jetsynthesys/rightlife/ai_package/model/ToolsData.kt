package com.jetsynthesys.rightlife.ai_package.model

import com.google.gson.annotations.SerializedName

data class ToolsData(
    @SerializedName("userId"       )
    var userId       : String?,
    @SerializedName("_id"       )
    var _id       : String?,
    @SerializedName("moduleId"         )
    var moduleId         : String?,
    @SerializedName("title"         )
    var title         : String?,
    @SerializedName("moduleType"       )
    var moduleType       : String?,
    @SerializedName("isSelectedModule" )
    var isSelectedModule : Boolean? ,
    @SerializedName("createdAt"        )
    var createdAt        : String?,
    @SerializedName("updatedAt"        )
    var updatedAt        : String?,
)
