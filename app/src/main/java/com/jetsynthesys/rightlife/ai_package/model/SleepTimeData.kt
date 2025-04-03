package com.jetsynthesys.rightlife.ai_package.model

import com.google.gson.annotations.SerializedName

data class SleepTimeData(
    @SerializedName("ideal_sleep_data"  ) var idealSleepData  : Double? = null,
    @SerializedName("actual_sleep_data" ) var actualSleepData : Double? = null
)
