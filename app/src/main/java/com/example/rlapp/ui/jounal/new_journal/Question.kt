package com.example.rlapp.ui.jounal.new_journal

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Question {
    @SerializedName("_id")
    @Expose
    var id: String? = null

    @SerializedName("question")
    @Expose
    var question: String? = null
}