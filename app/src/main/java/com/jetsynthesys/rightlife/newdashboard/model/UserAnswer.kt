package com.jetsynthesys.rightlife.newdashboard.model

import com.google.gson.annotations.SerializedName

data class UserAnswer(
    @SerializedName("question") val question: String?,
    @SerializedName("answer") val answer: String?
)

