package com.jetsynthesys.rightlife.ui.mindaudit

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class Assessments : Serializable {
    @JvmField
    @SerializedName("suggestedAssessments")
    @Expose
    var suggestedAssessments: SuggestedAssessments? = null

    @JvmField
    @SerializedName("allAssessment")
    @Expose
    var allAssessment: AllAssessment? = null

    @SerializedName("savedAssessment")
    @Expose
    var savedAssessment: ArrayList<SavedAssessment> = ArrayList()
}
