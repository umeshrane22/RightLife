package com.jetsynthesys.rightlife.ui.jounal.new_journal

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class Section : Serializable {
    @SerializedName("sectionName")
    @Expose
    var sectionName: String? = null

    @SerializedName("_id")
    @Expose
    var id: String? = null
}