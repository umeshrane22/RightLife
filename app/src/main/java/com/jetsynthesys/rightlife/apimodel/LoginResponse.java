package com.jetsynthesys.rightlife.apimodel;

public class LoginResponse {
    private boolean success;
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
    }
}
