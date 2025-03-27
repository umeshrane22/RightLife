package com.example.rlapp.ui.questionnaire.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class QuestionnaireAnswerRequest {
    @SerializedName("move_right")
    @Expose
    var moveRight: MoveRight? = null

    @SerializedName("eat_right")
    @Expose
    var eatRight: EatRight? = null

    @SerializedName("sleep_right")
    @Expose
    var sleepRight: SleepRight? = null

    @SerializedName("think_right")
    @Expose
    var thinkRight: ThinkRight? = null
}