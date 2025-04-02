package com.jetsynthesys.rightlife.ai_package.model

import com.google.gson.annotations.SerializedName

data class SleepIdealActualResponse(

    @SerializedName("message" ) var message : String? ,
    @SerializedName("data"    ) var data    : SleepData?
)
