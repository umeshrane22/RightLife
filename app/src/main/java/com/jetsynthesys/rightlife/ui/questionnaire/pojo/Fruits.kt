package com.jetsynthesys.rightlife.ui.questionnaire.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Fruits {
    @SerializedName("servings")
    @Expose
    var servings: String? = null
}