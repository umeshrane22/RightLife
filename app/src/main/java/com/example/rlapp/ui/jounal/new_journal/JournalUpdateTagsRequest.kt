package com.example.rlapp.ui.jounal.new_journal

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class JournalUpdateTagsRequest {
    @SerializedName("tag")
    @Expose
    var tag: String? = null

    @SerializedName("outerIndex")
    @Expose
    var outerIndex: Int? = null

    @SerializedName("innerIndex")
    @Expose
    var innerIndex: Int? = null
}