package com.example.rlapp.apimodel.exploremodules.affirmations;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import javax.annotation.processing.Generated;

@Generated("jsonschema2pojo")
public class Data {

    @SerializedName("affirmationList")
    @Expose
    private List<Affirmation> affirmationList;

    public List<Affirmation> getAffirmationList() {
        return affirmationList;
    }

    public void setAffirmationList(List<Affirmation> affirmationList) {
        this.affirmationList = affirmationList;
    }

}