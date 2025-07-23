package com.jetsynthesys.rightlife.ai_package.model

import com.google.gson.annotations.SerializedName

data class AddWorkoutResponse(
    @SerializedName("status_code")
    var statusCode : Int?     ,
    @SerializedName("message")
    var message    : String?  ,
    @SerializedName("workouts")
    var workouts   : ArrayList<Workouts> = arrayListOf()
)

data class Workouts (
    @SerializedName("user_id")
    var userId         : String? ,
    @SerializedName("date")
    var date           : String?,
    @SerializedName("activityId")
    var activityId     : String? ,
    @SerializedName("intensity" )
    var intensity      : String? ,
    @SerializedName("duration_min")
    var durationMin    : Int?  ,
    @SerializedName("calories_burned")
    var caloriesBurned : Double?,
    @SerializedName("routine_id")
    var routineId      : String?

)