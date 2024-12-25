package com.example.rlapp.apimodel;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class SetPasswordRequest {

    @SerializedName("password")
    @Expose
    private String password;

    public SetPasswordRequest(String password) {
        this.password = password;
    }

 /*   public String getPassword() {
        return password;
    }*/

  /*  public void setPassword(String password) {
        this.password = password;
    }*/

}