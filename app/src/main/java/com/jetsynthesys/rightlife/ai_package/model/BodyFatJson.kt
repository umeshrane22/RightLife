package com.jetsynthesys.rightlife.ai_package.model

import com.google.gson.annotations.SerializedName

data class BodyFatJson(
    @SerializedName("DataSource" )
    var dataSource : String?   ,
    @SerializedName("DataPoints" )
    var dataFatPoints : ArrayList<DataFatPoints> = arrayListOf()
)

data class DataFatPoints (
    @SerializedName("fitValue")
    var fitValue           : ArrayList<FitFatValue> = arrayListOf(),
    @SerializedName("originDataSourceId")
    var originDataSourceId : String?  ,
    @SerializedName("endTimeNanos")
    var endTimeNanos       : Long?    ,
    @SerializedName("dataTypeName")
    var dataTypeName       : String? ,
    @SerializedName("startTimeNanos")
    var startTimeNanos     : Long?  ,
    @SerializedName("modifiedTimeMillis")
    var modifiedTimeMillis : Long? ,
    @SerializedName("rawTimestampNanos" )
    var rawTimestampNanos  : Long?
)

data class FitFatValue (
    @SerializedName("value" )
    var value : FatValue?
)

data class FatValue (
    @SerializedName("fpVal" )
    var fpVal : Double?
)