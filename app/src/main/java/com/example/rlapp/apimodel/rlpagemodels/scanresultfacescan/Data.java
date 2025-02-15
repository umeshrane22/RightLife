package com.example.rlapp.apimodel.rlpagemodels.scanresultfacescan;


    public class Data {
        private Service service;
        private String _id;

        public Report getReport() {
            return report;
        }

        public void setReport(Report report) {
            this.report = report;
        }

        public int get__v() {
            return __v;
        }

        public void set__v(int __v) {
            this.__v = __v;
        }

        public String getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(String updatedAt) {
            this.updatedAt = updatedAt;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getAnswerId() {
            return answerId;
        }

        public void setAnswerId(String answerId) {
            this.answerId = answerId;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String get_id() {
            return _id;
        }

        public void set_id(String _id) {
            this._id = _id;
        }

        public Service getService() {
            return service;
        }

        public void setService(Service service) {
            this.service = service;
        }

        private String userId;
        private String answerId;
        private String status;
        private String createdAt;
        private String updatedAt;
        private int __v;
        private Report report;
}
