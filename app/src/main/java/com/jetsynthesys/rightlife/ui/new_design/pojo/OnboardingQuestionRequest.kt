package com.jetsynthesys.rightlife.ui.new_design.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class OnboardingQuestionRequest {
    @SerializedName("gender")
    @Expose
    var gender: String? = null

    @SerializedName("age")
    @Expose
    var age: Int? = null

    @SerializedName("height")
    @Expose
    var height: String? = null

    @SerializedName("weight")
    @Expose
    var weight: String? = null

    @SerializedName("body_fat")
    @Expose
    var bodyFat: String? = null

    @SerializedName("target_weight")
    @Expose
    var targetWeight: String? = null

    @SerializedName("experience_stress_mindful_management")
    @Expose
    var experienceStressMindfulManagement: String? = null

    @SerializedName("daily_goal_achieve_time")
    @Expose
    var dailyGoalAchieveTime: String? = null
}