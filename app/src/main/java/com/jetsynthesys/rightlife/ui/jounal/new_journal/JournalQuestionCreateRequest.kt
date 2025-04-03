package com.jetsynthesys.rightlife.ui.jounal.new_journal

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class JournalQuestionCreateRequest {
    @SerializedName("title")
    @Expose
    var title: String? = null

    @SerializedName("questionId")
    @Expose
    var questionId: String? = null

    @SerializedName("answer")
    @Expose
    var answer: String? = null

    @SerializedName("emotion")
    @Expose
    var emotion: String? = null

    @SerializedName("tags")
    @Expose
    var tags: ArrayList<String>? = ArrayList()
}