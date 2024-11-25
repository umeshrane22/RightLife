package com.example.rlapp.apimodel.liveevents;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Attendance {

        @SerializedName("attendanceCount")
        @Expose
        private String attendanceCount;
        @SerializedName("attendeeText")
        @Expose
        private String attendeeText;

        public String getAttendanceCount() {
            return attendanceCount;
        }

        public void setAttendanceCount(String attendanceCount) {
            this.attendanceCount = attendanceCount;
        }

        public String getAttendeeText() {
            return attendeeText;
        }

        public void setAttendeeText(String attendeeText) {
            this.attendeeText = attendeeText;
        }

    }
