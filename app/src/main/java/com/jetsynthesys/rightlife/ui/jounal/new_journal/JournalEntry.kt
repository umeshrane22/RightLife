package com.jetsynthesys.rightlife.ui.jounal.new_journal

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class JournalEntry : Serializable {
    @SerializedName("_id")
    @Expose
    var id: String? = null

    @SerializedName("title")
    @Expose
    var title: String? = null

    @SerializedName("userId")
    @Expose
    var userId: String? = null

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
    var tags: ArrayList<String>? = null

    @SerializedName("createdAt")
    @Expose
    var createdAt: String? = null

    @SerializedName("updatedAt")
    @Expose
    var updatedAt: String? = null

    @SerializedName("__v")
    @Expose
    var v: Int? = null

    @SerializedName("question")
    @Expose
    var question: String? = null
}