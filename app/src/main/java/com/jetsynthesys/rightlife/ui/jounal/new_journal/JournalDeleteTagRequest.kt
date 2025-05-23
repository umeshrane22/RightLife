package com.jetsynthesys.rightlife.ui.jounal.new_journal

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class JournalDeleteTagRequest {
    @SerializedName("outerIndex")
    @Expose
    var outerIndex: Int? = null

    @SerializedName("innerIndex")
    @Expose
    var innerIndex: Int? = null
}