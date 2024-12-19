package com.example.rlapp.apimodel.emaillogin;

public class SubmitEmailOtpRequest {
    private String email;
    private String otp;
    private String deviceId;
    private String deviceName;
    private String deviceType;
    private String deviceToken;
    private String referralCode;




    public SubmitEmailOtpRequest(String email, String otp, String deviceId, String deviceName, String deviceType, String deviceToken,String referralCode) {

        this.email = email;
        this.otp = otp;
        this.deviceId = deviceId;
        this.deviceName = deviceName;
        this.deviceType = deviceType;
        this.deviceToken = deviceToken;

    }
}


