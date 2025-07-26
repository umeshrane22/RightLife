package com.jetsynthesys.rightlife.ui.healthcam

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ScoreComponent {
    @SerializedName("categoryName")
    @Expose
    var categoryName: String? = null

    @SerializedName("displayName")
    @Expose
    var displayName: String? = null

    @SerializedName("score")
    @Expose
    var score: Any? = null
}