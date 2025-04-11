package com.jetsynthesys.rightlife.apimodel.newquestionrequestfacescan

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class AnswerFaceScan {
    @SerializedName("question")
    @Expose
    var question: String? = null

    @SerializedName("answer")
    @Expose
    var answer: Any? = null
}