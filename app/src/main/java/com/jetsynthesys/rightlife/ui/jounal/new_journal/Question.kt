package com.jetsynthesys.rightlife.ui.jounal.new_journal

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class Question : Serializable {
    @SerializedName("_id")
    @Expose
    var id: String? = null

    @SerializedName("question")
    @Expose
    var question: String? = null
}