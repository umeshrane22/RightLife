package com.jetsynthesys.rightlife.newdashboard.model

object DashboardChecklistManager {
    var checklistStatus: Boolean = false
    var facialScanStatus: Boolean = false
    var mindAuditStatus: Boolean = false
    var isDataLoaded: Boolean = false

    fun updateFrom(response: DashboardChecklistResponse.Data) {
        checklistStatus = response.checklistStatus
        facialScanStatus = response.facialScanStatus
        mindAuditStatus = response.mindAuditStatus
        isDataLoaded = true
    }
}
