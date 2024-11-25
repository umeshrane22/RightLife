package com.example.rlapp.apimodel;

public class SubmitLoginOtpRequest {

    private String mobileNumber;
    private String otp;
    private String deviceId;
    private String deviceName;
    private String deviceType;
    private String deviceToken;




    public SubmitLoginOtpRequest(String phoneNumber, String otp, String deviceId, String deviceName, String deviceType, String deviceToken) {

        this.mobileNumber = phoneNumber;
        this.otp = otp;
        this.deviceId = deviceId;
        this.deviceName = deviceName;
        this.deviceType = deviceType;
        this.deviceToken = deviceToken;

    }
}
