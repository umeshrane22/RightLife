package com.jetsynthesys.rightlife.ui.questionnaire.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class MRQuestionOne {
    @SerializedName("module")
    @Expose
    var module: String? = null

    @SerializedName("answer")
    @Expose
    var answer: String? = null
}