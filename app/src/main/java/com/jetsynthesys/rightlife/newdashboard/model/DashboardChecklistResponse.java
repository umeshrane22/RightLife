package com.jetsynthesys.rightlife.newdashboard.model;

public class DashboardChecklistResponse {
    private boolean success;
    private int statusCode;
    private Data data;

    public boolean isSuccess() { return success; }
    public int getStatusCode() { return statusCode; }
    public Data getData() { return data; }

    public static class Data {
         boolean checklistStatus;
         boolean facialScanStatus;
         boolean mindAuditStatus;
         boolean paymentStatus;

        public boolean isChecklistStatus() { return checklistStatus; }
        public boolean isFacialScanStatus() { return facialScanStatus; }
        public boolean isMindAuditStatus() { return mindAuditStatus; }
        public boolean isPaymentStatus() { return paymentStatus; }
    }
}
