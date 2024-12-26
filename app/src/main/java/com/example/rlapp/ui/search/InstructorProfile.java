package com.example.rlapp.ui.search;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class InstructorProfile {

    @SerializedName("firstName")
    @Expose
    private String firstName;
    @SerializedName("lastName")
    @Expose
    private String lastName;
    @SerializedName("socialMediaLinks")
    @Expose
    private List<Object> socialMediaLinks;
    @SerializedName("bio")
    @Expose
    private String bio;
    @SerializedName("introVideo")
    @Expose
    private IntroVideo introVideo;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("module")
    @Expose
    private String module;
    @SerializedName("imageUrl")
    @Expose
    private String imageUrl;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public List<Object> getSocialMediaLinks() {
        return socialMediaLinks;
    }

    public void setSocialMediaLinks(List<Object> socialMediaLinks) {
        this.socialMediaLinks = socialMediaLinks;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public IntroVideo getIntroVideo() {
        return introVideo;
    }

    public void setIntroVideo(IntroVideo introVideo) {
        this.introVideo = introVideo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

}