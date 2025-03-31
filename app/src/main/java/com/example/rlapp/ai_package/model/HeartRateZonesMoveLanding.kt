package com.example.rlapp.ai_package.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Parcelize
data class HeartRateZonesMoveLanding(
    @SerialName("Light Zone") val lightZone: List<Int>?,
    @SerialName("Fat Burn Zone") val fatBurnZone: List<Int>?,
    @SerialName("Cardio Zone") val cardioZone: List<Int>?,
    @SerialName("Peak Zone") val peakZone: List<Int>?
):Parcelable {
    fun toZoneRanges(): Map<String, IntRange> {
        return mapOf(
            "Light" to (lightZone?.let { it[0]..it[1] } ?: (0..0)),
            "Fat Burn" to (fatBurnZone?.let { it[0]..it[1] } ?: (0..0)),
            "Cardio" to (cardioZone?.let { it[0]..it[1] } ?: (0..0)),
            "Peak" to (peakZone?.let { it[0]..it[1] } ?: (0..0))
        )
    }
}