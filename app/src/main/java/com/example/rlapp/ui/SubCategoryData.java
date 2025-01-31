package com.example.rlapp.ui;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SubCategoryData {

    @SerializedName("result")
    @Expose
    private List<SubCategoryResult> result;

    public List<SubCategoryResult> getResult() {
        return result;
    }

    public void setResult(List<SubCategoryResult> result) {
        this.result = result;
    }

}