package com.example.rlapp.apimodel.userdata;


import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import javax.annotation.processing.Generated;

@Generated("jsonschema2pojo")
    public class NotificationConfiguration {

        @SerializedName("MARKETING")
        @Expose
        private List<String> marketing;
        @SerializedName("REMAINDER")
        @Expose
        private List<String> remainder;
        @SerializedName("TRANSACTIONAL")
        @Expose
        private List<String> transactional;

        public List<String> getMarketing() {
            return marketing;
        }

        public void setMarketing(List<String> marketing) {
            this.marketing = marketing;
        }

        public List<String> getRemainder() {
            return remainder;
        }

        public void setRemainder(List<String> remainder) {
            this.remainder = remainder;
        }

        public List<String> getTransactional() {
            return transactional;
        }

        public void setTransactional(List<String> transactional) {
            this.transactional = transactional;
        }

    }
