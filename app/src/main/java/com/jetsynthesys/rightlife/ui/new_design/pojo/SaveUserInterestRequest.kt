package com.jetsynthesys.rightlife.ui.new_design.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class SaveUserInterestRequest {
    @SerializedName("intrestId")
    @Expose
    var intrestId: List<String>? = null
}