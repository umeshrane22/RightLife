package com.jetsynthesys.rightlife.apimodel.newquestionrequestfacescan

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class FaceScanQuestionRequest {
    @SerializedName("questionId")
    @Expose
    var questionId: String? = null

    @SerializedName("answers")
    @Expose
    var answers: ArrayList<AnswerFaceScan>? = ArrayList()
}