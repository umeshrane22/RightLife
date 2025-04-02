package com.jetsynthesys.rightlife.apimodel;

public class LoginRequest {
    private String email;
    private String loginType;
    private String phoneNumber;

    public LoginRequest(String email, String loginType, String phoneNumber) {
        this.email = email;
        this.loginType = loginType;
        this.phoneNumber = phoneNumber;
    }
}
