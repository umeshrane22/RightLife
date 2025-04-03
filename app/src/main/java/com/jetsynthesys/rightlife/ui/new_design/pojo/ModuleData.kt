package com.jetsynthesys.rightlife.ui.new_design.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ModuleData {
    @SerializedName("sectionTitle")
    @Expose
    var sectionTitle: String? = null

    @SerializedName("sectionSubtitle")
    @Expose
    var sectionSubtitle: String? = null

    @SerializedName("isShowGrid")
    @Expose
    var isShowGrid: Boolean? = null

    @SerializedName("services")
    @Expose
    var services: List<ModuleService>? = null
}