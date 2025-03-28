package com.example.rlapp.ai_package.model

import com.google.gson.annotations.SerializedName

data class SleepData(
    @SerializedName("user_id"           )
    var userId          : String? ,
    @SerializedName("source"            )
    var source          : String? ,
    @SerializedName("starttime"         )
    var starttime       : String? ,
    @SerializedName("endtime"           )
    var endtime         : String? ,
    @SerializedName("sleep_time_detail" )
    var sleepTimeDetail : ArrayList<SleepTimeDetail> = arrayListOf()
)
