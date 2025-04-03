package com.jetsynthesys.rightlife.apimodel.Episodes;

public class EpisodeResponseModel {
        private boolean success;
        private int statusCode;
        private DataModel data;

        // Getters and Setters
        public boolean isSuccess() {
            return success;
        }

        public void setSuccess(boolean success) {
            this.success = success;
        }

        public int getStatusCode() {
            return statusCode;
        }

        public void setStatusCode(int statusCode) {
            this.statusCode = statusCode;
        }

        public DataModel getData() {
            return data;
        }

        public void setData(DataModel data) {
            this.data = data;
        }
    }

