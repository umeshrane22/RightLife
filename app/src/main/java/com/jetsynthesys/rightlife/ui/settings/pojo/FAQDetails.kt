package com.jetsynthesys.rightlife.ui.settings.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class FAQDetails: Serializable {
    @SerializedName("question")
    @Expose
    var question: String? = null

    @SerializedName("answer")
    @Expose
    var answer: String? = null

    @SerializedName("_id")
    @Expose
    var id: String? = null
}