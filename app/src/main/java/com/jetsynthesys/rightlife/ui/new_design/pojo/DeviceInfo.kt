package com.jetsynthesys.rightlife.ui.new_design.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class DeviceInfo {

    @SerializedName("deviceId")
    @Expose
    var deviceId: String? = null

    @SerializedName("deviceName")
    @Expose
    var deviceName: String? = null

    @SerializedName("lastLoginAt")
    @Expose
    var lastLoginAt: Long? = null
}
