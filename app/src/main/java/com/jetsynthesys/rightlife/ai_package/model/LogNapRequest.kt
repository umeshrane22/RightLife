package com.jetsynthesys.rightlife.ai_package.model

data class LogNapRequest(
    var sleep_time : String?,
    var wakeup_time : String?,
    var set_reminder : Int?,
    var reminder_value : String?
)