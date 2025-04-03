package com.jetsynthesys.rightlife.ai_package.ui.thinkright

import java.time.LocalDate

// Enum to represent the severity levels
enum class SeverityLevel(val range: IntRange, val colorResId: Int) {
    MINIMAL(0..4, android.R.color.holo_green_dark),    // Green
    MILD(5..9, android.R.color.holo_green_light),      // Light Green
    MODERATE(10..14, android.R.color.holo_orange_light), // Yellow
    SEVERE(15..19, android.R.color.holo_orange_dark),   // Orange
    EXTREMELY_SEVERE(20..27, android.R.color.holo_red_dark) // Red
}

// Data class to hold PHQ-9 assessment data
data class Phq9Assessment(
    val score: Int,
    val dateTaken: LocalDate,
    val nextScanDate: LocalDate
) {
    // Determine the severity level based on the score
    val severityLevel: SeverityLevel
        get() = when (score) {
            in SeverityLevel.MINIMAL.range -> SeverityLevel.MINIMAL
            in SeverityLevel.MILD.range -> SeverityLevel.MILD
            in SeverityLevel.MODERATE.range -> SeverityLevel.MODERATE
            in SeverityLevel.SEVERE.range -> SeverityLevel.SEVERE
            in SeverityLevel.EXTREMELY_SEVERE.range -> SeverityLevel.EXTREMELY_SEVERE
            else -> SeverityLevel.MINIMAL // Default case for invalid scores
        }

    // Calculate days until the next scan
    val daysUntilNextScan: Long
        get() = java.time.temporal.ChronoUnit.DAYS.between(LocalDate.now(), nextScanDate)
}