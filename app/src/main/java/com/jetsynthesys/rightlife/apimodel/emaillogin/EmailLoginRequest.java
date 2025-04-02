package com.jetsynthesys.rightlife.apimodel.emaillogin;

public class EmailLoginRequest {
    private String emailId;
    private String password;
    private String deviceId;
    private String deviceName;
    private String deviceToken;
    private String deviceType;

    public EmailLoginRequest(String emailId, String password, String deviceId, String deviceName, String deviceToken, String deviceType) {
        this.emailId = emailId;
        this.password = password;
        this.deviceId = deviceId;
        this.deviceName = deviceName;
        this.deviceToken = deviceToken;
        this.deviceType = deviceType;
    }

    // Getters and setters (if needed)
}