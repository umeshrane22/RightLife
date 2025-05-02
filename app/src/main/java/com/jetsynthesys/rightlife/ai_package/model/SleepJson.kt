package com.jetsynthesys.rightlife.ai_package.model

import com.google.gson.annotations.SerializedName

data class SleepJson (
    @SerializedName("DataSource" )
    var dataSource : String? ,
    @SerializedName("DataPoints" )
    var dataPoints : ArrayList<DataPoints> =     arrayListOf()
)

data class SleepValue (
    @SerializedName("intVal" )
    var intVal : Long?
)

data class FitValue (
    @SerializedName("value" )
    var value : SleepValue?
)

data class DataPoints(
    @SerializedName("fitValue")
    var fitValue           : ArrayList<FitValue> = arrayListOf(),
    @SerializedName("originDataSourceId")
    var originDataSourceId : String?,
    @SerializedName("endTimeNanos")
    var endTimeNanos       : Long? ,
    @SerializedName("dataTypeName")
    var dataTypeName       : String?,
    @SerializedName("startTimeNanos")
    var startTimeNanos     : Long?,
    @SerializedName("modifiedTimeMillis" )
    var modifiedTimeMillis : Long?,
    @SerializedName("rawTimestampNanos"  )
    var rawTimestampNanos  : Long?
)
