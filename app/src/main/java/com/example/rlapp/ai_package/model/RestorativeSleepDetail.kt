package com.example.rlapp.ai_package.model

import com.google.gson.annotations.SerializedName

data class RestorativeSleepDetail(

    @SerializedName("total_sleep_duration_minutes" )
    var totalSleepDurationMinutes  : Int?,
    @SerializedName("sleep_start_time")
    var sleepStartTime             : String?,
    @SerializedName("sleep_end_time")
    var sleepEndTime               : String?,
   // @SerializedName("sleep_stages" )
    //var sleepStages                : ArrayList<SleepStages>                = arrayListOf(),
  //  @SerializedName("calculated_restorative_sleep" )
 //   var calculatedRestorativeSleep : ArrayList<CalculatedRestorativeSleep> = arrayListOf()
)
