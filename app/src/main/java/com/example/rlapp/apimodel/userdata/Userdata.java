package com.example.rlapp.apimodel.userdata;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import javax.annotation.processing.Generated;

@Generated("jsonschema2pojo")
public class Userdata {

    @SerializedName("gender")
    @Expose
    private String gender;
    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("phoneNumber")
    @Expose
    private String phoneNumber;
    @SerializedName("weightUnit")
    @Expose
    private String weightUnit;
    @SerializedName("heightUnit")
    @Expose
    private String heightUnit;
    @SerializedName("newEmailStatus")
    @Expose
    private String newEmailStatus;
    @SerializedName("notificationConfiguration")
    @Expose
    private NotificationConfiguration notificationConfiguration;
    @SerializedName("loginType")
    @Expose
    private String loginType;
    @SerializedName("syncGoogleCalendar")
    @Expose
    private Boolean syncGoogleCalendar;
    @SerializedName("profilePictures")
    @Expose
    private List<Object> profilePictures;
    @SerializedName("address")
    @Expose
    private List<Object> address;
    @SerializedName("country")
    @Expose
    private String country;
    @SerializedName("createdAt")
    @Expose
    private Long createdAt;
    @SerializedName("updatedAt")
    @Expose
    private Long updatedAt;

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getWeightUnit() {
        return weightUnit;
    }

    public void setWeightUnit(String weightUnit) {
        this.weightUnit = weightUnit;
    }

    public String getHeightUnit() {
        return heightUnit;
    }

    public void setHeightUnit(String heightUnit) {
        this.heightUnit = heightUnit;
    }

    public String getNewEmailStatus() {
        return newEmailStatus;
    }

    public void setNewEmailStatus(String newEmailStatus) {
        this.newEmailStatus = newEmailStatus;
    }

    public NotificationConfiguration getNotificationConfiguration() {
        return notificationConfiguration;
    }

    public void setNotificationConfiguration(NotificationConfiguration notificationConfiguration) {
        this.notificationConfiguration = notificationConfiguration;
    }

    public String getLoginType() {
        return loginType;
    }

    public void setLoginType(String loginType) {
        this.loginType = loginType;
    }

    public Boolean getSyncGoogleCalendar() {
        return syncGoogleCalendar;
    }

    public void setSyncGoogleCalendar(Boolean syncGoogleCalendar) {
        this.syncGoogleCalendar = syncGoogleCalendar;
    }

    public List<Object> getProfilePictures() {
        return profilePictures;
    }

    public void setProfilePictures(List<Object> profilePictures) {
        this.profilePictures = profilePictures;
    }

    public List<Object> getAddress() {
        return address;
    }

    public void setAddress(List<Object> address) {
        this.address = address;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }

    public Long getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Long updatedAt) {
        this.updatedAt = updatedAt;
    }

}
