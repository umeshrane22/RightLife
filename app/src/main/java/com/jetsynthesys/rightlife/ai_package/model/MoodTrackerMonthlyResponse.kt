package com.jetsynthesys.rightlife.ai_package.model

import com.google.gson.annotations.SerializedName

data class MoodTrackerMonthlyResponse(
    @SerializedName("success"    ) var success    : Boolean?,
    @SerializedName("statusCode" ) var statusCode : Int?,
    @SerializedName("data"       ) var data       : ArrayList<MoodTrackerMonthData> = arrayListOf()
)


data class MoodTrackerMonthData (
    @SerializedName("_id"        )
    var Id         : String?,
    @SerializedName("title"      )
    var title      : String?,
    @SerializedName("userId"     )
    var userId     : String?,
    @SerializedName("questionId" )
    var questionId : String?,
    @SerializedName("answer"     )
    var answer     : String?,
    @SerializedName("emotion"    )
    var emotion    : String?,
    @SerializedName("tags"       )
    var tags       : ArrayList<String> = arrayListOf(),
    @SerializedName("createdAt"  )
    var createdAt  : String?,
    @SerializedName("updatedAt"  )
    var updatedAt  : String?,
    @SerializedName("__v"        )
    var _v         : Int?,
    @SerializedName("question"   )
    var question   : String?
)