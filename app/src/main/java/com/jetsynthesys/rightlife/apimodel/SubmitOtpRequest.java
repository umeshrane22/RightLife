package com.jetsynthesys.rightlife.apimodel;

public class SubmitOtpRequest {

    private String phoneNumber;
    private String otp;
    private String deviceId;
    private String deviceName;
    private String deviceType;
    private String deviceToken;




    public SubmitOtpRequest(String phoneNumber,String otp,String deviceId,String deviceName,String deviceType,String deviceToken) {

        this.phoneNumber = phoneNumber;
        this.otp = otp;
        this.deviceId = deviceId;
        this.deviceName = deviceName;
        this.deviceType = deviceType;
        this.deviceToken = deviceToken;

    }
}
