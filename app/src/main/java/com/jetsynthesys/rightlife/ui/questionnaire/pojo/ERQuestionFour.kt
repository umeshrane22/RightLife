package com.jetsynthesys.rightlife.ui.questionnaire.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ERQuestionFour {
    @SerializedName("module")
    @Expose
    var module: String? = "EAT_RIGHT"

    @SerializedName("answer")
    @Expose
    var answer: AnswerFruitVegetable? = null
}