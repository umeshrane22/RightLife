package com.example.rlapp.newdashboard.model

import com.google.gson.annotations.SerializedName

data class UpdatedModule(
    @SerializedName("moduleId") val moduleId: String?,
    @SerializedName("title") val title: String?,
    @SerializedName("calorieBalance") val calorieBalance: Int?,
    @SerializedName("intake") val intake: Int?,
    @SerializedName("burned") val burned: Int?,
    @SerializedName("mindfulTime") val mindfulTime: String?,
    @SerializedName("calories") val calories: String?,
    @SerializedName("sleepDuration") val sleepDuration: String?,
    @SerializedName("isSelectedModule") val isSelectedModule: Boolean?,
    @SerializedName("mindfulnessMinutes") val mindfulnessMinutes: String?,
    @SerializedName("wellnessDays") val wellnessDays: String?,
    @SerializedName("activeBurn") val activeBurn: String?,
    @SerializedName("protein") val protein: String?,
    @SerializedName("carbs") val carbs: String?,
    @SerializedName("fats") val fats: String?,
    @SerializedName("sleepTime") val sleepTime: String?,
    @SerializedName("wakeUpTime") val wakeUpTime: String?,
    @SerializedName("rem") val rem: String?,
    @SerializedName("core") val core: String?,
    @SerializedName("deep") val deep: String?,
    @SerializedName("awake") val awake: String?
)
