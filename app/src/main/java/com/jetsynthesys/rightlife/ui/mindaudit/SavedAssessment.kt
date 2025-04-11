package com.jetsynthesys.rightlife.ui.mindaudit

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class SavedAssessment: Serializable{
    @SerializedName("assessmentType")
    @Expose
    val assessmentType: String = ""

    @SerializedName("isCompleted")
    @Expose
    val isCompleted: Boolean = false
}