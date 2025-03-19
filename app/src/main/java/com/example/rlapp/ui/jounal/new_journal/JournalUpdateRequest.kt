package com.example.rlapp.ui.jounal.new_journal

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class JournalUpdateRequest{
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