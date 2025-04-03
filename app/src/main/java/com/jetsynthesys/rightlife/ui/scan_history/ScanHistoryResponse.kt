package com.jetsynthesys.rightlife.ui.scan_history

data class ScanHistoryResponse(
    val success: Boolean,
    val statusCode: Int,
    val data: List<ReportItem>
)