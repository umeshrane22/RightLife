package com.example.rlapp.ui.Articles.models;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class FunFacts {

    @SerializedName("heading")
    @Expose
    private String heading;
    @SerializedName("description")
    @Expose
    private String description;

    public String getHeading() {
        return heading;
    }

    public void setHeading(String heading) {
        this.heading = heading;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}