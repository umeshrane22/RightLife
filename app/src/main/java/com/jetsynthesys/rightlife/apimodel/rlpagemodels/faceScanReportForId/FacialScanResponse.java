package com.jetsynthesys.rightlife.apimodel.rlpagemodels.faceScanReportForId;

import java.util.List;

public class FacialScanResponse {

    private boolean success;
        private int statusCode;
        private String successMessage;
        private FacialReportData data;
        private int count;
        private HealthCamReportByCategory healthCamReportByCategory;
        private UserAnswers userAnswers;
        private List<ScoreComponent> scoreComponents;
        private List<Recommendation> recommendation;


        // Getters for all fields (Example)
        public boolean isSuccess() {
            return success;
        }

        public int getStatusCode() {
            return statusCode;
        }

        public String getSuccessMessage() {
            return successMessage;
        }

        public FacialReportData getData() {
            return data;
        }

        public int getCount() {
            return count;
        }

        public HealthCamReportByCategory getHealthCamReportByCategory() {
            return healthCamReportByCategory;
        }

        public UserAnswers getUserAnswers() {
            return userAnswers;
        }

        public List<ScoreComponent> getScoreComponents() {
            return scoreComponents;
        }

        public List<Recommendation> getRecommendation() {
            return recommendation;
        }
    }
