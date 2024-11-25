package com.example.rlapp.apimodel.liveevents;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class LiveEventResponse {

        @SerializedName("success")
        @Expose
        private Boolean success;
        @SerializedName("statusCode")
        @Expose
        private Integer statusCode;
        @SerializedName("data")
        @Expose
        private List<LiveEventList> data;

        public Boolean getSuccess() {
            return success;
        }

        public void setSuccess(Boolean success) {
            this.success = success;
        }

        public Integer getStatusCode() {
            return statusCode;
        }

        public void setStatusCode(Integer statusCode) {
            this.statusCode = statusCode;
        }

        public List<LiveEventList> getData() {
            return data;
        }

        public void setData(List<LiveEventList> data) {
            this.data = data;
        }

    }
