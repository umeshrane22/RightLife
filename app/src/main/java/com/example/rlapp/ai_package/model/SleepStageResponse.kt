package com.example.rlapp.ai_package.model

import com.google.gson.annotations.SerializedName

data class SleepStageResponse(
    @SerializedName("message")
    val message: String,

    @SerializedName("date")
    val date: String,

    @SerializedName("sleep_stage_data")
    val sleepStageData:  ArrayList<SleepStageData> = arrayListOf(),

    @SerializedName("sleep_summary")
    val sleepSummary: SleepSummary?,

    @SerializedName("sleep_percentages")
    val sleepPercentages:  SleepPercentages?
)