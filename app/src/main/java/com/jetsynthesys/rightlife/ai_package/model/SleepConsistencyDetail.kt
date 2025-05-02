package com.jetsynthesys.rightlife.ai_package.model

import com.google.gson.annotations.SerializedName

data class SleepConsistencyDetail(
    @SerializedName("consistency_message")
    var consistencyMessage : String?,
    @SerializedName("action_step")
    var actionStep         : String?
)
