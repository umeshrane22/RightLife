package com.jetsynthesys.rightlife.ui.jounal.new_journal

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class JournalTagsResponse {
    @SerializedName("success")
    @Expose
    var success: Boolean? = null

    @SerializedName("statusCode")
    @Expose
    var statusCode: Int? = null

    @SerializedName("data")
    @Expose
    var data: JournalTagData? = null
}