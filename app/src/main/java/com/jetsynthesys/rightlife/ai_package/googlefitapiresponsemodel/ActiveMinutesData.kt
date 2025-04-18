//package com.jetsynthesys.rightlife.ai_package.googlefitapiresponsemodel
//
//import com.google.gson.annotations.SerializedName
//
//data class ActiveMinutesData(
//    @SerializedName("Data Source") val dataSource: String,
//    @SerializedName("Data Points") val dataPoints: List<DataPoint>
//)
//
//data class DataPoint(
//    val fitValue: List<FitValue>,
//    val originDataSourceId: String,
//    val endTimeNanos: Long,
//    val dataTypeName: String,
//    val startTimeNanos: Long,
//    val modifiedTimeMillis: Long,
//    val rawTimestampNanos: Long
//)
//
//data class FitValue(
//    val value: Value
//)
//
//data class Value(
//    val intVal: Int
//)
