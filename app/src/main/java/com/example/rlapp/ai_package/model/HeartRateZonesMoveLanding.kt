package com.example.rlapp.ai_package.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class HeartRateZonesMoveLanding(
    @SerialName("Light Zone") val lightZone: List<Int>,
    @SerialName("Fat Burn Zone") val fatBurnZone: List<Int>,
    @SerialName("Cardio Zone") val cardioZone: List<Int>,
    @SerialName("Peak Zone") val peakZone: List<Int>
) {
    // Helper function to convert to a Map<String, IntRange> for use in calculateHeartRateZonePercentages
    fun toZoneRanges(): Map<String, IntRange> {
        return mapOf(
            "Light" to (lightZone[0]..lightZone[1]),
            "Fat Burn" to (fatBurnZone[0]..fatBurnZone[1]),
            "Cardio" to (cardioZone[0]..cardioZone[1]),
            "Peak" to (peakZone[0]..peakZone[1])
        )
    }
}