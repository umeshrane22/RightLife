package com.example.rlapp.apimodel.rlpagemodels.faceScanReportForId;

import java.util.List;

public  class HealthCamReportByCategory {
    private List<HealthCamItem> HEALTH_CAM_GOOD;
    private List<HealthCamItem> HEALTH_CAM_PAY_ATTENTION;

    public List<HealthCamItem> getHEALTH_CAM_GOOD() {
        return HEALTH_CAM_GOOD;
    }

    public List<HealthCamItem> getHEALTH_CAM_PAY_ATTENTION() {
        return HEALTH_CAM_PAY_ATTENTION;
    }

    public static class HealthCamItem {
        private String fieldName;
        private Double value;

        public String getFieldName() {
            return fieldName;
        }

        public Double getValue() {
            return value;
        }
    }
}
