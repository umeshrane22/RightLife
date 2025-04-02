package com.jetsynthesys.rightlife.apimodel;

import java.util.List;

public class LoginResponseMobile {
  /*  private boolean success;
    private int statusCode;
    private String message;
    private boolean isRegistered;
    private boolean isPasswordSet;

    // Getters to access response fields.
    public boolean isSuccess() {
        return success;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getMessage() {
        return message;
    }

    public boolean isRegistered() {
        return isRegistered;
    }

    public boolean isPasswordSet() {
        return isPasswordSet;
    }*/



        private boolean success;
        private int statusCode;
        private String accessToken;
        private String refreshToken;
        private String role;
        private boolean maxDeviceReached;
        private List<String> devices;

        // Getters and setters
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

        public String getAccessToken() {
            return accessToken;
        }

        public void setAccessToken(String accessToken) {
            this.accessToken = accessToken;
        }

        public String getRefreshToken() {
            return refreshToken;
        }

        public void setRefreshToken(String refreshToken) {
            this.refreshToken = refreshToken;
        }

        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }

        public boolean isMaxDeviceReached() {
            return maxDeviceReached;
        }

        public void setMaxDeviceReached(boolean maxDeviceReached) {
            this.maxDeviceReached = maxDeviceReached;
        }

        public List<String> getDevices() {
            return devices;
        }

        public void setDevices(List<String> devices) {
            this.devices = devices;
        }

}
